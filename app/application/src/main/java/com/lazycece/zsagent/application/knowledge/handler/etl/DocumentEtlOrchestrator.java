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

/**
 * @author lazycece
 */
public interface DocumentEtlOrchestrator {

    /**
     * 处理文档（完整 ETL 流水线）。
     */
    void process(String documentId);

    /**
     * 重新处理文档（删除旧 chunk 后重新 ETL）。
     */
    void reprocess(String documentId);

    /**
     * 标记文档删除（清理 ES chunk）。
     */
    void markDeleted(String documentId);

    /**
     * 标记文档恢复（重新 ETL）。
     */
    void markRestored(String documentId);
}
