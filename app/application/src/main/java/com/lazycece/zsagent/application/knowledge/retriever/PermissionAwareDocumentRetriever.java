package com.lazycece.zsagent.application.knowledge.retriever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 带权限过滤的文档检索器。
 * 在 ES 向量检索的基础上，动态追加权限过滤条件，保证召回数量不因权限过滤而低于 topK。
 *
 * @author lazycece
 */
@Component
public class PermissionAwareDocumentRetriever implements DocumentRetriever {

    private static final Logger log = LoggerFactory.getLogger(PermissionAwareDocumentRetriever.class);

    private static final int TOP_K = 10;
    private static final double SIMILARITY_THRESHOLD = 0.65;

    private final VectorStore vectorStore;

    public PermissionAwareDocumentRetriever(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public List<Document> retrieve(Query query) {
        Map<String, Object> context = query.context();
        String userId = (String) context.getOrDefault("user_id", "");
        @SuppressWarnings("unchecked")
        List<String> userDepts = (List<String>) context.getOrDefault("user_depts", List.of());

        Filter.Expression permissionFilter = buildPermissionFilter(userId, userDepts);

        SearchRequest request = SearchRequest.builder()
                .query(query.text())
                .topK(TOP_K)
                .similarityThreshold(SIMILARITY_THRESHOLD)
                .filterExpression(permissionFilter)
                .build();
        List<Document> documents = vectorStore.similaritySearch(request);
        log.debug("权限过滤检索: userId={}, 部门数={}, 结果数={}", userId, userDepts.size(), documents.size());
        return documents;
    }

    /**
     * 构建权限过滤条件：
     * permission_type == 'public'
     * OR (permission_type == 'department' AND permission_depts IN userDepts)
     * OR (permission_type == 'specific' AND permission_users == userId)
     */
    private Filter.Expression buildPermissionFilter(String userId, List<String> userDepts) {
        Filter.Expression publicFilter = eq("permission_type", "public");
        Filter.Expression specificFilter = and(
                eq("permission_type", "specific"),
                eq("permission_users", userId));

        if (userDepts == null || userDepts.isEmpty()) {
            return or(publicFilter, specificFilter);
        }

        Filter.Expression departmentFilter = and(
                eq("permission_type", "department"),
                in("permission_depts", userDepts));
        return or(or(publicFilter, departmentFilter), specificFilter);
    }

    private Filter.Expression eq(String key, Object value) {
        return new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key(key), new Filter.Value(value));
    }

    private Filter.Expression in(String key, List<String> values) {
        return new Filter.Expression(Filter.ExpressionType.IN, new Filter.Key(key), new Filter.Value(values));
    }

    private Filter.Expression and(Filter.Expression left, Filter.Expression right) {
        return new Filter.Expression(Filter.ExpressionType.AND, left, right);
    }

    private Filter.Expression or(Filter.Expression left, Filter.Expression right) {
        return new Filter.Expression(Filter.ExpressionType.OR, left, right);
    }
}
