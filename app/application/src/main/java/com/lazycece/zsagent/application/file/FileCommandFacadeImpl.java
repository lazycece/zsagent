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
package com.lazycece.zsagent.application.file;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.zsagent.domain.common.utils.FileUtils;
import com.lazycece.zsagent.domain.knowledge.repository.FileStorage;
import com.lazycece.zsagent.facade.file.api.FileCommandFacade;
import com.lazycece.zsagent.facade.file.request.FileUploadRequest;
import com.lazycece.zsagent.facade.file.result.FileUploadResult;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Primary;

/**
 * 文件命令门面实现。 负责文件上传编排：参数校验、文件命名、日期目录路径构建、落盘存储。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class FileCommandFacadeImpl implements FileCommandFacade {

    private final FileStorage fileStorage;

    public FileCommandFacadeImpl(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Override
    public RespData<FileUploadResult> upload(FileUploadRequest request) {
        Assert.notNull(request, RespStatus.PARAM_ERROR, "上传请求不能为空");
        Assert.notNull(request.getFile(), RespStatus.PARAM_ERROR, "文件不能为空");
        Assert.isTrue(!request.getFile().isEmpty(), RespStatus.PARAM_ERROR, "文件内容不能为空");

        String filePath =
                FileUtils.assembleFilePath(
                        LocalDateTime.now(), request.getFile().getOriginalFilename());
        try (InputStream inputStream = request.getFile().getInputStream()) {
            fileStorage.store(filePath, inputStream);
        } catch (IOException e) {
            throw ExceptionFactory.businessException("文件存储失败", e);
        }

        FileUploadResult result = new FileUploadResult();
        result.setFilePath(filePath);
        return RespData.success(result);
    }
}
