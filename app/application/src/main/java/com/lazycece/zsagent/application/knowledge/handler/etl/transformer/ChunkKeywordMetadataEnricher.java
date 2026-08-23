package com.lazycece.zsagent.application.knowledge.handler.etl.transformer;

import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.utils.DefaultUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;

import java.util.List;

/**
 * @author lazycece
 */
@ApplicationHandler
public class ChunkKeywordMetadataEnricher implements DocumentTransformer {

    public static final String KEYWORDS_TEMPLATE = """
            你是一个知识管理助手，本节内容如下
            {context_str}.
            
            请为此内容提取 3~5 个唯一关键词标签，格式为英文逗号分隔。
            
            关键词：""";

    private final KeywordMetadataEnricher keywordMetadataEnricher;

    public ChunkKeywordMetadataEnricher(ChatModel chatModel) {
        this.keywordMetadataEnricher = KeywordMetadataEnricher.builder(chatModel)
                // keywordsTemplate
                .keywordsTemplate(new PromptTemplate(KEYWORDS_TEMPLATE))
                //
                .build();
    }

    @Override
    public List<Document> apply(List<Document> documents) {
        if (DefaultUtils.defaultList(documents).isEmpty()) {
            return List.of();
        }
        return keywordMetadataEnricher.apply(documents);
    }
}
