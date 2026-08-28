package com.lazycece.zsagent.facade.knowledge.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 更新文档内容请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentUpdateContentRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 文档ID */
    @NotBlank(message = "documentId不能为空")
    private String documentId;

    /** 变更说明（可空） */
    private String changeLog;

    /** 新文件相对路径（先经文件上传接口获取） */
    @NotBlank(message = "filePath不能为空")
    private String filePath;
}
