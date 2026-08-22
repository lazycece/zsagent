package com.lazycece.zsagent.infra.acl.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.handler.parse.DocumentParseHandler;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import com.lazycece.zsagent.domain.knowledge.valueobject.Section;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Word (.docx) 解析器。
 *
 * @author lazycece
 */
@Component
public class WordDocumentParseHandler implements DocumentParseHandler {

    @Override
    public ParsedDocument parse(InputStream inputStream) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(inputStream)) {
            StringBuilder fullText = new StringBuilder();
            List<Section> sections = new ArrayList<>();
            int charOffset = 0;

            for (IBodyElement element : doc.getBodyElements()) {
                if (!(element instanceof XWPFParagraph paragraph)) {
                    continue;
                }
                String text = paragraph.getText();
                if (StringUtils.isBlank(text)) {
                    continue;
                }
                int headingLevel = extractHeadingLevel(paragraph.getStyle());
                if (headingLevel > 0) {
                    sections.add(new Section(headingLevel, text.trim(), "", 0, charOffset));
                } else if (!sections.isEmpty()) {
                    Section last = sections.get(sections.size() - 1);
                    sections.set(sections.size() - 1,
                            new Section(last.headingLevel(), last.headingText(),
                                    last.content() + text + "\n", 0, last.charOffset()));
                } else {
                    sections.add(new Section(0, "", text, 0, charOffset));
                }
                fullText.append(text).append("\n");
                charOffset += text.length() + 1;
            }

            return new ParsedDocument(ParserSupport.extractTitle(fullText.toString()),
                    fullText.toString(), sections);
        }
    }

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.DOCX;
    }

    private int extractHeadingLevel(String style) {
        if (StringUtils.isBlank(style)) {
            return 0;
        }
        String normalized = style.toLowerCase();
        if (!normalized.contains("heading") && !normalized.contains("标题")) {
            return 0;
        }
        for (int i = 1; i <= 6; i++) {
            if (normalized.contains(String.valueOf(i))) {
                return i;
            }
        }
        return 1;
    }
}
