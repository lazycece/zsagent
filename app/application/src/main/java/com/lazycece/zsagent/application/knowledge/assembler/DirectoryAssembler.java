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
package com.lazycece.zsagent.application.knowledge.assembler;

import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDirectoryCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.MoveDirectoryCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.RenameDirectoryCmd;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryCreateRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryMoveRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryRenameRequest;

/**
 * 目录组装器。
 * 负责从请求体构建领域命令对象，命令对象构建逻辑统一收拢于此。
 *
 * @author lazycece
 */
public final class DirectoryAssembler {

    private DirectoryAssembler() {}

    /**
     * 从创建请求构建创建目录命令。
     */
    public static CreateDirectoryCmd assembleCreateDirectoryCmd(DirectoryCreateRequest request) {
        CreateDirectoryCmd command = new CreateDirectoryCmd();
        command.setUserId(request.getUserId());
        command.setParentId(request.getParentId());
        command.setName(request.getName());
        return command;
    }

    /**
     * 从重命名请求构建重命名目录命令。
     */
    public static RenameDirectoryCmd assembleRenameDirectoryCmd(DirectoryRenameRequest request) {
        RenameDirectoryCmd command = new RenameDirectoryCmd();
        command.setUserId(request.getUserId());
        command.setDirectoryId(request.getDirectoryId());
        command.setNewName(request.getNewName());
        return command;
    }

    /**
     * 从移动请求构建移动目录命令。
     */
    public static MoveDirectoryCmd assembleMoveDirectoryCmd(DirectoryMoveRequest request) {
        MoveDirectoryCmd command = new MoveDirectoryCmd();
        command.setUserId(request.getUserId());
        command.setDirectoryId(request.getDirectoryId());
        command.setNewParentId(request.getNewParentId());
        return command;
    }
}
