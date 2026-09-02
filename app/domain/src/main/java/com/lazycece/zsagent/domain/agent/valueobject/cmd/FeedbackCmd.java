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
package com.lazycece.zsagent.domain.agent.valueobject.cmd;

import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import lombok.Getter;
import lombok.Setter;

/**
 * 记录用户反馈命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class FeedbackCmd {

    /** 用户ID */
    private String userId;
    /** 对话ID */
    private String conversationId;
    /** 被评价的消息ID */
    private String messageId;
    /** 反馈类型 */
    private FeedbackType type;
    /** 反馈原因（仅 NOT_USEFUL 时可能填写） */
    private String reason;
}
