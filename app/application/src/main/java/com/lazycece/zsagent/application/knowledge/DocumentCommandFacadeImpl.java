package com.lazycece.zsagent.application.knowledge;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.rapidf.utils.EnumUtils;
import com.lazycece.rapidf.utils.UUIDUtils;
import com.lazycece.zsagent.application.knowledge.etl.DocumentEtlOrchestrator;
import com.lazycece.zsagent.application.knowledge.validator.DocumentUploadValidator;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.repository.FileStorage;
import com.lazycece.zsagent.domain.knowledge.service.DocumentDomainService;
import com.lazycece.zsagent.domain.knowledge.valueobject.CreateDocumentCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.RollbackDocumentCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.UpdateDocumentContentCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.UpdateDocumentMetadataCommand;
import com.lazycece.zsagent.facade.knowledge.api.DocumentCommandFacade;
import com.lazycece.zsagent.facade.knowledge.request.DocumentDeleteRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRestoreRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRollbackRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateContentRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateMetadataRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUploadRequest;
import com.lazycece.zsagent.facade.knowledge.result.DocumentDeleteResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRestoreResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRollbackResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateContentResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateMetadataResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUploadResult;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 文档命令门面实现。
 * 负责文档上传、更新、删除、恢复、回滚的编排：文件落盘 → 领域操作 → 触发 ETL。
 *
 * @author lazycece
 */
@ApplicationService
public class DocumentCommandFacadeImpl implements DocumentCommandFacade {

    private final DocumentDomainService documentService;
    private final DocumentEtlOrchestrator etlOrchestrator;
    private final FileStorage fileStorage;

    public DocumentCommandFacadeImpl(DocumentDomainService documentService,
                                     DocumentEtlOrchestrator etlOrchestrator,
                                     FileStorage fileStorage) {
        this.documentService = documentService;
        this.etlOrchestrator = etlOrchestrator;
        this.fileStorage = fileStorage;
    }

    @Override
    public RespData<DocumentUploadResult> upload(DocumentUploadRequest request) {
        DocumentUploadValidator.validate(request);

        DocumentFormat format = detectFormat(request.getOriginalFilename());
        String title = StringUtils.isNotBlank(request.getTitle())
                ? request.getTitle()
                : extractFileNameWithoutExtension(request.getOriginalFilename());

        String filePath = buildFilePath(request.getUserId(), request.getOriginalFilename());
        long fileSize = storeFile(filePath, request.getFileContent());

        CreateDocumentCommand command = new CreateDocumentCommand();
        command.setUserId(request.getUserId());
        command.setTitle(title);
        command.setFormat(format);
        command.setFileSize(fileSize);
        command.setFilePath(filePath);
        command.setDirectoryId(request.getDirectoryId());
        command.setTags(request.getTags());
        command.setVisibility(EnumUtils.getEnum(Visibility.class, request.getVisibility()));
        command.setVisibleTo(request.getVisibleTo());
        String documentId = documentService.createDocument(command);

        etlOrchestrator.process(documentId);

        DocumentUploadResult result = new DocumentUploadResult();
        result.setDocumentId(documentId);
        result.setEtlStatus(EtlStatus.PENDING.getCode());
        return RespData.success(result);
    }

    @Override
    public RespData<DocumentUpdateMetadataResult> updateMetadata(DocumentUpdateMetadataRequest request) {
        UpdateDocumentMetadataCommand command = new UpdateDocumentMetadataCommand();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setTitle(request.getTitle());
        command.setSummary(request.getSummary());
        command.setDirectoryId(request.getDirectoryId());
        command.setTags(request.getTags());
        command.setVisibility(EnumUtils.getEnum(Visibility.class, request.getVisibility()));
        command.setVisibleTo(request.getVisibleTo());
        documentService.updateMetadata(command);
        return RespData.success(new DocumentUpdateMetadataResult());
    }

    @Override
    public RespData<DocumentUpdateContentResult> updateContent(DocumentUpdateContentRequest request) {
        String filePath = buildVersionFilePath(request.getUserId(), request.getDocumentId(),
                request.getOriginalFilename());
        long fileSize = storeFile(filePath, request.getFileContent());

        UpdateDocumentContentCommand command = new UpdateDocumentContentCommand();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setFilePath(filePath);
        command.setFileSize(fileSize);
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
        RollbackDocumentCommand command = new RollbackDocumentCommand();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setTargetVersionId(request.getTargetVersionId());
        documentService.rollback(command);
        etlOrchestrator.reprocess(request.getDocumentId());
        return RespData.success(new DocumentRollbackResult());
    }

    /**
     * 根据文件扩展名识别格式。
     */
    private DocumentFormat detectFormat(String filename) {
        if (StringUtils.isBlank(filename)) {
            return DocumentFormat.OTHER;
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            return DocumentFormat.OTHER;
        }
        String ext = filename.substring(dotIndex + 1).toLowerCase();
        return switch (ext) {
            case "pdf" -> DocumentFormat.PDF;
            case "docx" -> DocumentFormat.DOCX;
            case "md", "markdown" -> DocumentFormat.MD;
            case "html", "htm" -> DocumentFormat.HTML;
            case "txt" -> DocumentFormat.TXT;
            case "xlsx" -> DocumentFormat.XLSX;
            case "pptx" -> DocumentFormat.PPTX;
            case "csv" -> DocumentFormat.CSV;
            default -> DocumentFormat.OTHER;
        };
    }

    /**
     * 从文件名提取标题（去除扩展名）。
     */
    private String extractFileNameWithoutExtension(String filename) {
        if (StringUtils.isBlank(filename)) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(0, dotIndex) : filename;
    }

    /**
     * 构建上传文件存储路径。
     */
    private String buildFilePath(String userId, String filename) {
        return "documents/" + userId + "/" + UUIDUtils.uuid() + "/" + filename;
    }

    /**
     * 构建新版本文件存储路径。
     */
    private String buildVersionFilePath(String userId, String documentId, String filename) {
        return "documents/" + documentId + "/" + UUIDUtils.uuid() + "/" + filename;
    }

    /**
     * 保存文件，将 IO 异常包装为运行时异常。
     */
    private long storeFile(String filePath, byte[] content) {
        try {
            return fileStorage.store(filePath, new ByteArrayInputStream(content));
        } catch (IOException e) {
            throw ExceptionFactory.serverException("文件存储失败", e);
        }
    }
}
