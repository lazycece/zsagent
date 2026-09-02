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
package com.lazycece.zsagent.domain.knowledge.repository;

import com.lazycece.zsagent.domain.knowledge.model.Directory;
import java.util.List;

/**
 * 目录仓储接口
 *
 * @author lazycece
 */
public interface DirectoryRepository {

    /**
     * 新建目录。
     *
     * @param directory 目录聚合根
     */
    void save(Directory directory);

    /**
     * 按 ID 查询目录。
     *
     * @param directoryId 目录ID
     * @return 目录聚合根，不存在时返回 null
     */
    Directory findByDirectoryId(String directoryId);

    /**
     * 查询指定父目录下的所有子目录。
     *
     * @param parentId 父目录ID
     * @return 子目录列表
     */
    List<Directory> findByParentId(String parentId);

    /**
     * 查询所有目录（用于构建目录树）。
     *
     * @return 目录列表
     */
    List<Directory> findAll();

    /**
     * 更新目录。
     *
     * @param directory 目录聚合根
     */
    void update(Directory directory);

    /**
     * 统计子目录数。
     *
     * @param parentId 父目录ID
     * @return 子目录数
     */
    int countByParentId(String parentId);
}
