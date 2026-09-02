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
package com.lazycece.zsagent.domain.knowledge.service.acl;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;

/**
 * 文档解析器注册中心（领域 SPI）。
 * 按文档格式路由到对应解析器，由 infrastructure 层提供实现。
 *
 * @author lazycece
 */
public interface DocumentParseHandlerRegistry {

    /**
     * 按格式获取解析器。
     *
     * @param format 文档格式
     * @return 对应解析器
     */
    DocumentParseHandler getParser(DocumentFormat format);
}
