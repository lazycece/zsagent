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
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.application.knowledge.assembler.DirectoryAssembler;
import com.lazycece.zsagent.domain.knowledge.service.DirectoryDomainService;
import com.lazycece.zsagent.facade.knowledge.api.DirectoryCommandFacade;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryCreateRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryDeleteRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryMoveRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryRenameRequest;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryCreateResult;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryDeleteResult;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryMoveResult;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryRenameResult;
import org.springframework.context.annotation.Primary;

/**
 * 目录命令门面实现。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class DirectoryCommandFacadeImpl implements DirectoryCommandFacade {

    private final DirectoryDomainService directoryService;

    public DirectoryCommandFacadeImpl(DirectoryDomainService directoryService) {
        this.directoryService = directoryService;
    }

    @Override
    public RespData<DirectoryCreateResult> create(DirectoryCreateRequest request) {
        String directoryId =
                directoryService.createDirectory(
                        DirectoryAssembler.assembleCreateDirectoryCmd(request));
        DirectoryCreateResult result = new DirectoryCreateResult();
        result.setDirectoryId(directoryId);
        return RespData.success(result);
    }

    @Override
    public RespData<DirectoryRenameResult> rename(DirectoryRenameRequest request) {
        directoryService.rename(DirectoryAssembler.assembleRenameDirectoryCmd(request));
        return RespData.success(new DirectoryRenameResult());
    }

    @Override
    public RespData<DirectoryMoveResult> move(DirectoryMoveRequest request) {
        directoryService.moveTo(DirectoryAssembler.assembleMoveDirectoryCmd(request));
        return RespData.success(new DirectoryMoveResult());
    }

    @Override
    public RespData<DirectoryDeleteResult> delete(DirectoryDeleteRequest request) {
        directoryService.delete(request.getUserId(), request.getDirectoryId());
        return RespData.success(new DirectoryDeleteResult());
    }
}
