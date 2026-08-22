package com.lazycece.zsagent.infra.acl.service.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.acl.DocumentParseHandler;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import com.lazycece.zsagent.domain.knowledge.valueobject.Section;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Markdown 解析器（基于 commonmark-java）。
 *
 * @author lazycece
 */
@Component
public class MarkdownDocumentParseHandler implements DocumentParseHandler {

    private static final Parser PARSER = Parser.builder().build();
    private static final TextContentRenderer TEXT_RENDERER = TextContentRenderer.builder().build();

    @Override
    public ParsedDocument parse(InputStream inputStream) throws IOException {
        String markdown = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Node document = PARSER.parse(markdown);
        String fullText = TEXT_RENDERER.render(document);
        List<Section> sections = buildSections(document);
        return new ParsedDocument(ParserSupport.extractTitle(fullText), fullText, sections);
    }

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.MD;
    }

    /**
     * 遍历顶层块节点，按标题构建章节。
     */
    private List<Section> buildSections(Node document) {
        List<Section> sections = new ArrayList<>();
        int charOffset = 0;

        Node child = document.getFirstChild();
        while (child != null) {
            String text = TEXT_RENDERER.render(child);
            if (child instanceof Heading heading) {
                sections.add(new Section(heading.getLevel(), text.trim(), "", 0, charOffset));
            } else if (StringUtils.isNotBlank(text)) {
                if (sections.isEmpty()) {
                    sections.add(new Section(0, "", text + "\n", 0, charOffset));
                } else {
                    Section last = sections.get(sections.size() - 1);
                    sections.set(sections.size() - 1,
                            new Section(last.headingLevel(), last.headingText(),
                                    last.content() + text + "\n", 0, last.charOffset()));
                }
            }
            charOffset += text.length() + 1;
            child = child.getNext();
        }
        return sections;
    }
}
