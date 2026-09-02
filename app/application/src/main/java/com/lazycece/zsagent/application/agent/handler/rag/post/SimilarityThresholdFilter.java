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
 * 相似度阈值过滤器（Stage 3a）。 Stage 2 用宽松阈值（0.65）召回候选，此处做精确过滤（0.70）， 过滤掉相似度过低的结果，减少后续 LLM 输入的噪声。
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
        List<Document> filtered =
                documents.stream()
                        .filter(doc -> doc.getScore() != null && doc.getScore() >= THRESHOLD)
                        .collect(Collectors.toList());
        log.debug(
                "相似度过滤: {} -> {} 条文档 (threshold={})", documents.size(), filtered.size(), THRESHOLD);
        return filtered;
    }
}
