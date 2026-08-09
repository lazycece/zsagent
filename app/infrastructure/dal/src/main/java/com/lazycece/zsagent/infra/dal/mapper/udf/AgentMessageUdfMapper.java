package com.lazycece.zsagent.infra.dal.mapper.udf;

import com.lazycece.zsagent.infra.dal.po.AgentMessagePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * agent_message 表用户自定义 Mapper
 *
 * @author lazycece
 */
@Mapper
public interface AgentMessageUdfMapper {

    /**
     * 批量插入消息。
     */
    int insertBatch(List<AgentMessagePO> messages);

    /**
     * 按 conversationId 查询所有消息（按 createTime 升序）。
     */
    List<AgentMessagePO> selectByConversationId(@Param("conversationId") String conversationId);

    /**
     * 更新消息内容。
     */
    int update(AgentMessagePO message);

    /**
     * 按 conversationId 逻辑删除所有消息。
     */
    int deleteByConversationId(@Param("conversationId") String conversationId,
                               @Param("updateTime") LocalDateTime updateTime);
}
