package com.lazycece.zsagent.application.knowledge.handler.etl.reader;

import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentMetadataKey;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.repository.FileStorage;
import com.lazycece.zsagent.domain.knowledge.service.DocumentDomainService;
import com.lazycece.zsagent.domain.knowledge.service.acl.DocumentParseHandler;
import com.lazycece.zsagent.domain.knowledge.service.acl.DocumentParseHandlerRegistry;
import com.lazycece.zsagent.domain.knowledge.valueobject.EnrichResult;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentMetadataCmd;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识文档读取器（ETL Extract 阶段）。
 * <p>
 * 加载文档文件，通过解析器提取结构化内容，构建一个承载全文与元数据的
 * Spring AI {@link org.springframework.ai.document.Document}。
 * 元数据中的权限 keys（permission_type/permission_depts/permission_users）
 * 会被后续 {@code TokenTextSplitter} 复制到每个分块，供检索侧权限过滤使用。
 *
 * @author lazycece
 */
@ApplicationHandler
public class DefaultKnowledgeDocumentReader implements KnowledgeDocumentReader {

    private Document knowledgeDocument;

    private final DocumentRepository documentRepository;
    private final DocumentDomainService documentDomainService;
    private final FileStorage fileStorage;
    private final DocumentParseHandlerRegistry parserRegistry;
    private final KnowledgeDocumentEnricher knowledgeDocumentEnricher;

    public DefaultKnowledgeDocumentReader(DocumentRepository documentRepository,
                                          DocumentDomainService documentDomainService,
                                          FileStorage fileStorage,
                                          DocumentParseHandlerRegistry parserRegistry,
                                          KnowledgeDocumentEnricher knowledgeDocumentEnricher) {
        this.documentRepository = documentRepository;
        this.documentDomainService = documentDomainService;
        this.fileStorage = fileStorage;
        this.parserRegistry = parserRegistry;
        this.knowledgeDocumentEnricher = knowledgeDocumentEnricher;
    }

    @Override
    public KnowledgeDocumentReader loadDocument(String documentId) {
        Assert.notBlank(documentId, RespStatus.PARAM_ERROR, "参数 documentId 不能为空");
        Document document = documentRepository.findById(documentId);
        Assert.notNull(document, RespStatus.PARAM_ERROR, "文档不存在: documentId={}", documentId);
        this.knowledgeDocument = document;
        return this;
    }

    @Override
    public List<org.springframework.ai.document.Document> get() {
        // check
        Assert.notNull(this.knowledgeDocument, RespStatus.DATA_NOT_EXIST, "文档不存在，请先执行 loadDocument");

        // 文档解析
        DocumentParseHandler parser = parserRegistry.getParser(knowledgeDocument.getFormat());
        ParsedDocument parsedDocument;
        try (InputStream inputStream = fileStorage.load(knowledgeDocument.getFilePath())) {
            parsedDocument = parser.parse(inputStream);
        } catch (IOException e) {
            throw ExceptionFactory.businessException("文档读取失败: documentId=" + knowledgeDocument.getDocumentId(), e);
        }

        // 增强处理文档元数据
        EnrichResult enrichResult = knowledgeDocumentEnricher.enrich(parsedDocument, knowledgeDocument.getTitle());
        this.updateMetadataFromEnrichResult(enrichResult);

        //
        org.springframework.ai.document.Document aiDocument =
                org.springframework.ai.document.Document.builder()
                        .id(knowledgeDocument.getDocumentId())
                        .text(parsedDocument.fullText())
                        .metadata(buildMetadata())
                        .build();
        return List.of(aiDocument);
    }

    /**
     * 从增强结果中提取摘要与标签，持久化到文档元数据。
     */
    private void updateMetadataFromEnrichResult(EnrichResult enrichResult) {
        if (enrichResult == null) {
            return;
        }
        UpdateDocumentMetadataCmd command = new UpdateDocumentMetadataCmd();
        command.setUserId(knowledgeDocument.getUpdater());
        command.setDocumentId(knowledgeDocument.getDocumentId());
        command.setSummary(enrichResult.getSummary());
        command.setTags(enrichResult.getTags());
        documentDomainService.updateMetadataOptional(command);
    }

    /**
     * 构建文档元数据
     */
    private Map<String, Object> buildMetadata() {

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DocumentMetadataKey.DOCUMENT_ID.getCode(), knowledgeDocument.getDocumentId());
        metadata.put(DocumentMetadataKey.TITLE.getCode(), knowledgeDocument.getTitle());
        metadata.put(DocumentMetadataKey.FORMAT.getCode(), DefaultUtils.defaultValueIfNullObj(knowledgeDocument.getFormat(), DocumentFormat::getCode, null));
        metadata.put(DocumentMetadataKey.PERMISSION_TYPE.getCode(), DefaultUtils.defaultValueIfNullObj(knowledgeDocument.getVisibility(), Visibility::getCode, null));
        metadata.put(DocumentMetadataKey.PERMISSION_DEPTS.getCode(), knowledgeDocument.getVisibleTo());
        metadata.put(DocumentMetadataKey.PERMISSION_USERS.getCode(), knowledgeDocument.getVisibleTo());
        metadata.put(DocumentMetadataKey.CURRENT_VERSION.getCode(), knowledgeDocument.getCurrentVersion());

        return metadata;
    }

}
