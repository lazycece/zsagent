package com.lazycece.zsagent.application.knowledge.handler.etl;

/**
 * @author lazycece
 */
public interface DocumentEtlOrchestrator {

    /**
     * 处理文档（完整 ETL 流水线）。
     */
    void process(String documentId);

    /**
     * 重新处理文档（删除旧 chunk 后重新 ETL）。
     */
    void reprocess(String documentId);

    /**
     * 标记文档删除（清理 ES chunk）。
     */
    void markDeleted(String documentId);

    /**
     * 标记文档恢复（重新 ETL）。
     */
    void markRestored(String documentId);
}
