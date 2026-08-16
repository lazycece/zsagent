package com.lazycece.zsagent.domain.agent.repository;

import com.lazycece.zsagent.domain.agent.model.KnowledgeChunk;

import java.util.List;

/**
 * 知识块仓储接口（检索在 ES 中）
 *
 * @author lazycece
 */
public interface KnowledgeChunkRepository {

    /**
     * 语义检索，返回与查询文本最相关的 topK 个知识块。
     *
     * @param query 查询文本（原始文本，由实现层负责向量化）
     * @param topK  返回数量
     * @return 相关知识块列表
     */
    List<KnowledgeChunk> search(String query, int topK);

    /**
     * 批量索引知识块。
     *
     * @param chunks 待索引的知识块列表
     */
    void index(List<KnowledgeChunk> chunks);

    /**
     * 按文档 ID 批量删除知识块（文档更新或删除时调用）。
     *
     * @param documentId 文档ID
     */
    void deleteByDocumentId(String documentId);
}
