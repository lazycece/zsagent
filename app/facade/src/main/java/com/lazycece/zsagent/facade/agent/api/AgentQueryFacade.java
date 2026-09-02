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
package com.lazycece.zsagent.facade.agent.api;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.agent.request.ConversationListQueryRequest;
import com.lazycece.zsagent.facade.agent.request.ConversationQueryRequest;
import com.lazycece.zsagent.facade.agent.result.ConversationListResult;
import com.lazycece.zsagent.facade.agent.result.ConversationResult;

/**
 * Agent 查询门面接口
 *
 * @author lazycece
 */
public interface AgentQueryFacade {

    /**
     * 查询单个对话详情（含所有消息）。
     *
     * @param request 对话查询请求
     * @return 对话详情
     */
    RespData<ConversationResult> getConversation(ConversationQueryRequest request);

    /**
     * 分页查询用户的对话列表。
     *
     * @param request 列表查询请求
     * @return 分页对话列表
     */
    RespData<ConversationListResult> listConversations(ConversationListQueryRequest request);
}
