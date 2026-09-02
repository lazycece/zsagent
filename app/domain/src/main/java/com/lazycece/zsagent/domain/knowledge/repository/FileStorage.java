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

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件存储接口（领域 SPI）。
 * domain 定义"我需要存取文件"，由 infrastructure 提供具体实现。
 *
 * @author lazycece
 */
public interface FileStorage {

    /**
     * 保存文件。
     *
     * @param path        文件路径（key）
     * @param inputStream 文件输入流
     * @return 文件大小（字节）
     * @throws IOException IO 异常
     */
    long store(String path, InputStream inputStream) throws IOException;

    /**
     * 读取文件。
     *
     * @param path 文件路径（key）
     * @return 文件输入流，调用方负责关闭
     * @throws IOException IO 异常
     */
    InputStream load(String path) throws IOException;

    /**
     * 删除文件。
     *
     * @param path 文件路径（key）
     * @return 是否删除成功
     */
    boolean delete(String path);
}
