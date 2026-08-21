package com.lazycece.zsagent.application.knowledge.etl;

import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.zsagent.domain.agent.model.KnowledgeChunk;
import com.lazycece.zsagent.domain.agent.repository.KnowledgeChunkRepository;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.repository.FileStorage;
import com.lazycece.zsagent.domain.knowledge.service.DocumentDomainService;
import com.lazycece.zsagent.domain.knowledge.service.DocumentParser;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentMetadataCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateEtlStatusCmd;
import com.lazycece.zsagent.infra.acl.parser.ParserRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档 ETL 异步编排器。
 * 负责文档的解析、分块、增强、向量化、索引与发布。
 *
 * @author lazycece
 */
@Component
public class DocumentEtlOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(DocumentEtlOrchestrator.class);

    private final DocumentDomainService documentService;
    private final DocumentRepository documentRepository;
    private final FileStorage fileStorage;
    private final ParserRegistry parserRegistry;
    private final DocumentChunker chunker;
    private final DocumentEnricher enricher;
    private final EmbeddingModel embeddingModel;
    private final KnowledgeChunkRepository chunkRepository;

    public DocumentEtlOrchestrator(DocumentDomainService documentService,
                                   DocumentRepository documentRepository,
                                   FileStorage fileStorage,
                                   ParserRegistry parserRegistry,
                                   DocumentChunker chunker,
                                   DocumentEnricher enricher,
                                   EmbeddingModel embeddingModel,
                                   KnowledgeChunkRepository chunkRepository) {
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.fileStorage = fileStorage;
        this.parserRegistry = parserRegistry;
        this.chunker = chunker;
        this.enricher = enricher;
        this.embeddingModel = embeddingModel;
        this.chunkRepository = chunkRepository;
    }

    /**
     * 处理文档（完整 ETL 流水线）。
     */
    @Async("etlTaskExecutor")
    public void process(String documentId) {
        try {
            Document document = documentRepository.findById(documentId);
            Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在: {}", documentId);

            // Phase 1: 解析
            updateEtlStatus(documentId, EtlStatus.PARSING, null);
            DocumentParser parser = parserRegistry.getParser(document.getFormat());
            ParsedDocument parsed;
            try (InputStream inputStream = fileStorage.load(document.getFilePath())) {
                parsed = parser.parse(inputStream);
            }

            // Phase 2: 分块
            updateEtlStatus(documentId, EtlStatus.CHUNKING, null);
            List<KnowledgeChunk> chunks = chunker.chunk(parsed, document);

            // Phase 3: 增强（LLM 摘要/标签）
            updateEtlStatus(documentId, EtlStatus.ENRICHING, null);
            com.lazycece.zsagent.domain.knowledge.valueobject.EnrichResult enrichResult = enricher.enrich(parsed, document.getTitle());
            UpdateDocumentMetadataCmd metadataCommand = new UpdateDocumentMetadataCmd();
            metadataCommand.setUserId(document.getCreator());
            metadataCommand.setDocumentId(documentId);
            metadataCommand.setSummary(enrichResult.getSummary());
            metadataCommand.setTags(enrichResult.getTags());
            documentService.updateMetadata(metadataCommand);

            // Phase 4: 向量化
            updateEtlStatus(documentId, EtlStatus.EMBEDDING, null);
            embedChunks(chunks);

            // Phase 5: 索引
            updateEtlStatus(documentId, EtlStatus.INDEXING, null);
            chunkRepository.index(chunks);

            // Phase 6: 发布
            documentService.publish(documentId);
            log.info("ETL 处理完成: documentId={}, chunk数={}", documentId, chunks.size());

        } catch (Exception e) {
            log.error("ETL 处理失败: documentId={}", documentId, e);
            updateEtlStatus(documentId, EtlStatus.FAILED,
                    e.getMessage() != null ? e.getMessage() : "未知错误");
        }
    }

    /**
     * 重新处理文档（删除旧 chunk 后重新 ETL）。
     */
    @Async("etlTaskExecutor")
    public void reprocess(String documentId) {
        chunkRepository.deleteByDocumentId(documentId);
        process(documentId);
    }

    /**
     * 标记文档删除（清理 ES chunk）。
     */
    @Async("etlTaskExecutor")
    public void markDeleted(String documentId) {
        chunkRepository.deleteByDocumentId(documentId);
    }

    /**
     * 标记文档恢复（重新 ETL）。
     */
    @Async("etlTaskExecutor")
    public void markRestored(String documentId) {
        process(documentId);
    }

    /**
     * 批量向量化。
     */
    private void embedChunks(List<KnowledgeChunk> chunks) {
        if (chunks.isEmpty()) {
            return;
        }
        List<String> texts = chunks.stream()
                .map(KnowledgeChunk::getContent)
                .collect(Collectors.toList());
        List<float[]> embeddings = embeddingModel.embed(texts);
        for (int i = 0; i < chunks.size(); i++) {
            chunks.get(i).setEmbedding(embeddings.get(i));
        }
    }

    /**
     * 更新 ETL 状态。
     */
    private void updateEtlStatus(String documentId, EtlStatus status, String errorMessage) {
        UpdateEtlStatusCmd command = new UpdateEtlStatusCmd();
        command.setDocumentId(documentId);
        command.setStatus(status);
        command.setErrorMessage(errorMessage);
        documentService.updateEtlStatus(command);
    }
}
