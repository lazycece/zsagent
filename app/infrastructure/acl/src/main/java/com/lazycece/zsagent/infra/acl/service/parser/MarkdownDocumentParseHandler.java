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
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

/**
 * @author lazycece
 */
@Component
public class MarkdownDocumentParseHandler implements DocumentParseHandler {

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.MD;
    }

    @Override
    public List<Document> parse(InputStream inputStream) {
        MarkdownDocumentReaderConfig config =
                MarkdownDocumentReaderConfig.builder()
                        // 遇到水平分割线 --- 时，将其视为一个新文档的开始（即分页/分段）
                        // 设置为 true 意味着每个分割线后的内容会被当作独立文档处理。
                        .withHorizontalRuleCreateDocument(true)
                        // 不包含代码块。解析时会忽略Markdown中的代码块不会将其内容纳入最终结果
                        .withIncludeCodeBlock(true)
                        // 不包含引用块。解析时会忽略 > 开头的引用内容
                        .withIncludeBlockquote(true)
                        .build();

        return new MarkdownDocumentReader(new InputStreamResource(inputStream), config).read();
    }
}
