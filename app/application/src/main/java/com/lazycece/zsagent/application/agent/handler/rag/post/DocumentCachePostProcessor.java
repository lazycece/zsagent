package com.lazycece.zsagent.application.agent.handler.rag.post;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 文档缓存处理器（Stage 3c）。 在后处理链最末缓存当前对话的检索结果，供流式生成完成后提取来源引用。 仅做缓存，不修改文档列表。
 *
 * @author lazycece
 */
@Component
@Order(3)
public class DocumentCachePostProcessor implements DocumentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(DocumentCachePostProcessor.class);

    private final ConcurrentHashMap<String, List<Document>> documentCache = new ConcurrentHashMap<>();

    @Override
    public List<Document> process(Query query, List<Document> documents) {
        String conversationId = (String) query.context().get(ChatMemory.CONVERSATION_ID);
        if (conversationId != null) {
            this.documentCache.put(conversationId, documents);
            log.debug("缓存检索文档: conversationId={}, 文档数={}", conversationId,
                    documents.size());
        }
        return documents;
    }

    /**
     * 获取指定对话最近一次检索到的文档列表。
     */
    public List<Document> getLastRetrievedDocuments(String conversationId) {
        List<Document> docs = documentCache.get(conversationId);
        return docs != null ? docs : List.of();
    }

    /**
     * 清理指定对话的缓存文档。
     */
    public void clearDocuments(String conversationId) {
        documentCache.remove(conversationId);
    }

}
