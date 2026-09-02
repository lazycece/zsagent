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
package com.lazycece.zsagent.application.agent;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.domain.model.Pagination;
import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.zsagent.application.agent.converter.AgentConverter;
import com.lazycece.zsagent.domain.agent.model.AgentConversation;
import com.lazycece.zsagent.domain.agent.repository.AgentConversationRepository;
import com.lazycece.zsagent.facade.agent.api.AgentQueryFacade;
import com.lazycece.zsagent.facade.agent.request.ConversationListQueryRequest;
import com.lazycece.zsagent.facade.agent.request.ConversationQueryRequest;
import com.lazycece.zsagent.facade.agent.result.ConversationListResult;
import com.lazycece.zsagent.facade.agent.result.ConversationResult;
import java.util.List;
import org.springframework.context.annotation.Primary;

/**
 * Agent 查询门面实现。
 * 负责对话查询：单条对话详情 + 分页对话列表。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class AgentQueryFacadeImpl implements AgentQueryFacade {

    private final AgentConversationRepository conversationRepository;

    public AgentQueryFacadeImpl(AgentConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public RespData<ConversationResult> getConversation(ConversationQueryRequest request) {
        AgentConversation conversation =
                conversationRepository.findByConversationId(
                        request.getUserId(), request.getConversationId());
        Assert.notNull(
                conversation,
                RespStatus.DATA_NOT_EXIST,
                "对话不存在: conversationId={}",
                request.getConversationId());
        return RespData.success(AgentConverter.toConversationResult(conversation));
    }

    @Override
    public RespData<ConversationListResult> listConversations(
            ConversationListQueryRequest request) {
        Pagination pagination = new Pagination(request.getPage(), request.getSize());
        List<AgentConversation> conversations =
                conversationRepository.findByUserId(request.getUserId(), pagination);
        long total = pagination.getCount() != null ? pagination.getCount() : 0;
        return RespData.success(
                AgentConverter.toConversationListResult(
                        conversations, total, request.getPage(), request.getSize()));
    }
}
