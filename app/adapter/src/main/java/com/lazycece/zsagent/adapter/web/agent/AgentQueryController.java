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
 * Agent 查询控制器
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/agent")
public class AgentQueryController {

    private final AgentQueryFacade agentQueryFacade;

    public AgentQueryController(AgentQueryFacade agentQueryFacade) {
        this.agentQueryFacade = agentQueryFacade;
    }

    /**
     * 查询对话详情。
     */
    @GetMapping("/get-conversation")
    public RespData<ConversationResult> getConversation(@Validated ConversationQueryRequest request) {
        return agentQueryFacade.getConversation(request);
    }

    /**
     * 查询对话列表。
     */
    @GetMapping("/conversations")
    public RespData<ConversationListResult> listConversations(@Validated ConversationListQueryRequest request) {
        return agentQueryFacade.listConversations(request);
    }
}
