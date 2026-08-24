package com.lazycece.zsagent.application.knowledge.handler.etl.reader;

import java.util.List;
import org.springframework.ai.document.Document;

/**
 * @author lazycece
 */
public interface KnowledgeDocumentReader {

    List<Document> read(String documentId);
}
