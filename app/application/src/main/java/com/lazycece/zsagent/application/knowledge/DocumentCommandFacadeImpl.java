package com.lazycece.zsagent.application.knowledge;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.application.knowledge.assembler.DocumentAssembler;
import com.lazycece.zsagent.application.knowledge.etl.DocumentEtlOrchestrator;
import com.lazycece.zsagent.application.knowledge.validator.DocumentCreateValidator;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import com.lazycece.zsagent.domain.knowledge.service.DocumentDomainService;
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
        String documentId = documentService.createDocument(DocumentAssembler.toCreateDocumentCmd(request));

        etlOrchestrator.process(documentId);

        DocumentCreateResult result = new DocumentCreateResult();
        result.setDocumentId(documentId);
        result.setEtlStatus(EtlStatus.PENDING.getCode());
        return RespData.success(result);
    }

    @Override
    public RespData<DocumentUpdateMetadataResult> updateMetadata(DocumentUpdateMetadataRequest request) {
        documentService.updateMetadata(DocumentAssembler.toUpdateMetadataCmd(request));
        return RespData.success(new DocumentUpdateMetadataResult());
    }

    @Override
    public RespData<DocumentUpdateContentResult> updateContent(DocumentUpdateContentRequest request) {
        documentService.updateContent(DocumentAssembler.toUpdateContentCmd(request));

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
        documentService.rollback(DocumentAssembler.toRollbackCmd(request));
        etlOrchestrator.reprocess(request.getDocumentId());
        return RespData.success(new DocumentRollbackResult());
    }
}
