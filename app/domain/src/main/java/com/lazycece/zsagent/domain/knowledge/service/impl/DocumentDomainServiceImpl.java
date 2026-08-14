package com.lazycece.zsagent.domain.knowledge.service.impl;

import com.lazycece.rapidf.domain.anotation.DomainService;
import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.model.DocumentVersion;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentVersionRepository;
import com.lazycece.zsagent.domain.knowledge.service.DocumentDomainService;
import com.lazycece.zsagent.domain.knowledge.valueobject.CreateDocumentCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.RollbackDocumentCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.UpdateDocumentContentCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.UpdateDocumentMetadataCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.UpdateEtlStatusCommand;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档领域服务实现。
 * 领域服务仅做编排：参数校验 → 获取聚合 → 委托聚合行为 → 持久化。
 * 业务规则封装在聚合根内部。
 *
 * @author lazycece
 */
@DomainService
public class DocumentDomainServiceImpl implements DocumentDomainService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;

    public DocumentDomainServiceImpl(DocumentRepository documentRepository,
                                     DocumentVersionRepository versionRepository) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
    }

    /**
     * 创建文档，并创建初始版本（V1）。
     */
    @Override
    public String createDocument(CreateDocumentCommand command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        Document document = Document.create(command);
        DocumentVersion version = document.createNewVersion(
                command.filePath(), command.fileSize(), "初始版本");
        documentRepository.save(document);
        versionRepository.save(List.of(version));
        return document.getDocumentId();
    }

    /**
     * 更新文档元数据，不产生新版本。
     */
    @Override
    public void updateMetadata(UpdateDocumentMetadataCommand command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        Document document = documentRepository.findById(command.documentId());
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        document.setUpdater(command.userId());
        document.setUpdateTime(LocalDateTime.now());
        document.updateMetadata(command.title(), command.summary(), command.directoryId(),
                command.tags(), command.visibility(), command.visibleTo());
        documentRepository.update(document);
    }

    /**
     * 更新文档内容，创建新版本并重置 ETL 状态。
     */
    @Override
    public DocumentVersion updateContent(UpdateDocumentContentCommand command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        Document document = documentRepository.findById(command.documentId());
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        document.setUpdater(command.userId());
        document.setUpdateTime(LocalDateTime.now());
        document.updateEtlStatus(EtlStatus.PENDING);
        DocumentVersion version = document.createNewVersion(
                command.filePath(), command.fileSize(), command.changeLog());
        documentRepository.update(document);
        versionRepository.save(List.of(version));
        return version;
    }

    /**
     * 删除文档（移入回收站）。
     */
    @Override
    public void delete(String userId, String documentId) {
        Document document = documentRepository.findById(documentId);
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        document.setUpdater(userId);
        document.setUpdateTime(LocalDateTime.now());
        document.delete();
        documentRepository.update(document);
    }

    /**
     * 恢复文档（从回收站）。
     */
    @Override
    public void restore(String userId, String documentId) {
        Document document = documentRepository.findById(documentId);
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        document.setUpdater(userId);
        document.setUpdateTime(LocalDateTime.now());
        document.restore();
        documentRepository.update(document);
    }

    /**
     * 回滚到指定版本——创建新版本（复用目标版本文件）。
     */
    @Override
    public DocumentVersion rollback(RollbackDocumentCommand command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        DocumentVersion targetVersion = versionRepository.findByVersionId(command.targetVersionId());
        Assert.notNull(targetVersion, RespStatus.PARAM_ERROR, "版本不存在");
        Document document = documentRepository.findById(command.documentId());
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        document.setUpdater(command.userId());
        document.setUpdateTime(LocalDateTime.now());
        document.updateEtlStatus(EtlStatus.PENDING);
        DocumentVersion newVersion = document.createNewVersion(
                targetVersion.getFilePath(), targetVersion.getFileSize(),
                "回滚到 V" + targetVersion.getVersionNumber());
        documentRepository.update(document);
        versionRepository.save(List.of(newVersion));
        return newVersion;
    }

    /**
     * 更新 ETL 状态。
     */
    @Override
    public void updateEtlStatus(UpdateEtlStatusCommand command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        Document document = documentRepository.findById(command.documentId());
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        if (command.status() == EtlStatus.FAILED) {
            document.markEtlFailed(command.errorMessage());
        } else {
            document.updateEtlStatus(command.status());
            document.setEtlErrorMessage(null);
        }
        documentRepository.update(document);
    }

    /**
     * ETL 完成后发布文档。
     */
    @Override
    public void publish(String documentId) {
        Document document = documentRepository.findById(documentId);
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        document.publish();
        documentRepository.update(document);
    }
}
