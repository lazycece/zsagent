package com.lazycece.zsagent.infra.acl.converter;

import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.rapidf.utils.EnumUtils;
import com.lazycece.rapidf.utils.json.JsonUtils;
import com.lazycece.zsagent.domain.agent.enums.ConversationStatus;
import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import com.lazycece.zsagent.domain.agent.enums.MessageRole;
import com.lazycece.zsagent.domain.agent.model.AgentConversation;
import com.lazycece.zsagent.domain.agent.model.AgentMessage;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import com.lazycece.zsagent.infra.dal.po.AgentConversationPO;
import com.lazycece.zsagent.infra.dal.po.AgentMessagePO;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础设施层 ↔ 领域层 对象转换器。
 *
 * @author lazycece
 */
public final class AgentInfraConverter {

    private AgentInfraConverter() {
    }

    // ======================== Conversation: 领域 → PO ========================

    public static AgentConversationPO toConversationPO(AgentConversation conversation) {
        if (conversation == null) {
            return null;
        }
        AgentConversationPO po = new AgentConversationPO();
        po.setConversationId(conversation.getConversationId());
        po.setUserId(conversation.getUserId());
        po.setTitle(conversation.getTitle());
        po.setStatus(conversation.getStatus() != null ? conversation.getStatus().getCode() : null);
        po.setCreator(conversation.getCreator());
        po.setUpdater(conversation.getUpdater());
        po.setCreateTime(conversation.getCreateTime());
        po.setUpdateTime(conversation.getUpdateTime());
        po.setDeleted(conversation.isDeleted());
        return po;
    }

    // ======================== Conversation: PO → 领域 ========================

    public static AgentConversation toConversation(AgentConversationPO po, List<AgentMessagePO> messagePOs) {
        if (po == null) {
            return null;
        }
        AgentConversation conversation = new AgentConversation();
        conversation.setConversationId(po.getConversationId());
        conversation.setUserId(po.getUserId());
        conversation.setTitle(po.getTitle());
        conversation.setStatus(EnumUtils.getEnum(ConversationStatus.class, po.getStatus()));
        conversation.setCreator(po.getCreator());
        conversation.setUpdater(po.getUpdater());
        conversation.setCreateTime(po.getCreateTime());
        conversation.setUpdateTime(po.getUpdateTime());
        conversation.setDeleted(DefaultUtils.defaultValue(po.getDeleted(), false));
        conversation.setMessages(DefaultUtils.defaultList(messagePOs).stream()
                .map(AgentInfraConverter::toMessage)
                .collect(Collectors.toList()));
        return conversation;
    }

    // ======================== Message: 领域 → PO ========================

    public static AgentMessagePO toMessagePO(AgentMessage message) {
        if (message == null) {
            return null;
        }
        AgentMessagePO po = new AgentMessagePO();
        po.setMessageId(message.getMessageId());
        po.setConversationId(message.getConversationId());
        po.setRole(message.getRole() != null ? message.getRole().getCode() : null);
        po.setContent(message.getContent());
        po.setSources(toSourcesJson(message.getSources()));
        if (message.getFeedback() != null) {
            po.setFeedback(message.getFeedback().getCode());
        }
        po.setFeedbackReason(message.getFeedbackReason());
        po.setCreator(message.getCreator());
        po.setUpdater(message.getUpdater());
        po.setCreateTime(message.getCreateTime());
        po.setUpdateTime(message.getUpdateTime());
        po.setDeleted(message.isDeleted());
        return po;
    }

    // ======================== Message: PO → 领域 ========================

    public static AgentMessage toMessage(AgentMessagePO po) {
        if (po == null) {
            return null;
        }
        AgentMessage message = new AgentMessage();
        message.setMessageId(po.getMessageId());
        message.setConversationId(po.getConversationId());
        message.setRole(EnumUtils.getEnum(MessageRole.class, po.getRole()));
        message.setContent(po.getContent());
        message.setSources(parseSources(po.getSources()));
        message.setFeedback(EnumUtils.getEnum(FeedbackType.class, po.getFeedback()));
        message.setFeedbackReason(po.getFeedbackReason());
        message.setCreator(po.getCreator());
        message.setUpdater(po.getUpdater());
        message.setCreateTime(po.getCreateTime());
        message.setUpdateTime(po.getUpdateTime());
        message.setDeleted(DefaultUtils.defaultValue(po.getDeleted(), false));
        return message;
    }

    // ======================== Sources JSON ========================

    private static String toSourcesJson(List<SourceReference> sources) {
        List<SourceReference> safeSources = DefaultUtils.defaultList(sources);
        if (safeSources.isEmpty()) {
            return null;
        }
        return JsonUtils.toJSONString(safeSources);
    }

    private static List<SourceReference> parseSources(String sourcesJson) {
        if (StringUtils.isNotBlank(sourcesJson)) {
            return JsonUtils.parseArray(sourcesJson, SourceReference.class);
        }
        return Collections.emptyList();
    }
}
