package com.lazycece.zsagent.application.agent.handler.rag;

import java.util.List;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.generation.augmentation.QueryAugmenter;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RAG Advisor 装配配置。
 * 将 Stage 1~4 的自定义组件组装为 Spring AI 的 {@link RetrievalAugmentationAdvisor}，
 * 注入 ChatClient 后即可实现模块化 RAG 流水线。
 *
 * @author lazycece
 */
@Configuration
public class RagAdvisorConfig {

    private static final String PROMPT_TEMPLATE = """
            ## 系统角色
            你是一个企业知识库助手，基于提供的文档内容回答用户问题。

            ## 核心规则
            1. **仅根据下文「参考文档」的内容回答**，不要使用你自己的知识。
            2. 如果「参考文档」中没有相关信息，回答："抱歉，当前知识库中暂未收录相关内容，建议联系人工客服获取帮助。"
            3. 回答需简洁、准确，必要时使用列表或步骤形式组织。
            4. **每条关键信息必须标注来源**，格式为 ①[文档标题]
            5. 不要提及"根据参考文档"等元描述，直接给出答案。

            ## 参考文档
            {context}

            ## 用户问题
            {user_query}
            """;

    private static final String EMPTY_CONTEXT_TEMPLATE = """
            ## 系统角色
            你是一个企业知识库助手。

            ## 用户问题
            {user_query}
            """;

    private final List<QueryTransformer> queryTransformer;
    private final DocumentRetriever documentRetriever;
    private final List<DocumentPostProcessor> documentPostProcessors;

    public RagAdvisorConfig(
            List<QueryTransformer> queryTransformer,
            DocumentRetriever documentRetriever,
            List<DocumentPostProcessor> documentPostProcessors) {
        this.queryTransformer = queryTransformer;
        this.documentRetriever = documentRetriever;
        this.documentPostProcessors = documentPostProcessors;
    }

    /**
     * 创建 QueryAugmenter（Spring AI 内置 ContextualQueryAugmenter）。
     * 使用 {context} 占位符接收文档格式化结果，{user_query} 接收用户问题。
     */
    @Bean
    public QueryAugmenter queryAugmenter() {
        return ContextualQueryAugmenter.builder()
                .promptTemplate(new PromptTemplate(PROMPT_TEMPLATE))
                .emptyContextPromptTemplate(new PromptTemplate(EMPTY_CONTEXT_TEMPLATE))
                .allowEmptyContext(true)
                .documentFormatter(this::formatDocuments)
                .build();
    }

    /**
     * 创建 RetrievalAugmentationAdvisor Bean。
     * DocumentPostProcessor 按 @Order 排序后自动收集：
     *   ① SimilarityThresholdFilter → ② DocumentCompressor → ③ DocumentCachePostProcessor
     */
    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(QueryAugmenter queryAugmenter) {
        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers(queryTransformer)
                .documentRetriever(documentRetriever)
                .documentPostProcessors(documentPostProcessors)
                .queryAugmenter(queryAugmenter)
                .build();
    }

    /**
     * 将检索到的文档格式化为带编号的引用文本。
     */
    private String formatDocuments(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            return "无参考文档";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < documents.size(); i++) {
            Document doc = documents.get(i);
            sb.append("[").append(i + 1).append("] ");
            String title = (String) doc.getMetadata().getOrDefault("document_title", "未知文档");
            sb.append(title).append("\n");
            sb.append(doc.getText()).append("\n\n");
        }
        return sb.toString();
    }
}
