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
package com.lazycece.zsagent.adapter.web.knowledge;

import com.lazycece.rapidf.restful.response.RespData;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档命令控制器，直接实现门面接口，仅负责请求转发。
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/document")
public class DocumentCommandController implements DocumentCommandFacade {

    private final DocumentCommandFacade documentCommandFacade;

    public DocumentCommandController(DocumentCommandFacade documentCommandFacade) {
        this.documentCommandFacade = documentCommandFacade;
    }

    /**
     * 创建文档（文件需先经文件上传接口获取相对路径）。
     */
    @Override
    @PostMapping("/create")
    public RespData<DocumentCreateResult> create(
            @Validated @RequestBody DocumentCreateRequest request) {
        return documentCommandFacade.create(request);
    }

    /**
     * 更新文档元数据（不产生新版本）。
     */
    @Override
    @PostMapping("/update-metadata")
    public RespData<DocumentUpdateMetadataResult> updateMetadata(
            @Validated @RequestBody DocumentUpdateMetadataRequest request) {
        return documentCommandFacade.updateMetadata(request);
    }

    /**
     * 更新文档内容（产生新版本并触发 ETL）。
     */
    @Override
    @PostMapping("/update-content")
    public RespData<DocumentUpdateContentResult> updateContent(
            @Validated @RequestBody DocumentUpdateContentRequest request) {
        return documentCommandFacade.updateContent(request);
    }

    /**
     * 删除文档（移入回收站）。
     */
    @Override
    @PostMapping("/delete")
    public RespData<DocumentDeleteResult> delete(
            @Validated @RequestBody DocumentDeleteRequest request) {
        return documentCommandFacade.delete(request);
    }

    /**
     * 恢复文档。
     */
    @Override
    @PostMapping("/restore")
    public RespData<DocumentRestoreResult> restore(
            @Validated @RequestBody DocumentRestoreRequest request) {
        return documentCommandFacade.restore(request);
    }

    /**
     * 回滚到指定历史版本。
     */
    @Override
    @PostMapping("/rollback")
    public RespData<DocumentRollbackResult> rollback(
            @Validated @RequestBody DocumentRollbackRequest request) {
        return documentCommandFacade.rollback(request);
    }
}
