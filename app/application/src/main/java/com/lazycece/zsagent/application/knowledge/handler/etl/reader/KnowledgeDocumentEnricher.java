/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lazycece.zsagent.application.knowledge.handler.etl.reader;

import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.zsagent.domain.knowledge.valueobject.EnrichResult;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.template.st.StTemplateRenderer;

/**
 * 文档增强器，通过 LLM 生成摘要与候选标签。
 *
 * @author lazycece
 */
@ApplicationHandler
public class KnowledgeDocumentEnricher {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeDocumentEnricher.class);

    private static final int PREVIEW_LENGTH = 2000;
    private static final String TITLE_PLACEHOLDER = "title";
    private static final String PREVIEW_LENGTH_PLACEHOLDER = "preview_length";
    private static final String PREVIEW_CONTENT_PLACEHOLDER = "preview_content";

    private static final String DOCUMENT_ENRICHER_TEMPLATE =
            """
            你是一个知识管理助手。根据以下文档内容完成两个任务:
            1. 生成一段简洁的摘要（不超过 200 字）
            2. 提取 3~5 个关键词标签

            ## 文档标题
            <title>

            ## 文档内容（前 <preview_length> 字）
            <preview_content>

            ## 输出格式（严格按 JSON 输出，不要输出其他内容
            {"summary": "文档摘要", "tags": ["标签1", "标签2", "标签3"]}""";

    private final ChatClient.Builder chatClientBuilder;

    public KnowledgeDocumentEnricher(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    /**
     * 生成文档摘要与标签。
     */
    public EnrichResult enrich(List<Document> aiDocList, String title) {
        try {
            // 文档内容截取
            String previewContent = this.handlePreviewContent(aiDocList);
            if (StringUtils.isBlank(previewContent)) {
                return EnrichResult.empty();
            }

            // 提示词构建
            PromptTemplate promptTemplate =
                    PromptTemplate.builder()
                            .renderer(
                                    StTemplateRenderer.builder()
                                            .startDelimiterToken('<')
                                            .endDelimiterToken('>')
                                            .build())
                            .template(DOCUMENT_ENRICHER_TEMPLATE)
                            .build();
            Prompt prompt =
                    promptTemplate.create(
                            Map.of(
                                    TITLE_PLACEHOLDER,
                                    title,
                                    PREVIEW_LENGTH_PLACEHOLDER,
                                    PREVIEW_LENGTH,
                                    PREVIEW_CONTENT_PLACEHOLDER,
                                    previewContent));

            // LLM invoke
            EnrichResult enrichResult =
                    chatClientBuilder.build().prompt(prompt).call().entity(EnrichResult.class);

            return enrichResult == null ? EnrichResult.empty() : enrichResult;

        } catch (Exception e) {
            log.warn("文档级别摘要/标签生成失败，默认返回空结果, title={}", title, e);
            return EnrichResult.empty();
        }
    }

    private String handlePreviewContent(List<Document> aiDocList) {
        StringBuilder previewContent = new StringBuilder();
        for (Document doc : DefaultUtils.defaultList(aiDocList)) {
            previewContent.append(doc.getText());
            if (previewContent.length() > PREVIEW_LENGTH) {
                previewContent = new StringBuilder(previewContent.substring(0, PREVIEW_LENGTH));
                break;
            }
        }
        return previewContent.toString();
    }
}
