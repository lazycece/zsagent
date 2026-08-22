package com.lazycece.zsagent.infra.acl.service.parser;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.handler.parse.DocumentParseHandler;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import com.lazycece.zsagent.domain.knowledge.valueobject.Section;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PDF 解析器。
 *
 * @author lazycece
 */
@Component
public class PdfDocumentParseHandler implements DocumentParseHandler {

    @Override
    public ParsedDocument parse(InputStream inputStream) throws IOException {
        try (PDDocument pdfDoc = Loader.loadPDF(inputStream.readAllBytes())) {
            StringBuilder fullText = new StringBuilder();
            List<Section> sections = new ArrayList<>();
            PDFTextStripper stripper = new PDFTextStripper();

            int pageCount = pdfDoc.getNumberOfPages();
            for (int page = 0; page < pageCount; page++) {
                stripper.setStartPage(page + 1);
                stripper.setEndPage(page + 1);
                String pageText = stripper.getText(pdfDoc);
                sections.add(new Section(0, "", pageText, page + 1, fullText.length()));
                fullText.append(pageText).append("\n");
            }

            return new ParsedDocument(ParserSupport.extractTitle(fullText.toString()),
                    fullText.toString(), sections);
        }
    }

    @Override
    public DocumentFormat supportedFormat() {
        return DocumentFormat.PDF;
    }
}
