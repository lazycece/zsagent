package com.lazycece.zsagent.facade.agent.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 反馈请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class FeedbackRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 对话标识 */
    @NotBlank(message = "conversationId不能为空")
    private String conversationId;

    /** 被评价的消息ID */
    @NotBlank(message = "messageId不能为空")
    private String messageId;

    /** 反馈类型：USEFUL / NOT_USEFUL */
    @NotBlank(message = "type不能为空")
    private String type;

    /** 反馈原因（仅 NOT_USEFUL 时可能填写） */
    private String reason;
}
