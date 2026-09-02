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
package com.lazycece.zsagent.application.agent.handler.rag.post;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 文档内容压缩器（Stage 3b）。
 * 截断过长文档，避免 Prompt 超长导致 LLM 截断关键信息。
 * 截断后追加 "..." 标记，保持语义完整性提示。
 *
 * @author lazycece
 */
@Component
@Order(2)
public class DocumentCompressor implements DocumentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(DocumentCompressor.class);

    private static final int MAX_LENGTH = 1500;

    @Override
    public List<Document> process(Query query, List<Document> documents) {
        return documents.stream()
                .map(
                        doc -> {
                            String text = doc.getText();
                            if (text != null && text.length() > MAX_LENGTH) {
                                log.debug("压缩文档: {} -> {} 字符", text.length(), MAX_LENGTH);
                                return Document.builder()
                                        .text(text.substring(0, MAX_LENGTH) + "...")
                                        .metadata(new HashMap<>(doc.getMetadata()))
                                        .build();
                            }
                            return doc;
                        })
                .collect(Collectors.toList());
    }
}
