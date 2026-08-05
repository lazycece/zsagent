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
