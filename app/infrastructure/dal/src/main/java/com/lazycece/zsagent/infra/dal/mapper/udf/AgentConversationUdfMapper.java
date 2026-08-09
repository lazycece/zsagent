package com.lazycece.zsagent.infra.dal.mapper.udf;

import com.lazycece.zsagent.infra.dal.po.AgentConversationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * agent_conversation 表用户自定义 Mapper
 *
 * @author lazycece
 */
@Mapper
public interface AgentConversationUdfMapper {

    /**
     * 插入对话记录。
     */
    int insert(AgentConversationPO conversation);

    /**
     * 按 userId + conversationId 查询单条记录。
     */
    AgentConversationPO selectByUserIdAndConversationId(
            @Param("userId") String userId,
            @Param("conversationId") String conversationId);

    /**
     * 按 userId 分页查询，不含消息列表。
     */
    List<AgentConversationPO> selectByUserId(
            @Param("userId") String userId,
            @Param("offset") int offset,
            @Param("size") int size);

    /**
     * 按 userId 统计对话总数。
     */
    long countByUserId(@Param("userId") String userId);

    /**
     * 更新对话。
     */
    int update(AgentConversationPO conversation);
}
