package com.lazycece.zsagent.application.knowledge;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.rapidf.utils.EnumUtils;
import com.lazycece.zsagent.application.knowledge.etl.DocumentEtlOrchestrator;
import com.lazycece.zsagent.application.knowledge.validator.DocumentCreateValidator;
import com.lazycece.zsagent.domain.common.utils.FileUtils;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.service.DocumentDomainService;
import com.lazycece.zsagent.domain.knowledge.utils.DocumentUtils;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDocumentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.RollbackDocumentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentContentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentMetadataCmd;
import com.lazycece.zsagent.facade.knowledge.api.DocumentCommandFacade;
import com.lazycece.zsagent.facade.knowledge.request.DocumentDeleteRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRestoreRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRollbackRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateContentRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateMetadataRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentCreateRequest;
import com.lazycece.zsagent.facade.knowledge.result.DocumentDeleteResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRestoreResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRollbackResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateContentResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateMetadataResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentCreateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;

/**
 * 文档命令门面实现。
 * 负责文档创建、更新、删除、恢复、回滚的编排：文件路径 → 领域操作 → 触发 ETL。
 * 文件本体已由文件上传接口（FileCommandFacade）预先落盘，此处仅接收相对路径。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class DocumentCommandFacadeImpl implements DocumentCommandFacade {

    private final DocumentDomainService documentService;
    private final DocumentEtlOrchestrator etlOrchestrator;

    public DocumentCommandFacadeImpl(DocumentDomainService documentService,
                                     DocumentEtlOrchestrator etlOrchestrator) {
        this.documentService = documentService;
        this.etlOrchestrator = etlOrchestrator;
    }

    @Override
    public RespData<DocumentCreateResult> create(DocumentCreateRequest request) {
        DocumentCreateValidator.validate(request);

        DocumentFormat format = DocumentUtils.detectFormat(request.getFilePath());
        String title = StringUtils.isNotBlank(request.getTitle())
                ? request.getTitle()
                : extractFileNameWithoutExtension(request.getFilePath());

        CreateDocumentCmd command = new CreateDocumentCmd();
        command.setUserId(request.getUserId());
        command.setTitle(title);
        command.setFormat(format);
        command.setFilePath(request.getFilePath());
        command.setDirectoryId(request.getDirectoryId());
        command.setTags(request.getTags());
        command.setVisibility(EnumUtils.getEnum(Visibility.class, request.getVisibility()));
        command.setVisibleTo(request.getVisibleTo());
        String documentId = documentService.createDocument(command);

        etlOrchestrator.process(documentId);

        DocumentCreateResult result = new DocumentCreateResult();
        result.setDocumentId(documentId);
        result.setEtlStatus(EtlStatus.PENDING.getCode());
        return RespData.success(result);
    }

    @Override
    public RespData<DocumentUpdateMetadataResult> updateMetadata(DocumentUpdateMetadataRequest request) {
        UpdateDocumentMetadataCmd command = new UpdateDocumentMetadataCmd();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setTitle(request.getTitle());
        command.setSummary(request.getSummary());
        command.setDirectoryId(request.getDirectoryId());
        command.setTags(request.getTags());
        command.setVisibility(StringUtils.isNotBlank(request.getVisibility())
                ? EnumUtils.getEnum(Visibility.class, request.getVisibility())
                : null);
        command.setVisibleTo(request.getVisibleTo());
        documentService.updateMetadata(command);
        return RespData.success(new DocumentUpdateMetadataResult());
    }

    @Override
    public RespData<DocumentUpdateContentResult> updateContent(DocumentUpdateContentRequest request) {
        UpdateDocumentContentCmd command = new UpdateDocumentContentCmd();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setFilePath(request.getFilePath());
        command.setChangeLog(request.getChangeLog());
        documentService.updateContent(command);

        etlOrchestrator.reprocess(request.getDocumentId());
        return RespData.success(new DocumentUpdateContentResult());
    }

    @Override
    public RespData<DocumentDeleteResult> delete(DocumentDeleteRequest request) {
        documentService.delete(request.getUserId(), request.getDocumentId());
        etlOrchestrator.markDeleted(request.getDocumentId());
        return RespData.success(new DocumentDeleteResult());
    }

    @Override
    public RespData<DocumentRestoreResult> restore(DocumentRestoreRequest request) {
        documentService.restore(request.getUserId(), request.getDocumentId());
        etlOrchestrator.markRestored(request.getDocumentId());
        return RespData.success(new DocumentRestoreResult());
    }

    @Override
    public RespData<DocumentRollbackResult> rollback(DocumentRollbackRequest request) {
        RollbackDocumentCmd command = new RollbackDocumentCmd();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setTargetVersionId(request.getTargetVersionId());
        documentService.rollback(command);
        etlOrchestrator.reprocess(request.getDocumentId());
        return RespData.success(new DocumentRollbackResult());
    }


    /**
     * 从路径提取文件名（去除扩展名），用作默认标题。
     */
    private String extractFileNameWithoutExtension(String filePath) {
        String filename = FileUtils.extractFilename(filePath);
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(0, dotIndex) : filename;
    }
}
