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
package com.lazycece.zsagent.infra.dal.po;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * agent_message 表 PO
 *
 * @author lazycece
 */
@Getter
@Setter
public class AgentMessagePO {

    /** 主键 */
    private Long id;
    /** 消息唯一标识 */
    private String messageId;
    /** 所属对话ID */
    private String conversationId;
    /** 角色: USER / ASSISTANT / SYSTEM */
    private String role;
    /** 消息内容 */
    private String content;
    /** 来源引用 JSON */
    private String sources;
    /** 反馈: USEFUL / NOT_USEFUL */
    private String feedback;
    /** 反馈原因 */
    private String feedbackReason;
    /** 创建人 */
    private String creator;
    /** 更新人 */
    private String updater;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 逻辑删除 */
    private Boolean deleted;
}
