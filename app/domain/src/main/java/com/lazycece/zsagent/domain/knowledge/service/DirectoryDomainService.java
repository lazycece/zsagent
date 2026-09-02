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
package com.lazycece.zsagent.domain.knowledge.service;

import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDirectoryCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.MoveDirectoryCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.RenameDirectoryCmd;

/**
 * 目录领域服务接口
 *
 * @author lazycece
 */
public interface DirectoryDomainService {

    /**
     * 创建目录。
     *
     * @param command 创建目录命令
     * @return directoryId
     */
    String createDirectory(CreateDirectoryCmd command);

    /**
     * 重命名目录。
     *
     * @param command 重命名目录命令
     */
    void rename(RenameDirectoryCmd command);

    /**
     * 移动目录（改变父级）。
     *
     * @param command 移动目录命令
     */
    void moveTo(MoveDirectoryCmd command);

    /**
     * 删除目录（需先检查无子目录、无关连文档）。
     *
     * @param command 删除目录命令
     */
    void delete(String userId, String directoryId);
}
