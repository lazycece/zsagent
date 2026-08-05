package com.lazycece.zsagent.domain.agent.service.impl;

import com.lazycece.rapidf.domain.anotation.DomainService;
import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.zsagent.domain.agent.model.AgentConversation;
import com.lazycece.zsagent.domain.agent.repository.AgentConversationRepository;
import com.lazycece.zsagent.domain.agent.service.ConversationDomainService;
import com.lazycece.zsagent.domain.agent.valueobject.AssistantMessageRecord;
import com.lazycece.zsagent.domain.agent.valueobject.FeedbackRecord;
import com.lazycece.zsagent.domain.agent.valueobject.UserMessageRecord;

import java.time.LocalDateTime;

/**
 * 对话领域服务实现。
 * 领域服务仅做编排：参数校验 → 获取聚合 → 委托聚合行为 → 持久化。
 * 业务规则封装在聚合根内部。
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
     * 记录用户提问消息。
     * 首次对话时通过工厂方法创建聚合根，已有对话时仅更新操作者信息。
     */
    @Override
    public void recordUserMessage(UserMessageRecord record) {
        Assert.notNull(record, RespStatus.PARAM_ERROR, "record 不能为 null");
        String userId = record.userId();
        String conversationId = record.conversationId();
        String content = record.content();
        Assert.notNull(userId, RespStatus.PARAM_ERROR, "userId 不能为 null");
        Assert.notNull(conversationId, RespStatus.PARAM_ERROR, "conversationId 不能为 null");
        Assert.notNull(content, RespStatus.PARAM_ERROR, "content 不能为 null");

        AgentConversation conversation = conversationRepository.findByConversationId(userId, conversationId);

        if (conversation == null) {
            conversation = AgentConversation.create(userId, conversationId);
            conversation.askQuestion(content);
            conversationRepository.save(conversation);
        } else {
            conversation.setUpdater(userId);
            conversation.setUpdateTime(LocalDateTime.now());
            conversation.askQuestion(content);
            conversationRepository.update(conversation);
        }
    }

    /**
     * 记录助手回答消息。
     */
    @Override
    public void recordAssistantMessage(AssistantMessageRecord record) {
        Assert.notNull(record, RespStatus.PARAM_ERROR, "record 不能为 null");
        String userId = record.userId();
        String conversationId = record.conversationId();
        String content = record.content();
        Assert.notNull(userId, RespStatus.PARAM_ERROR, "userId 不能为 null");
        Assert.notNull(conversationId, RespStatus.PARAM_ERROR, "conversationId 不能为 null");
        Assert.notNull(content, RespStatus.PARAM_ERROR, "content 不能为 null");

        AgentConversation conversation = conversationRepository.findByConversationId(userId, conversationId);
        Assert.notNull(conversation, RespStatus.PARAM_ERROR, "对话不存在");

        conversation.setUpdater(userId);
        conversation.setUpdateTime(LocalDateTime.now());
        conversation.answer(content, record.sources());
        conversationRepository.update(conversation);
    }

    /**
     * 记录用户反馈。
     */
    @Override
    public void recordFeedback(FeedbackRecord record) {
        Assert.notNull(record, RespStatus.PARAM_ERROR, "record 不能为 null");
        String userId = record.userId();
        String conversationId = record.conversationId();
        String messageId = record.messageId();
        Assert.notNull(userId, RespStatus.PARAM_ERROR, "userId 不能为 null");
        Assert.notNull(conversationId, RespStatus.PARAM_ERROR, "conversationId 不能为 null");
        Assert.notNull(messageId, RespStatus.PARAM_ERROR, "messageId 不能为 null");
        Assert.notNull(record.type(), RespStatus.PARAM_ERROR, "feedbackType 不能为 null");

        AgentConversation conversation = conversationRepository.findByConversationId(userId, conversationId);
        Assert.notNull(conversation, RespStatus.PARAM_ERROR, "对话不存在");

        conversation.setUpdater(userId);
        conversation.setUpdateTime(LocalDateTime.now());
        conversation.submitFeedback(messageId, record.type(), record.reason());
        conversationRepository.update(conversation);
    }
}
