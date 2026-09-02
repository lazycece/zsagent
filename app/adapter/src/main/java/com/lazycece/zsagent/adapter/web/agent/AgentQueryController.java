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
package com.lazycece.zsagent.adapter.web.agent;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.agent.api.AgentQueryFacade;
import com.lazycece.zsagent.facade.agent.request.ConversationListQueryRequest;
import com.lazycece.zsagent.facade.agent.request.ConversationQueryRequest;
import com.lazycece.zsagent.facade.agent.result.ConversationListResult;
import com.lazycece.zsagent.facade.agent.result.ConversationResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Agent 查询控制器，直接实现门面接口，仅负责请求转发。
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/agent")
public class AgentQueryController implements AgentQueryFacade {

    private final AgentQueryFacade agentQueryFacade;

    public AgentQueryController(AgentQueryFacade agentQueryFacade) {
        this.agentQueryFacade = agentQueryFacade;
    }

    /**
     * 查询对话详情。
     */
    @Override
    @GetMapping("/get-conversation")
    public RespData<ConversationResult> getConversation(
            @Validated ConversationQueryRequest request) {
        return agentQueryFacade.getConversation(request);
    }

    /**
     * 查询对话列表。
     */
    @Override
    @GetMapping("/list-conversations")
    public RespData<ConversationListResult> listConversations(
            @Validated ConversationListQueryRequest request) {
        return agentQueryFacade.listConversations(request);
    }
}
