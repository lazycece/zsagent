package com.lazycece.zsagent.application.knowledge.etl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 文档 ETL 异步编排器。
 * 完整 ETL 流水线（解析 → 分块 → 增强 → 向量化 → 索引 → 发布）在 step 7 实现。
 *
 * @author lazycece
 */
@Component
public class DocumentEtlOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(DocumentEtlOrchestrator.class);

    /**
     * 处理文档（完整 ETL 流水线）。
     */
    public void process(String documentId) {
        log.warn("ETL 流水线尚未实现（step 7），documentId={}", documentId);
    }

    /**
     * 重新处理文档（删除旧 chunk 后重新 ETL）。
     */
    public void reprocess(String documentId) {
        log.warn("ETL 重新处理尚未实现（step 7），documentId={}", documentId);
    }

    /**
     * 标记文档删除（清理 ES chunk）。
     */
    public void markDeleted(String documentId) {
        log.warn("ETL 标记删除尚未实现（step 7），documentId={}", documentId);
    }

    /**
     * 标记文档恢复（重新 ETL）。
     */
    public void markRestored(String documentId) {
        log.warn("ETL 标记恢复尚未实现（step 7），documentId={}", documentId);
    }
}
