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
package com.lazycece.zsagent.adapter.web.file;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.file.api.FileCommandFacade;
import com.lazycece.zsagent.facade.file.request.FileUploadRequest;
import com.lazycece.zsagent.facade.file.result.FileUploadResult;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件命令控制器，直接实现门面接口
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/file")
public class FileCommandController implements FileCommandFacade {

    private final FileCommandFacade fileCommandFacade;

    public FileCommandController(FileCommandFacade fileCommandFacade) {
        this.fileCommandFacade = fileCommandFacade;
    }

    /**
     * 上传文件。
     */
    @Override
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespData<FileUploadResult> upload(@Validated FileUploadRequest request) {
        return fileCommandFacade.upload(request);
    }
}
