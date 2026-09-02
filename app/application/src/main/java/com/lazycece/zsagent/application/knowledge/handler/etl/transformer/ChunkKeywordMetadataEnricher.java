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
package com.lazycece.zsagent.application.knowledge.handler.etl.transformer;

import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.utils.DefaultUtils;
import java.util.List;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;

/**
 * @author lazycece
 */
@ApplicationHandler
public class ChunkKeywordMetadataEnricher implements DocumentTransformer {

    public static final String KEYWORDS_TEMPLATE =
            """
            你是一个知识管理助手，本节内容如下
            {context_str}.

            请为此内容提取 3~5 个唯一关键词标签，格式为英文逗号分隔。

            关键词：""";

    private final KeywordMetadataEnricher keywordMetadataEnricher;

    public ChunkKeywordMetadataEnricher(ChatModel chatModel) {
        this.keywordMetadataEnricher =
                KeywordMetadataEnricher.builder(chatModel)
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
