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
package com.lazycece.zsagent.domain.agent.model;

import com.lazycece.cell.specification.CellHelper;
import com.lazycece.rapidf.domain.anotation.DomainEntity;
import com.lazycece.rapidf.domain.model.Entity;
import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import com.lazycece.zsagent.domain.agent.enums.MessageRole;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import com.lazycece.zsagent.domain.common.enums.CellEnum;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息实体
 *
 * @author lazycece
 */
@Getter
@Setter
@DomainEntity
public class AgentMessage extends Entity<String> {

    /**
     * 消息唯一标识 (UUID)
     */
    private String messageId;
    /**
     * 所属对话ID
     */
    private String conversationId;
    /**
     * 消息角色
     */
    private MessageRole role;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 来源引用（仅 ASSISTANT 角色有值）
     */
    private List<SourceReference> sources = new ArrayList<>();
    /**
     * 反馈状态（null/ USEFUL / NOT_USEFUL）
     */
    private FeedbackType feedback;
    /**
     * 无用反馈原因
     */
    private String feedbackReason;

    @Override
    public String getId() {
        return this.messageId;
    }

    /**
     * 创建消息实体。
     */
    static AgentMessage create(
            String userId,
            String conversationId,
            MessageRole role,
            String content,
            List<SourceReference> sources) {
        AgentMessage message = new AgentMessage();
        message.messageId = CellHelper.getInstance().generateId(CellEnum.AGENT_MESSAGE);
        message.conversationId = conversationId;
        message.role = role;
        message.content = content;
        message.sources = sources != null ? sources : new ArrayList<>();

        message.setCreator(userId);
        message.setUpdater(userId);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        return message;
    }

    // ======================== 行为方法 ========================

    /**
     * 提交反馈。
     *
     * @param type   反馈类型
     * @param reason 反馈原因（仅 NOT_USEFUL 时可能填写）
     */
    public void submitFeedback(String userId, FeedbackType type, String reason) {
        this.feedback = type;
        this.feedbackReason = reason;
        super.setUpdater(userId);
        super.setUpdateTime(LocalDateTime.now());
    }
}
