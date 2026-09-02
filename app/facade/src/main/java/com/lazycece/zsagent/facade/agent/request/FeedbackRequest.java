/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lazycece.zsagent.facade.agent.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

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
