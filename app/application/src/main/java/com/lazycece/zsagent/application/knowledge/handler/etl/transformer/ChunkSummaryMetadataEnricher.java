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
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;

/**
 * @author lazycece
 */
@ApplicationHandler
public class ChunkSummaryMetadataEnricher implements DocumentTransformer {

    private static final String SUMMARY_TEMPLATE =
            """
            你是一个知识管理助手，本节内容如下：
            {context_str}

            概括本节的关键主题和内容。

            摘要：""";

    private final SummaryMetadataEnricher summaryMetadataEnricher;

    public ChunkSummaryMetadataEnricher(ChatModel chatModel) {
        this.summaryMetadataEnricher =
                new SummaryMetadataEnricher(
                        //
                        chatModel,
                        //
                        List.of(
                                SummaryMetadataEnricher.SummaryType.PREVIOUS,
                                SummaryMetadataEnricher.SummaryType.CURRENT,
                                SummaryMetadataEnricher.SummaryType.NEXT),
                        //
                        SUMMARY_TEMPLATE,
                        //
                        MetadataMode.ALL);
    }

    @Override
    public List<Document> apply(List<Document> documents) {
        if (DefaultUtils.defaultList(documents).isEmpty()) {
            return List.of();
        }
        return summaryMetadataEnricher.apply(documents);
    }
}
