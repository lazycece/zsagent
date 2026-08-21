package com.lazycece.zsagent.facade.file.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文件上传结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class FileUploadResult implements Serializable {

    /** 文件相对路径 */
    private String filePath;

}
