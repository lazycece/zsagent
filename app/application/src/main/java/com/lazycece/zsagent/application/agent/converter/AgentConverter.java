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
package com.lazycece.zsagent.application.agent.converter;

import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.zsagent.domain.agent.model.AgentConversation;
import com.lazycece.zsagent.domain.agent.model.AgentMessage;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import com.lazycece.zsagent.facade.agent.dto.ConversationDTO;
import com.lazycece.zsagent.facade.agent.dto.MessageDTO;
import com.lazycece.zsagent.facade.agent.dto.SourceReferenceDTO;
import com.lazycece.zsagent.facade.agent.result.ConversationListResult;
import com.lazycece.zsagent.facade.agent.result.ConversationResult;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Agent 数据转换器。 负责将领域对象转换为 Facade DTO/Result。
 *
 * @author lazycece
 */
public final class AgentConverter {

    /**
     * 将对话聚合根转换为对话 Result。
     */
    public static ConversationResult toConversationResult(AgentConversation conversation) {
        if (conversation == null) {
            return null;
        }
        ConversationResult result = new ConversationResult();
        result.setConversation(toConversationDTO(conversation));
        return result;
    }

    /**
     * 将对话列表转换为对话列表 Result。
     */
    public static ConversationListResult toConversationListResult(
            List<AgentConversation> conversations, long total, int page, int size) {
        ConversationListResult result = new ConversationListResult();
        result.setList(
                DefaultUtils.defaultList(conversations).stream()
                        .map(AgentConverter::toConversationDTO)
                        .collect(Collectors.toList()));
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        return result;
    }

    /**
     * 对话聚合根 → ConversationDTO。
     */
    public static ConversationDTO toConversationDTO(AgentConversation conversation) {
        if (conversation == null) {
            return null;
        }
        ConversationDTO dto = new ConversationDTO();
        dto.setConversationId(conversation.getConversationId());
        dto.setTitle(conversation.getTitle());
        dto.setCreateTime(conversation.getCreateTime());
        dto.setUpdatedAt(conversation.getUpdateTime());
        // 消息列表：详情接口加载，列表接口这里也做兜底转换
        if (conversation.getMessages() != null) {
            dto.setMessages(
                    conversation.getMessages().stream()
                            .map(AgentConverter::toMessageDTO)
                            .collect(Collectors.toList()));
        } else {
            dto.setMessages(Collections.emptyList());
        }
        return dto;
    }

    /**
     * 消息实体 → MessageDTO。
     */
    public static MessageDTO toMessageDTO(AgentMessage message) {
        if (message == null) {
            return null;
        }
        MessageDTO dto = new MessageDTO();
        dto.setMessageId(message.getMessageId());
        dto.setRole(message.getRole() != null ? message.getRole().getCode() : null);
        dto.setContent(message.getContent());
        dto.setCreateTime(message.getCreateTime());
        // feedback：枚举转 code 字符串
        if (message.getFeedback() != null) {
            dto.setFeedback(message.getFeedback().getCode());
        }
        // sources：领域值对象 → DTO
        if (message.getSources() != null) {
            dto.setSources(
                    message.getSources().stream()
                            .map(AgentConverter::toSourceReferenceDTO)
                            .collect(Collectors.toList()));
        }
        return dto;
    }

    /**
     * 来源引用值对象 → SourceReferenceDTO。
     */
    public static SourceReferenceDTO toSourceReferenceDTO(SourceReference ref) {
        if (ref == null) {
            return null;
        }
        SourceReferenceDTO dto = new SourceReferenceDTO();
        dto.setDocumentId(ref.documentId());
        dto.setDocumentTitle(ref.documentTitle());
        dto.setContentSnippet(ref.contentSnippet());
        dto.setScore(ref.score());
        return dto;
    }
}
