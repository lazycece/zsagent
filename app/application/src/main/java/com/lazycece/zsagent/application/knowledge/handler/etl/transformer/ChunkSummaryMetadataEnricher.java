package com.lazycece.zsagent.application.knowledge.handler.etl.transformer;

import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.utils.DefaultUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;

import java.util.List;

/**
 * @author lazycece
 */
@ApplicationHandler
public class ChunkSummaryMetadataEnricher implements DocumentTransformer {

    private final static String SUMMARY_TEMPLATE = """
            你是一个知识管理助手，本节内容如下：
            {context_str}
            
            概括本节的关键主题和内容。
            
            摘要：""";

    private final SummaryMetadataEnricher summaryMetadataEnricher;

    public ChunkSummaryMetadataEnricher(ChatModel chatModel) {
        this.summaryMetadataEnricher = new SummaryMetadataEnricher(
                //
                chatModel,
                //
                List.of(SummaryMetadataEnricher.SummaryType.PREVIOUS, SummaryMetadataEnricher.SummaryType.CURRENT, SummaryMetadataEnricher.SummaryType.NEXT),
                //
                SUMMARY_TEMPLATE,
                //
                MetadataMode.ALL

        );
    }

    @Override
    public List<Document> apply(List<Document> documents) {
        if (DefaultUtils.defaultList(documents).isEmpty()) {
            return List.of();
        }
        return summaryMetadataEnricher.apply(documents);
    }

}
