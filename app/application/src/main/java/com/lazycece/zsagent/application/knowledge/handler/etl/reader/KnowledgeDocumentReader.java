package com.lazycece.zsagent.application.knowledge.handler.etl.reader;

import org.springframework.ai.document.DocumentReader;

/**
 * @author lazycece
 */
public interface KnowledgeDocumentReader extends DocumentReader {

    KnowledgeDocumentReader loadDocument(String documentId);
}
