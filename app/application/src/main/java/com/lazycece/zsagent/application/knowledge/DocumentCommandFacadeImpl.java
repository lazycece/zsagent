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
package com.lazycece.zsagent.application.knowledge;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.application.knowledge.assembler.DocumentAssembler;
import com.lazycece.zsagent.application.knowledge.handler.etl.DocumentEtlOrchestrator;
import com.lazycece.zsagent.application.knowledge.validator.DocumentCreateValidator;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import com.lazycece.zsagent.domain.knowledge.repository.FileStorage;
import com.lazycece.zsagent.domain.knowledge.service.DocumentDomainService;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDocumentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentContentCmd;
import com.lazycece.zsagent.facade.knowledge.api.DocumentCommandFacade;
import com.lazycece.zsagent.facade.knowledge.request.DocumentCreateRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentDeleteRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRestoreRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRollbackRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateContentRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateMetadataRequest;
import com.lazycece.zsagent.facade.knowledge.result.DocumentCreateResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentDeleteResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRestoreResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRollbackResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateContentResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateMetadataResult;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Primary;

/**
 * 文档命令门面实现。 负责文档创建、更新、删除、恢复、回滚的编排：文件路径 → 领域操作 → 触发 ETL。
 * 文件本体已由文件上传接口（FileCommandFacade）预先落盘，此处仅接收相对路径。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class DocumentCommandFacadeImpl implements DocumentCommandFacade {

    private final DocumentDomainService documentService;
    private final FileStorage fileStorage;
    private final DocumentEtlOrchestrator etlOrchestrator;

    public DocumentCommandFacadeImpl(
            DocumentDomainService documentService,
            FileStorage fileStorage,
            DocumentEtlOrchestrator etlOrchestrator) {
        this.documentService = documentService;
        this.fileStorage = fileStorage;
        this.etlOrchestrator = etlOrchestrator;
    }

    @Override
    public RespData<DocumentCreateResult> create(DocumentCreateRequest request) {
        DocumentCreateValidator.validate(request);

        // assemble
        CreateDocumentCmd cmd = DocumentAssembler.assembleCreateDocumentCmd(request);

        cmd.setFileSize(this.getFileSize(request.getFilePath()));

        // create
        String documentId = documentService.createDocument(cmd);

        etlOrchestrator.process(documentId);

        DocumentCreateResult result = new DocumentCreateResult();
        result.setDocumentId(documentId);
        result.setEtlStatus(EtlStatus.PENDING.getCode());
        return RespData.success(result);
    }

    private long getFileSize(String filePath) {
        try (InputStream inputStream = fileStorage.load(filePath)) {
            return inputStream.readAllBytes().length;
        } catch (IOException e) {
            throw ExceptionFactory.businessException("文档读取失败: filePath=" + filePath, e);
        }
    }

    @Override
    public RespData<DocumentUpdateMetadataResult> updateMetadata(
            DocumentUpdateMetadataRequest request) {
        documentService.updateMetadataOptional(
                DocumentAssembler.assembleUpdateMetadataCmd(request));
        return RespData.success(new DocumentUpdateMetadataResult());
    }

    @Override
    public RespData<DocumentUpdateContentResult> updateContent(
            DocumentUpdateContentRequest request) {
        UpdateDocumentContentCmd cmd = DocumentAssembler.assembleUpdateContentCmd(request);
        cmd.setFileSize(this.getFileSize(request.getFilePath()));

        documentService.updateContent(cmd);

        etlOrchestrator.reprocess(request.getDocumentId());
        return RespData.success(new DocumentUpdateContentResult());
    }

    @Override
    public RespData<DocumentDeleteResult> delete(DocumentDeleteRequest request) {
        documentService.delete(request.getUserId(), request.getDocumentId());
        etlOrchestrator.markDeleted(request.getDocumentId());
        return RespData.success(new DocumentDeleteResult());
    }

    @Override
    public RespData<DocumentRestoreResult> restore(DocumentRestoreRequest request) {
        documentService.restore(request.getUserId(), request.getDocumentId());
        etlOrchestrator.markRestored(request.getDocumentId());
        return RespData.success(new DocumentRestoreResult());
    }

    @Override
    public RespData<DocumentRollbackResult> rollback(DocumentRollbackRequest request) {
        documentService.rollback(DocumentAssembler.assembleRollbackCmd(request));
        etlOrchestrator.reprocess(request.getDocumentId());
        return RespData.success(new DocumentRollbackResult());
    }
}
