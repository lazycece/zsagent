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
        ExtractedTextFormatter formatter = ExtractedTextFormatter.builder()
                .withNumberOfTopTextLinesToDelete(0)
                .build();
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageExtractedTextFormatter(formatter)
                // 每1页作为一个独立文档处理
                .withPagesPerDocument(1)
                .build();
        return new PagePdfDocumentReader(new InputStreamResource(inputStream), config).read();
    }


}
