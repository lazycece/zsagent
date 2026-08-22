package com.lazycece.zsagent.infra.acl.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.handler.parse.DocumentParseHandler;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import com.lazycece.zsagent.domain.knowledge.valueobject.Section;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 纯文本解析器。
 *
 * @author lazycece
 */
@Component
public class TxtDocumentParseHandler implements DocumentParseHandler {

    @Override
    public ParsedDocument parse(InputStream inputStream) throws IOException {
        String fullText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(0, "", fullText, 0, 0));
        return new ParsedDocument(ParserSupport.extractTitle(fullText), fullText, sections);
    }

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.TXT;
    }
}
