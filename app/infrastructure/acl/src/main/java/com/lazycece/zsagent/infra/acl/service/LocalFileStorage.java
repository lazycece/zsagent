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
package com.lazycece.zsagent.infra.acl.service;

import com.lazycece.zsagent.domain.knowledge.repository.FileStorage;
import com.lazycece.zsagent.infra.acl.config.StorageConfig;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Component;

/**
 * 本地文件存储实现。
 *
 * @author lazycece
 */
@Component
public class LocalFileStorage implements FileStorage {

    private final StorageConfig storageConfig;

    public LocalFileStorage(StorageConfig storageConfig) {
        this.storageConfig = storageConfig;
    }

    @Override
    public long store(String path, InputStream inputStream) throws IOException {
        Path fullPath = Path.of(storageConfig.getLocalFileBaseDir(), path);
        Files.createDirectories(fullPath.getParent());
        return Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public InputStream load(String path) throws IOException {
        Path fullPath = Path.of(storageConfig.getLocalFileBaseDir(), path);
        if (!Files.exists(fullPath)) {
            throw new IOException("文件不存在: " + path);
        }
        return Files.newInputStream(fullPath);
    }

    @Override
    public boolean delete(String path) {
        try {
            return Files.deleteIfExists(Path.of(storageConfig.getLocalFileBaseDir(), path));
        } catch (IOException e) {
            return false;
        }
    }
}
