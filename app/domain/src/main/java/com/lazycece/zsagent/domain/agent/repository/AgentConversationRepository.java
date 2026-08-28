package com.lazycece.zsagent.domain.agent.repository;

import com.lazycece.rapidf.domain.model.Pagination;
import com.lazycece.zsagent.domain.agent.model.AgentConversation;

import java.util.List;

/**
 * 对话仓储接口
 *
 * @author lazycece
 */
public interface AgentConversationRepository {

    /**
     * 新建对话，返回 conversationId。
     *
     * @param conversation 对话聚合根
     * @return conversationId
     */
    String save(AgentConversation conversation);

    /**
     * 按 userId + conversationId 联合查询单条对话。
     *
     * @param userId         用户ID
     * @param conversationId 对话ID
     * @return 对话聚合根，不存在时返回 null
     */
    AgentConversation findByConversationId(String userId, String conversationId);

    /**
     * 分页查询用户的对话列表（仅返回摘要信息，不加载消息列表）。
     *
     * @param userId     用户ID
     * @param pagination 分页参数
     * @return 对话列表
     */
    List<AgentConversation> findByUserId(String userId, Pagination pagination);

    /**
     * 更新已有对话。
     *
     * @param conversation 对话聚合根
     */
    void update(AgentConversation conversation);
}
