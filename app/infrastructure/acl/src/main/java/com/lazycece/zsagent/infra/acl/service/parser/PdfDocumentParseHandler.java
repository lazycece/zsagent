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
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

/**
 * PDF 解析器。
 *
 * @author lazycece
 */
@Component
public class PdfDocumentParseHandler implements DocumentParseHandler {

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.PDF;
    }

    @Override
    public List<Document> parse(InputStream inputStream) {
        ExtractedTextFormatter formatter =
                ExtractedTextFormatter.builder().withNumberOfTopTextLinesToDelete(0).build();
        PdfDocumentReaderConfig config =
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(formatter)
                        // 每1页作为一个独立文档处理
                        .withPagesPerDocument(1)
                        .build();
        return new PagePdfDocumentReader(new InputStreamResource(inputStream), config).read();
    }
}
