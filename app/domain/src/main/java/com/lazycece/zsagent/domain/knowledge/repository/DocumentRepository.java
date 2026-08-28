package com.lazycece.zsagent.domain.knowledge.repository;

import com.lazycece.rapidf.domain.model.Pagination;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentCountQuery;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentListQuery;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentQuery;

import java.util.List;

/**
 * 文档仓储接口
 *
 * @author lazycece
 */
public interface DocumentRepository {

    /**
     * 新建文档。
     *
     * @param document 文档聚合根
     */
    void save(Document document);

    /**
     * 按 ID 查询（含权限校验，只能查自己有权限的）。
     *
     * @param query 查询条件（userId + userDepts + documentId）
     * @return 文档聚合根，不存在或无权限时返回 null
     */
    Document findByDocumentId(DocumentQuery query);

    /**
     * 按 ID 查询（无权限过滤，仅供内部 ETL / 定时任务使用）。
     *
     * @param documentId 文档ID
     * @return 文档聚合根，不存在时返回 null
     */
    Document findById(String documentId);

    /**
     * 分页查询文档列表（用户可见范围内）。
     *
     * @param query      筛选条件
     * @param pagination 分页参数
     * @return 文档列表
     */
    List<Document> findByUserId(DocumentListQuery query, Pagination pagination);

    /**
     * 统计文档数（用户可见范围内）。
     *
     * @param query 筛选条件
     * @return 文档数
     */
    int countByUserId(DocumentCountQuery query);

    /**
     * 更新文档（元数据、状态、ETL 状态）。
     *
     * @param document 文档聚合根
     */
    void update(Document document);

    /**
     * 按目录统计文档数（删除目录前校验）。
     *
     * @param directoryId 目录ID
     * @return 文档数
     */
    int countByDirectoryId(String directoryId);
}
