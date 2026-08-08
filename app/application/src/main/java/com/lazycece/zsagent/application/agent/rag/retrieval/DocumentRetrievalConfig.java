package com.lazycece.zsagent.application.agent.rag.retrieval;

import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文档检索配置（Stage 2）。
 * 基于 auto-config 注入的 {@link VectorStore} 构建 {@link VectorStoreDocumentRetriever}。
 *
 * @author lazycece
 */
@Configuration
public class DocumentRetrievalConfig {

    /**
     * 创建向量存储文档检索器。
     * topK=10 候选数大于最终需要数，留给 Stage 3 后处理过滤；
     * similarityThreshold=0.65 宽松阈值，精确过滤交给 Stage 3。
     */
    @Bean
    public DocumentRetriever documentRetriever(VectorStore vectorStore) {
        return VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .topK(10)
                .similarityThreshold(0.65)
                .build();
    }
}
