package com.lazycece.zsagent.facade.knowledge.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文档回滚请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentRollbackRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 文档ID */
    @NotBlank(message = "documentId不能为空")
    private String documentId;

    /** 目标版本ID */
    @NotBlank(message = "targetVersionId不能为空")
    private String targetVersionId;
}
