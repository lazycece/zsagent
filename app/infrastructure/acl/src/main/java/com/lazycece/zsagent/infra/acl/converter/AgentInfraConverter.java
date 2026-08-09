package com.lazycece.zsagent.infra.acl.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.zsagent.domain.agent.enums.ConversationStatus;
import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import com.lazycece.zsagent.domain.agent.enums.MessageRole;
import com.lazycece.zsagent.domain.agent.model.AgentConversation;
import com.lazycece.zsagent.domain.agent.model.AgentMessage;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import com.lazycece.zsagent.infra.dal.po.AgentConversationPO;
import com.lazycece.zsagent.infra.dal.po.AgentMessagePO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础设施层 ↔ 领域层 对象转换器。
 *
 * @author lazycece
 */
public final class AgentInfraConverter {

    private static final Logger log = LoggerFactory.getLogger(AgentInfraConverter.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();


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
        conversation.setStatus(enumByCode(ConversationStatus.values(), po.getStatus()));
        conversation.setCreator(po.getCreator());
        conversation.setUpdater(po.getUpdater());
        conversation.setCreateTime(po.getCreateTime());
        conversation.setUpdateTime(po.getUpdateTime());
        conversation.setDeleted(po.getDeleted() != null ? po.getDeleted() : false);
        if (messagePOs != null) {
            conversation.setMessages(messagePOs.stream()
                    .map(AgentInfraConverter::toMessage)
                    .collect(Collectors.toList()));
        } else {
            conversation.setMessages(Collections.emptyList());
        }
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
        message.setRole(enumByCode(MessageRole.values(), po.getRole()));
        message.setContent(po.getContent());
        message.setSources(parseSources(po.getSources()));
        message.setFeedback(enumByCode(FeedbackType.values(), po.getFeedback()));
        message.setFeedbackReason(po.getFeedbackReason());
        message.setCreator(po.getCreator());
        message.setUpdater(po.getUpdater());
        message.setCreateTime(po.getCreateTime());
        message.setUpdateTime(po.getUpdateTime());
        message.setDeleted(po.getDeleted() != null ? po.getDeleted() : false);
        return message;
    }

    // ======================== Sources JSON ========================

    private static String toSourcesJson(List<SourceReference> sources) {
        if (sources == null || sources.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(sources);
        } catch (JsonProcessingException e) {
            log.error("序列化 sources 失败", e);
            throw ExceptionFactory.serverException("序列化 sources 失败");
        }
    }

    private static List<SourceReference> parseSources(String sourcesJson) {
        if (sourcesJson == null || sourcesJson.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(sourcesJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("反序列化 sources 失败: {}", sourcesJson, e);
            return Collections.emptyList();
        }
    }

    // ======================== Enum 转换 ========================

    private static <T extends com.lazycece.rapidf.domain.model.BaseEnum<String>> T enumByCode(
            T[] values, String code) {
        if (code == null) {
            return null;
        }
        for (T v : values) {
            if (v.getCode().equals(code)) {
                return v;
            }
        }
        return null;
    }
}
