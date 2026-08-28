package com.lazycece.zsagent.infra.acl.service.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.acl.DocumentParseHandler;
import java.io.InputStream;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

/**
 * 纯文本解析器。
 *
 * @author lazycece
 */
@Component
public class TxtDocumentParseHandler implements DocumentParseHandler {

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.TXT;
    }

    @Override
    public List<Document> parse(InputStream inputStream) {
        return new TextReader(new InputStreamResource(inputStream)).read();
    }
}
