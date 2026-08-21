package com.lazycece.zsagent.facade.file.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class FileUploadRequest extends BaseRequest implements Serializable {

    /** 上传文件（multipart 表单绑定，part 名为 file） */
    @NotNull
    private MultipartFile file;
}
