package com.lazycece.zsagent.infra.acl.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.DocumentParser;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import com.lazycece.zsagent.domain.knowledge.valueobject.Section;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Markdown 解析器。
 *
 * @author lazycece
 */
@Component
public class MarkdownDocumentParser implements DocumentParser {

    @Override
    public ParsedDocument parse(String filePath) throws IOException {
        String rawText = Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
        StringBuilder fullText = new StringBuilder();
        List<Section> sections = new ArrayList<>();
        int charOffset = 0;

        for (String line : rawText.split("\n")) {
            int headingLevel = extractHeadingLevel(line);
            if (headingLevel > 0) {
                String headingText = line.trim().replaceFirst("^#{1,6}\\s*", "").trim();
                sections.add(new Section(headingLevel, headingText, "", 0, charOffset));
            } else if (!sections.isEmpty()) {
                Section last = sections.get(sections.size() - 1);
                sections.set(sections.size() - 1,
                        new Section(last.headingLevel(), last.headingText(),
                                last.content() + line + "\n", 0, last.charOffset()));
            }
            fullText.append(line).append("\n");
            charOffset += line.length() + 1;
        }

        return new ParsedDocument(ParserSupport.extractTitle(fullText.toString()),
                fullText.toString(), sections);
    }

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.MD;
    }

    private int extractHeadingLevel(String line) {
        if (StringUtils.isBlank(line)) {
            return 0;
        }
        String trimmed = line.trim();
        int level = 0;
        while (level < 6 && level < trimmed.length() && trimmed.charAt(level) == '#') {
            level++;
        }
        if (level > 0 && (level == trimmed.length() || trimmed.charAt(level) == ' ')) {
            return level;
        }
        return 0;
    }
}
