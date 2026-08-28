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
