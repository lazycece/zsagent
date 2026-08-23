package com.lazycece.zsagent.infra.acl.repository;

import com.lazycece.rapidf.domain.anotation.DomainRepository;
import com.lazycece.zsagent.domain.agent.model.KnowledgeChunk;
import com.lazycece.zsagent.domain.agent.repository.KnowledgeChunkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 知识块仓储 ES VectorStore 实现。
 *
 * @author lazycece
 */
@DomainRepository
public class KnowledgeChunkRepositoryImpl implements KnowledgeChunkRepository {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeChunkRepositoryImpl.class);

    private final VectorStore vectorStore;

    public KnowledgeChunkRepositoryImpl(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public List<KnowledgeChunk> search(String query, int topK) {
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(SearchRequest.SIMILARITY_THRESHOLD_ACCEPT_ALL)
                .build();
        List<Document> docs = vectorStore.similaritySearch(request);
        log.debug("语义检索: query={}, topK={}, 结果数={}", query, topK, docs.size());
        return docs.stream()
                .map(this::toKnowledgeChunk)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByDocumentId(String documentId) {
        Filter.Expression filter = new Filter.Expression(
                Filter.ExpressionType.EQ,
                new Filter.Key("document_id"),
                new Filter.Value(documentId));
        vectorStore.delete(filter);
        log.info("按文档删除知识块: documentId={}", documentId);
    }

    /**
     * Spring AI Document → 领域 KnowledgeChunk。
     */
    private KnowledgeChunk toKnowledgeChunk(Document doc) {
        KnowledgeChunk chunk = new KnowledgeChunk();
        chunk.setChunkId(doc.getId());
        chunk.setDocumentId((String) doc.getMetadata().getOrDefault("document_id", ""));
        chunk.setDocumentTitle((String) doc.getMetadata().getOrDefault("document_title", ""));
        chunk.setContent(doc.getText());
        Double score = doc.getScore();
        chunk.setScore(score != null ? score.floatValue() : 0.0f);

        Map<String, Object> metadata = new HashMap<>(doc.getMetadata());
        metadata.remove("document_id");
        metadata.remove("document_title");
        metadata.remove("embedding");
        chunk.setMetadata(metadata);
        return chunk;
    }
}
