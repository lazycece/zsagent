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
package com.lazycece.zsagent.infra.acl.repository;

import com.lazycece.rapidf.domain.anotation.DomainRepository;
import com.lazycece.rapidf.domain.model.Pagination;
import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.zsagent.domain.agent.model.AgentConversation;
import com.lazycece.zsagent.domain.agent.model.AgentMessage;
import com.lazycece.zsagent.domain.agent.repository.AgentConversationRepository;
import com.lazycece.zsagent.infra.acl.converter.AgentInfraConverter;
import com.lazycece.zsagent.infra.dal.mapper.udf.AgentConversationUdfMapper;
import com.lazycece.zsagent.infra.dal.mapper.udf.AgentMessageUdfMapper;
import com.lazycece.zsagent.infra.dal.po.AgentConversationPO;
import com.lazycece.zsagent.infra.dal.po.AgentMessagePO;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对话仓储 MyBatis 实现。
 *
 * @author lazycece
 */
@DomainRepository
public class AgentConversationRepositoryImpl implements AgentConversationRepository {

    private static final Logger log =
            LoggerFactory.getLogger(AgentConversationRepositoryImpl.class);

    private final AgentConversationUdfMapper conversationMapper;
    private final AgentMessageUdfMapper messageMapper;

    public AgentConversationRepositoryImpl(
            AgentConversationUdfMapper conversationMapper, AgentMessageUdfMapper messageMapper) {
        this.conversationMapper = conversationMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(AgentConversation conversation) {
        AgentConversationPO convPO = AgentInfraConverter.toConversationPO(conversation);
        conversationMapper.insert(convPO);

        List<AgentMessage> messages = DefaultUtils.defaultList(conversation.getMessages());
        if (!messages.isEmpty()) {
            List<AgentMessagePO> messagePOs =
                    messages.stream()
                            .map(AgentInfraConverter::toMessagePO)
                            .collect(Collectors.toList());
            messageMapper.insertBatch(messagePOs);
        }

        log.debug(
                "新建对话: conversationId={}, 消息数={}",
                conversation.getConversationId(),
                messages.size());
        return conversation.getConversationId();
    }

    @Override
    public AgentConversation findByConversationId(String userId, String conversationId) {
        AgentConversationPO convPO =
                conversationMapper.selectByUserIdAndConversationId(userId, conversationId);
        if (convPO == null) {
            return null;
        }
        List<AgentMessagePO> messagePOs = messageMapper.selectByConversationId(conversationId);
        return AgentInfraConverter.toConversation(convPO, messagePOs);
    }

    @Override
    public List<AgentConversation> findByUserId(String userId, Pagination pagination) {
        long total = conversationMapper.countByUserId(userId);
        pagination.setCount(total);

        int offset = (pagination.getPage() - 1) * pagination.getSize();
        List<AgentConversationPO> convPOs =
                conversationMapper.selectByUserId(userId, offset, pagination.getSize());

        return convPOs.stream()
                .map(po -> AgentInfraConverter.toConversation(po, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AgentConversation conversation) {
        AgentConversationPO convPO = AgentInfraConverter.toConversationPO(conversation);
        conversationMapper.update(convPO);

        if (conversation.getMessages() != null) {
            List<AgentMessage> messages = conversation.getMessages();
            messageMapper.deleteByConversationId(conversation.getConversationId());
            if (!messages.isEmpty()) {
                List<AgentMessagePO> messagePOs =
                        messages.stream()
                                .map(AgentInfraConverter::toMessagePO)
                                .collect(Collectors.toList());
                messageMapper.insertBatch(messagePOs);
            }
            log.debug(
                    "更新对话: conversationId={}, 消息数={}",
                    conversation.getConversationId(),
                    messages.size());
        } else {
            log.debug("更新对话: conversationId={}", conversation.getConversationId());
        }
    }
}
