package com.lazycece.zsagent.facade.agent.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 提问请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class AskQuestionRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 对话归属标识 */
    @NotBlank(message = "conversationId不能为空")
    private String conversationId;

    /** 用户问题，最长2000字符 */
    @NotBlank(message = "question不能为空")
    @Size(max = 2000, message = "question不能超过2000字符")
    private String question;
}
