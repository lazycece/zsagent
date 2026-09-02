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
import java.io.InputStream;
import java.util.List;
import org.springframework.ai.document.Document;

/**
 * 文档解析器接口（领域 SPI）。 由 infrastructure 层提供按格式的多态实现。
 *
 * @author lazycece
 */
public interface DocumentParseHandler {

    /**
     * 支持的文档格式。
     *
     * @return 文档格式
     */
    DocumentFormat supportedFormat();

    /**
     * 解析文档，提取结构化内容。 解析器不负责关闭输入流，由调用方管理其生命周期。
     *
     * @param inputStream 文档内容输入流
     * @return 结构化文档内容
     */
    List<Document> parse(InputStream inputStream);
}
