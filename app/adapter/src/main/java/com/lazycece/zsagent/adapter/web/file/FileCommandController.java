package com.lazycece.zsagent.adapter.web.file;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.file.api.FileCommandFacade;
import com.lazycece.zsagent.facade.file.request.FileUploadRequest;
import com.lazycece.zsagent.facade.file.result.FileUploadResult;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public RespData<FileUploadResult> upload(@Validated @RequestBody FileUploadRequest request) {
        return fileCommandFacade.upload(request);
    }
}
