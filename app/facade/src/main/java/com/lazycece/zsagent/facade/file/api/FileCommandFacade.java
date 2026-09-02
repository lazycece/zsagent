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
package com.lazycece.zsagent.facade.file.api;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.file.request.FileUploadRequest;
import com.lazycece.zsagent.facade.file.result.FileUploadResult;

/**
 * 文件命令门面接口
 *
 * @author lazycece
 */
public interface FileCommandFacade {

    /**
     * 上传文件，落盘存储并返回相对路径。
     *
     * @param request 上传请求
     * @return 上传结果
     */
    RespData<FileUploadResult> upload(FileUploadRequest request);
}
