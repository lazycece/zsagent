package com.lazycece.zsagent.facade.agent.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 对话查询请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class ConversationQueryRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 对话ID */
    @NotBlank(message = "conversationId不能为空")
    private String conversationId;
}
