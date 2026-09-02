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
package com.lazycece.zsagent.domain.agent.service.impl;

import com.lazycece.rapidf.domain.anotation.DomainService;
import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.zsagent.domain.agent.model.AgentConversation;
import com.lazycece.zsagent.domain.agent.repository.AgentConversationRepository;
import com.lazycece.zsagent.domain.agent.service.ConversationDomainService;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.AssistantMessageCmd;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.FeedbackCmd;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.UserMessageCmd;

/**
 * 对话领域服务实现。 领域服务仅做编排：参数校验 → 获取聚合 → 委托聚合行为 → 持久化。 业务规则封装在聚合根内部。
 *
 * @author lazycece
 */
@DomainService
public class ConversationDomainServiceImpl implements ConversationDomainService {

    private final AgentConversationRepository conversationRepository;

    public ConversationDomainServiceImpl(AgentConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    /**
     * 记录用户提问消息。 首次对话时通过工厂方法创建聚合根，已有对话时仅更新操作者信息。
     */
    @Override
    public void recordUserMessage(UserMessageCmd command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        String userId = command.getUserId();
        String conversationId = command.getConversationId();
        String content = command.getContent();
        Assert.notNull(userId, RespStatus.PARAM_ERROR, "userId 不能为 null");
        Assert.notNull(conversationId, RespStatus.PARAM_ERROR, "conversationId 不能为 null");
        Assert.notNull(content, RespStatus.PARAM_ERROR, "content 不能为 null");

        AgentConversation conversation =
                conversationRepository.findByConversationId(userId, conversationId);

        if (conversation == null) {
            conversation = AgentConversation.create(userId, conversationId);
            conversation.askQuestion(userId, content);
            conversationRepository.save(conversation);
        } else {
            conversation.askQuestion(userId, content);
            conversationRepository.update(conversation);
        }
    }

    /**
     * 记录助手回答消息。
     */
    @Override
    public void recordAssistantMessage(AssistantMessageCmd command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        String userId = command.getUserId();
        String conversationId = command.getConversationId();
        String content = command.getContent();
        Assert.notNull(userId, RespStatus.PARAM_ERROR, "userId 不能为 null");
        Assert.notNull(conversationId, RespStatus.PARAM_ERROR, "conversationId 不能为 null");
        Assert.notNull(content, RespStatus.PARAM_ERROR, "content 不能为 null");

        AgentConversation conversation =
                conversationRepository.findByConversationId(userId, conversationId);
        Assert.notNull(conversation, RespStatus.PARAM_ERROR, "对话不存在");

        conversation.answer(userId, content, command.getSources());

        conversationRepository.update(conversation);
    }

    /**
     * 记录用户反馈。
     */
    @Override
    public void recordFeedback(FeedbackCmd command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        String userId = command.getUserId();
        String conversationId = command.getConversationId();
        String messageId = command.getMessageId();
        Assert.notNull(userId, RespStatus.PARAM_ERROR, "userId 不能为 null");
        Assert.notNull(conversationId, RespStatus.PARAM_ERROR, "conversationId 不能为 null");
        Assert.notNull(messageId, RespStatus.PARAM_ERROR, "messageId 不能为 null");
        Assert.notNull(command.getType(), RespStatus.PARAM_ERROR, "feedbackType 不能为 null");

        AgentConversation conversation =
                conversationRepository.findByConversationId(userId, conversationId);
        Assert.notNull(conversation, RespStatus.PARAM_ERROR, "对话不存在");

        conversation.submitFeedback(userId, messageId, command.getType(), command.getReason());
        conversationRepository.update(conversation);
    }
}
