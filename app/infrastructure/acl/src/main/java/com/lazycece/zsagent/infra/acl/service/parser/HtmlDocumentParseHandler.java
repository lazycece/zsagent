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
package com.lazycece.zsagent.infra.acl.service.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.acl.DocumentParseHandler;
import java.io.InputStream;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

/**
 * HTML 解析器。
 *
 * @author lazycece
 */
@Component
public class HtmlDocumentParseHandler implements DocumentParseHandler {

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.HTML;
    }

    @Override
    public List<Document> parse(InputStream inputStream) {
        JsoupDocumentReaderConfig config =
                JsoupDocumentReaderConfig.builder()
                        // Extract paragraphs within <article> tags
                        .selector("article p")
                        .charset("UTF-8")
                        // Include link URLs in metadata
                        .includeLinkUrls(true)
                        // Extract author and date meta tags
                        .metadataTags(List.of("author", "date"))
                        .build();

        return new JsoupDocumentReader(new InputStreamResource(inputStream), config).read();
    }
}
