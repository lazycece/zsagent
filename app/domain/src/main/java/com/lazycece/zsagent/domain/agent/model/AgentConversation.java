package com.lazycece.zsagent.domain.agent.model;

import com.lazycece.rapidf.domain.anotation.DomainAggregate;
import com.lazycece.rapidf.domain.model.Aggregate;
import com.lazycece.zsagent.domain.agent.enums.ConversationStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 对话聚合根
 *
 * @author lazycece
 */
@Getter
@Setter
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
}
