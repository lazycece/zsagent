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
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDocumentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.RollbackDocumentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentContentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentMetadataCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateEtlStatusCmd;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

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
    private final TransactionTemplate transactionTemplate;

    public DocumentDomainServiceImpl(DocumentRepository documentRepository,
                                     DocumentVersionRepository versionRepository,
                                     TransactionTemplate transactionTemplate) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * 创建文档，并创建初始版本（V1）。
     */
    @Override
    public String createDocument(CreateDocumentCmd command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        // build
        Document document = Document.create(command);
        DocumentVersion version = document.createNewVersion(command.getFilePath(), command.getFileSize(), "初始版本");
        // persistence
        transactionTemplate.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {
                documentRepository.save(document);
                versionRepository.save(List.of(version));
            }
        });

        return document.getDocumentId();
    }

    /**
     * 更新文档元数据，不产生新版本。
     */
    @Override
    public void updateMetadata(UpdateDocumentMetadataCmd command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        // load
        Document document = documentRepository.findById(command.getDocumentId());
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");

        // persistence
        document.updateMetadata(command);
        documentRepository.update(document);
    }

    /**
     * 更新文档内容，创建新版本并重置 ETL 状态。
     */
    @Override
    public DocumentVersion updateContent(UpdateDocumentContentCmd command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        // load
        Document document = documentRepository.findById(command.getDocumentId());
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");

        // update
        DocumentVersion version = document.updateContent(command);

        // persistence
        transactionTemplate.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {
                documentRepository.update(document);
                versionRepository.save(List.of(version));
            }
        });
        return version;
    }

    /**
     * 删除文档（移入回收站）。
     */
    @Override
    public void delete(String userId, String documentId) {
        Assert.notBlank(userId, RespStatus.PARAM_ERROR, "userId 不能为空");
        Assert.notBlank(documentId, RespStatus.PARAM_ERROR, "documentId 不能为空");
        // load
        Document document = documentRepository.findById(documentId);
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        // persistence
        document.delete(userId);
        documentRepository.update(document);
    }

    /**
     * 恢复文档（从回收站）。
     */
    @Override
    public void restore(String userId, String documentId) {
        Assert.notBlank(userId, RespStatus.PARAM_ERROR, "userId 不能为空");
        Assert.notBlank(documentId, RespStatus.PARAM_ERROR, "documentId 不能为空");
        // load
        Document document = documentRepository.findById(documentId);
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        // persistence
        document.restore(userId);
        documentRepository.update(document);
    }

    /**
     * 回滚到指定版本——创建新版本（复用目标版本文件）。
     */
    @Override
    public DocumentVersion rollback(RollbackDocumentCmd command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        // load
        DocumentVersion targetVersion = versionRepository.findByVersionId(command.getTargetVersionId());
        Assert.notNull(targetVersion, RespStatus.PARAM_ERROR, "版本不存在");
        Document document = documentRepository.findById(command.getDocumentId());
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        // rollback
        DocumentVersion newVersion = document.rollback(command, targetVersion);
        // persistence
        transactionTemplate.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {
                documentRepository.update(document);
                versionRepository.save(List.of(newVersion));
            }
        });
        return newVersion;
    }

    /**
     * 更新 ETL 状态。
     */
    @Override
    public void updateEtlStatus(UpdateEtlStatusCmd command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        // load
        Document document = documentRepository.findById(command.getDocumentId());
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");

        if (command.getStatus() == EtlStatus.FAILED) {
            document.markEtlFailed(command.getErrorMessage());
        } else {
            document.updateEtlStatus(command.getStatus());
            document.setEtlErrorMessage(null);
        }
        // persistence
        documentRepository.update(document);
    }

    /**
     * ETL 完成后发布文档。
     */
    @Override
    public void publish(String documentId) {
        Assert.notBlank(documentId, RespStatus.PARAM_ERROR, "documentId 不能为空");
        // load
        Document document = documentRepository.findById(documentId);
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在");
        // publish
        document.publish();
        // persistence
        documentRepository.update(document);
    }
}
