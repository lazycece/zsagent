package com.lazycece.zsagent.infra.acl.service.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.acl.DocumentParseHandler;
import java.io.InputStream;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

/**
 * Word (.docx) 解析器。
 *
 * @author lazycece
 */
@Component
public class DocxDocumentParseHandler implements DocumentParseHandler {


    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.DOCX;
    }

    @Override
    public List<Document> parse(InputStream inputStream) {
        ExtractedTextFormatter formatter = ExtractedTextFormatter.builder()
                .withNumberOfTopTextLinesToDelete(0).build();
        TikaDocumentReader reader = new TikaDocumentReader(new InputStreamResource(inputStream),
                formatter);
        return reader.read();
    }

}
