package com.lazycece.zsagent.application.agent.rag.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 相似度阈值过滤器（Stage 3a）。
 * Stage 2 用宽松阈值（0.65）召回候选，此处做精确过滤（0.70），
 * 过滤掉相似度过低的结果，减少后续 LLM 输入的噪声。
 *
 * @author lazycece
 */
@Component
@Order(1)
public class SimilarityThresholdFilter implements DocumentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(SimilarityThresholdFilter.class);

    private static final double THRESHOLD = 0.70;

    @Override
    public List<Document> process(Query query, List<Document> documents) {
        List<Document> filtered = documents.stream()
                .filter(doc -> {
                    Object scoreObj = doc.getMetadata().getOrDefault("score", 0.0);
                    double score = scoreObj instanceof Number n ? n.doubleValue() : 0.0;
                    return score >= THRESHOLD;
                })
                .collect(Collectors.toList());
        log.debug("相似度过滤: {} -> {} 条文档 (threshold={})", documents.size(), filtered.size(), THRESHOLD);
        return filtered;
    }
}
