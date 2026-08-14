package com.lazycece.zsagent.infra.acl.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.DocumentParser;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import com.lazycece.zsagent.domain.knowledge.valueobject.Section;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * HTML 解析器。
 *
 * @author lazycece
 */
@Component
public class HtmlDocumentParser implements DocumentParser {

    @Override
    public ParsedDocument parse(String filePath) throws IOException {
        Document htmlDoc = Jsoup.parse(new File(filePath), "UTF-8");
        htmlDoc.select("script, style, nav, footer, header").remove();

        String bodyText = htmlDoc.body() != null ? htmlDoc.body().text() : htmlDoc.text();
        List<Section> sections = extractSections(htmlDoc);
        String title = StringUtils.isNotBlank(htmlDoc.title())
                ? htmlDoc.title()
                : ParserSupport.extractTitle(bodyText);
        return new ParsedDocument(title, bodyText, sections);
    }

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.HTML;
    }

    private List<Section> extractSections(Document htmlDoc) {
        List<Section> sections = new ArrayList<>();
        if (htmlDoc.body() == null) {
            return sections;
        }
        for (Element element : htmlDoc.body().children()) {
            String tagName = element.tagName();
            int headingLevel = extractHeadingLevel(tagName);
            String text = element.text();
            if (StringUtils.isBlank(text)) {
                continue;
            }
            if (headingLevel > 0) {
                sections.add(new Section(headingLevel, text, "", 0, 0));
            } else if (!sections.isEmpty()) {
                Section last = sections.get(sections.size() - 1);
                sections.set(sections.size() - 1,
                        new Section(last.headingLevel(), last.headingText(),
                                last.content() + text + "\n", 0, last.charOffset()));
            } else {
                sections.add(new Section(0, "", text, 0, 0));
            }
        }
        return sections;
    }

    private int extractHeadingLevel(String tagName) {
        if (tagName == null || !tagName.matches("h[1-6]")) {
            return 0;
        }
        return Integer.parseInt(tagName.substring(1));
    }
}
