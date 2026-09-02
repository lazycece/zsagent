/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lazycece.zsagent.application.knowledge.handler.etl;

import com.google.common.collect.Lists;
import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.zsagent.application.knowledge.handler.etl.reader.KnowledgeDocumentReader;
import com.lazycece.zsagent.application.knowledge.handler.etl.transformer.ChunkKeywordMetadataEnricher;
import com.lazycece.zsagent.application.knowledge.handler.etl.transformer.ChunkSummaryMetadataEnricher;
import com.lazycece.zsagent.application.knowledge.handler.etl.transformer.DocumentTokenTextSplitter;
import com.lazycece.zsagent.domain.agent.repository.KnowledgeChunkRepository;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import com.lazycece.zsagent.domain.knowledge.service.DocumentDomainService;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateEtlStatusCmd;
import com.lazycece.zsagent.infra.acl.config.EmbeddingConfig;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Async;

/**
 * 文档 ETL 异步编排器。
 * <p>
 * 基于 Spring AI ETL pipeline（DocumentReader / DocumentTransformer / DocumentWriter）编排： 解析（Extract）→
 * 增强（Transform）→ 分块（Transform）→ 向量化索引（Load）→ 发布。
 *
 * @author lazycece
 */
@ApplicationHandler
public class DefaultDocumentEtlOrchestrator implements DocumentEtlOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(DefaultDocumentEtlOrchestrator.class);

    private final DocumentDomainService documentDomainService;
    private final KnowledgeDocumentReader knowledgeDocumentReader;
    private final DocumentTokenTextSplitter tokenTextSplitter;
    private final ChunkSummaryMetadataEnricher summaryMetadataEnricher;
    private final ChunkKeywordMetadataEnricher keywordMetadataEnricher;
    private final KnowledgeChunkRepository knowledgeChunkRepository;
    private final VectorStore vectorStore;
    private final EmbeddingConfig embeddingConfig;

    public DefaultDocumentEtlOrchestrator(
            DocumentDomainService documentDomainService,
            KnowledgeDocumentReader knowledgeDocumentReader,
            DocumentTokenTextSplitter tokenTextSplitter,
            ChunkSummaryMetadataEnricher summaryMetadataEnricher,
            ChunkKeywordMetadataEnricher keywordMetadataEnricher,
            KnowledgeChunkRepository knowledgeChunkRepository,
            VectorStore vectorStore,
            EmbeddingConfig embeddingConfig) {
        this.documentDomainService = documentDomainService;
        this.knowledgeDocumentReader = knowledgeDocumentReader;
        this.tokenTextSplitter = tokenTextSplitter;
        this.summaryMetadataEnricher = summaryMetadataEnricher;
        this.keywordMetadataEnricher = keywordMetadataEnricher;
        this.knowledgeChunkRepository = knowledgeChunkRepository;
        this.vectorStore = vectorStore;
        this.embeddingConfig = embeddingConfig;
    }

    @Override
    @Async("etlTaskExecutor")
    public void process(String documentId) {
        try {
            // todo 先临时走异步，后续可考虑走MQ

            // 1、文档解析
            documentDomainService.updateEtlStatus(
                    UpdateEtlStatusCmd.build(documentId, EtlStatus.PARSING, null));
            List<Document> docs = knowledgeDocumentReader.read(documentId);

            log.info("ETL 处理: 文档解析完成, documentId={}, docs数={}", documentId, docs.size());

            // 2、文档chunk，复制源 metadata 到每个分块
            documentDomainService.updateEtlStatus(
                    UpdateEtlStatusCmd.build(documentId, EtlStatus.CHUNKING, null));
            docs = tokenTextSplitter.transform(docs);

            log.info("ETL 处理: 文档chunk完成, documentId={}, chunk数={}", documentId, docs.size());

            // 3、chunk增强，生成摘要和关键词标签
            documentDomainService.updateEtlStatus(
                    UpdateEtlStatusCmd.build(documentId, EtlStatus.ENRICHING, null));
            docs = summaryMetadataEnricher.andThen(keywordMetadataEnricher).apply(docs);

            log.info(
                    "ETL 处理: chunk enricher 完成, documentId={}, chunk数={}", documentId, docs.size());

            // 4、索引写入，VectorStore 自动计算 embedding 并写入
            documentDomainService.updateEtlStatus(
                    UpdateEtlStatusCmd.build(documentId, EtlStatus.INDEXING, null));
            this.doVectorStore(docs);

            log.info("ETL 处理: 向量存储完成, documentId={}, chunk数={}", documentId, docs.size());

            // Phase 5: 发布
            documentDomainService.publish(documentId);
            log.info("ETL 处理: 流程处理完毕, documentId={}, chunk数={}", documentId, docs.size());

        } catch (Exception e) {
            log.error("ETL 处理失败: documentId={}", documentId, e);
            documentDomainService.updateEtlStatus(
                    UpdateEtlStatusCmd.build(
                            documentId,
                            EtlStatus.FAILED,
                            DefaultUtils.defaultValue(e.getMessage(), "未知错误")));
        }
    }

    private void doVectorStore(List<Document> docs) {
        Lists.partition(DefaultUtils.defaultList(docs), embeddingConfig.getBathNum())
                .forEach(vectorStore::add);
    }

    @Override
    @Async("etlTaskExecutor")
    public void reprocess(String documentId) {
        knowledgeChunkRepository.deleteByDocumentId(documentId);
        process(documentId);
    }

    @Override
    @Async("etlTaskExecutor")
    public void markDeleted(String documentId) {
        knowledgeChunkRepository.deleteByDocumentId(documentId);
    }

    @Override
    @Async("etlTaskExecutor")
    public void markRestored(String documentId) {
        process(documentId);
    }
}
