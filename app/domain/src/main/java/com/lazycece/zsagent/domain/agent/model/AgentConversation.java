package com.lazycece.zsagent.domain.agent.model;

import com.lazycece.rapidf.domain.anotation.DomainAggregate;
import com.lazycece.rapidf.domain.model.Aggregate;
import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.zsagent.domain.agent.enums.ConversationStatus;
import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import com.lazycece.zsagent.domain.agent.enums.MessageRole;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 对话聚合根
 *
 * @author lazycece
 */
@Getter
@Setter
@DomainAggregate
public class AgentConversation extends Aggregate<String> {

    /**
     * 对话唯一标识 (UUID)
     */
    private String conversationId;
    /**
     * 所属用户
     */
    private String userId;
    /**
     * 对话标题（取自首条问题，截取前30字）
     */
    private String title;
    /**
     * 对话状态
     */
    private ConversationStatus status;
    /**
     * 消息列表
     */
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
        // 由端视角处理，不默认生成
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
     * 发起提问，创建一条 USER 角色消息并追加到消息列表。 若为首条消息，同时设置对话标题（截取前30字）。
     *
     * @param content 问题内容
     * @return 创建的消息
     */
    public AgentMessage askQuestion(String userId, String content) {
        if (StringUtils.isBlank(this.title)) {
            this.title = content.length() > 30 ? content.substring(0, 30) : content;
        }
        AgentMessage message = AgentMessage.create(userId, this.conversationId, MessageRole.USER,
                content, null);
        this.messages.add(message);
        super.setUpdater(userId);
        super.setCreateTime(LocalDateTime.now());
        return message;
    }

    /**
     * 记录助手回答，创建一条 ASSISTANT 角色消息并追加到消息列表。
     *
     * @param content 回答内容
     * @param sources 来源引用列表
     * @return 创建的消息
     */
    public AgentMessage answer(String userId, String content, List<SourceReference> sources) {
        AgentMessage message = AgentMessage.create(userId, this.conversationId,
                MessageRole.ASSISTANT, content, sources);
        this.messages.add(message);
        super.setUpdater(userId);
        super.setUpdateTime(LocalDateTime.now());
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
    public void submitFeedback(String userId, String messageId, FeedbackType type, String reason) {
        AgentMessage target = this.messages.stream().filter(m -> m.getMessageId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> ExceptionFactory.businessException("消息不存在: " + messageId));
        target.submitFeedback(userId, type, reason);
        super.setUpdater(userId);
        super.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 归档对话。
     */
    public void archive() {
        this.status = ConversationStatus.ARCHIVED;
    }

}
