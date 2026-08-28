package com.lazycece.zsagent.facade.knowledge.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 恢复文档请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentRestoreRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 文档ID */
    @NotBlank(message = "documentId不能为空")
    private String documentId;
}
