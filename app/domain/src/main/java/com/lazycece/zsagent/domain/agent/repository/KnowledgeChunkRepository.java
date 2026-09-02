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
package com.lazycece.zsagent.domain.agent.repository;

import com.lazycece.zsagent.domain.agent.model.KnowledgeChunk;
import java.util.List;

/**
 * 知识块仓储接口（检索在 ES 中）
 *
 * @author lazycece
 */
public interface KnowledgeChunkRepository {

    /**
     * 语义检索，返回与查询文本最相关的 topK 个知识块。
     *
     * @param query 查询文本（原始文本，由实现层负责向量化）
     * @param topK  返回数量
     * @return 相关知识块列表
     */
    List<KnowledgeChunk> search(String query, int topK);

    /**
     * 按文档 ID 批量删除知识块（文档更新或删除时调用）。
     *
     * @param documentId 文档ID
     */
    void deleteByDocumentId(String documentId);
}
