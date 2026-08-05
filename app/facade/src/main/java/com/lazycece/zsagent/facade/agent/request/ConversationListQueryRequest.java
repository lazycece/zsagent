package com.lazycece.zsagent.facade.agent.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 对话列表查询请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class ConversationListQueryRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 页码，从1开始 */
    @NotNull(message = "page不能为null")
    @Min(value = 1, message = "page必须大于0")
    private Integer page = 1;

    /** 每页大小 */
    @NotNull(message = "size不能为null")
    @Min(value = 1, message = "size必须大于0")
    private Integer size = 20;
}
