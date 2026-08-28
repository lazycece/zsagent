package com.lazycece.zsagent.infra.acl.service.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.acl.DocumentParseHandler;
import java.io.InputStream;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

/**
 * HTML 解析器。
 *
 * @author lazycece
 */
@Component
public class HtmlDocumentParseHandler implements DocumentParseHandler {

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.HTML;
    }

    @Override
    public List<Document> parse(InputStream inputStream) {
        JsoupDocumentReaderConfig config = JsoupDocumentReaderConfig.builder()
                // Extract paragraphs within <article> tags
                .selector("article p").charset("UTF-8")
                // Include link URLs in metadata
                .includeLinkUrls(true)
                // Extract author and date meta tags
                .metadataTags(List.of("author", "date")).build();

        return new JsoupDocumentReader(new InputStreamResource(inputStream), config).read();
    }
}
