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
package com.lazycece.zsagent.facade.agent.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息DTO
 *
 * @author lazycece
 */
@Getter
@Setter
public class MessageDTO {

    /** 消息唯一标识 */
    private String messageId;

    /** 消息角色：USER / ASSISTANT / SYSTEM */
    private String role;

    /** 消息内容 */
    private String content;

    /** 来源引用列表 */
    private List<SourceReferenceDTO> sources;

    /** 反馈状态：USEFUL / NOT_USEFUL */
    private String feedback;

    /** 创建时间 */
    private LocalDateTime createTime;
}
