package com.lazycece.zsagent.domain.agent.model;

import com.lazycece.rapidf.domain.anotation.DomainAggregate;
import com.lazycece.rapidf.domain.model.Aggregate;
import com.lazycece.rapidf.utils.UUIDUtils;
import com.lazycece.zsagent.domain.agent.enums.ConversationStatus;
import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import com.lazycece.zsagent.domain.agent.enums.MessageRole;
import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 对话聚合根
 *
 * @author lazycece
 */
@Getter
@DomainAggregate
public class AgentConversation extends Aggregate<String> {

    /** 对话唯一标识 (UUID) */
    private String conversationId;
    /** 所属用户 */
    private String userId;
    /** 对话标题（取自首条问题，截取前30字） */
    private String title;
    /** 对话状态 */
    private ConversationStatus status;
    /** 消息列表 */
    private List<AgentMessage> messages = new ArrayList<>();

    @Override
    public String getId() {
        return this.conversationId;
    }

    // ======================== 工厂方法 ========================

    /**
     * 创建新对话聚合根。
     */
    public static AgentConversation create(String userId, String conversationId) {
        AgentConversation conversation = new AgentConversation();
        conversation.conversationId = conversationId;
        conversation.userId = userId;
        conversation.status = ConversationStatus.ACTIVE;
        conversation.setCreator(userId);
        conversation.setUpdater(userId);
        conversation.setCreateTime(LocalDateTime.now());
        conversation.setUpdateTime(LocalDateTime.now());
        conversation.setDeleted(false);
        return conversation;
    }

    // ======================== 行为方法 ========================

    /**
     * 发起提问，创建一条 USER 角色消息并追加到消息列表。
     * 若为首条消息，同时设置对话标题（截取前30字）。
     *
     * @param content 问题内容
     * @return 创建的消息
     */
    public AgentMessage askQuestion(String content) {
        if (StringUtils.isBlank(this.title)) {
            this.title = content.length() > 30 ? content.substring(0, 30) : content;
        }
        AgentMessage message = buildMessage(MessageRole.USER, content, null);
        this.messages.add(message);
        return message;
    }

    /**
     * 记录助手回答，创建一条 ASSISTANT 角色消息并追加到消息列表。
     *
     * @param content 回答内容
     * @param sources 来源引用列表
     * @return 创建的消息
     */
    public AgentMessage answer(String content, List<SourceReference> sources) {
        AgentMessage message = buildMessage(MessageRole.ASSISTANT, content, sources);
        this.messages.add(message);
        return message;
    }

    /**
     * 对指定消息提交反馈。
     *
     * @param messageId 被评价的消息ID
     * @param type      反馈类型
     * @param reason    反馈原因（仅 NOT_USEFUL 时可能填写）
     * @throws IllegalArgumentException 消息不存在时抛出
     */
    public void submitFeedback(String messageId, FeedbackType type, String reason) {
        AgentMessage target = this.messages.stream()
                .filter(m -> m.getMessageId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> ExceptionFactory.businessException("消息不存在: " + messageId));
        target.submitFeedback(type, reason);
    }

    /**
     * 归档对话。
     */
    public void archive() {
        this.status = ConversationStatus.ARCHIVED;
    }

    // ======================== 内部方法 ========================

    private AgentMessage buildMessage(MessageRole role, String content, List<SourceReference> sources) {
        AgentMessage message = AgentMessage.create(
                UUIDUtils.uuid(), this.conversationId, role, content, sources);
        message.setCreateTime(LocalDateTime.now());
        return message;
    }
}
