package com.lazycece.zsagent.domain.knowledge.service;

import com.lazycece.zsagent.domain.knowledge.model.DocumentVersion;
import com.lazycece.zsagent.domain.knowledge.valueobject.CreateDocumentCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.RollbackDocumentCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.UpdateDocumentContentCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.UpdateDocumentMetadataCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.UpdateEtlStatusCommand;

/**
 * 文档领域服务接口
 *
 * @author lazycece
 */
public interface DocumentDomainService {

    /**
     * 创建文档——初始化聚合、保存到仓储、返回 documentId。
     *
     * @param command 创建文档命令
     * @return documentId
     */
    String createDocument(CreateDocumentCommand command);

    /**
     * 更新文档元数据（标题、标签、目录、权限），不产生新版本。
     *
     * @param command 更新元数据命令
     */
    void updateMetadata(UpdateDocumentMetadataCommand command);

    /**
     * 更新文档文件内容——创建新版本 + 重置 ETL 状态为 PENDING。
     *
     * @param command 更新内容命令
     * @return 新创建的版本
     */
    DocumentVersion updateContent(UpdateDocumentContentCommand command);

    /**
     * 删除文档——移入回收站。
     *
     * @param userId     操作者
     * @param documentId 文档ID
     */
    void delete(String userId, String documentId);

    /**
     * 恢复文档——从回收站恢复。
     *
     * @param userId     操作者
     * @param documentId 文档ID
     */
    void restore(String userId, String documentId);

    /**
     * 回滚到指定版本——创建新版本（复用目标版本文件），重置 ETL。
     *
     * @param command 回滚命令
     * @return 新创建的版本
     */
    DocumentVersion rollback(RollbackDocumentCommand command);

    /**
     * 更新 ETL 状态。
     *
     * @param command ETL 状态命令
     */
    void updateEtlStatus(UpdateEtlStatusCommand command);

    /**
     * ETL 完成后发布文档（DRAFT → PUBLISHED）。
     *
     * @param documentId 文档ID
     */
    void publish(String documentId);
}
