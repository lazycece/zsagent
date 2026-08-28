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
import org.springframework.context.annotation.Primary;

import java.util.List;

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
        AgentConversation conversation = conversationRepository.findByConversationId(
                request.getUserId(), request.getConversationId());
        Assert.notNull(conversation, RespStatus.DATA_NOT_EXIST,
                "对话不存在: conversationId={}", request.getConversationId());
        return RespData.success(AgentConverter.toConversationResult(conversation));
    }

    @Override
    public RespData<ConversationListResult> listConversations(ConversationListQueryRequest request) {
        Pagination pagination = new Pagination(request.getPage(), request.getSize());
        List<AgentConversation> conversations = conversationRepository.findByUserId(
                request.getUserId(), pagination);
        long total = pagination.getCount() != null ? pagination.getCount() : 0;
        return RespData.success(AgentConverter.toConversationListResult(
                conversations, total, request.getPage(), request.getSize()));
    }
}
