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

import com.lazycece.zsagent.domain.knowledge.model.DocumentVersion;
import java.util.List;

/**
 * 文档版本仓储接口
 *
 * @author lazycece
 */
public interface DocumentVersionRepository {

    /**
     * 批量保存版本记录。
     *
     * @param versions 待保存的版本列表
     */
    void save(List<DocumentVersion> versions);

    /**
     * 查询文档的所有版本。
     *
     * @param documentId 文档ID
     * @return 版本列表（按版本号倒序）
     */
    List<DocumentVersion> findByDocumentId(String documentId);

    /**
     * 查询单个版本。
     *
     * @param versionId 版本ID
     * @return 版本实体，不存在时返回 null
     */
    DocumentVersion findByVersionId(String versionId);
}
