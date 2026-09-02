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
package com.lazycece.zsagent.infra.dal.mapper.udf;

import com.lazycece.zsagent.infra.dal.po.AgentConversationPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
            @Param("userId") String userId, @Param("conversationId") String conversationId);

    /**
     * 按 userId 分页查询，不含消息列表。
     */
    List<AgentConversationPO> selectByUserId(
            @Param("userId") String userId, @Param("offset") int offset, @Param("size") int size);

    /**
     * 按 userId 统计对话总数。
     */
    long countByUserId(@Param("userId") String userId);

    /**
     * 更新对话。
     */
    int update(AgentConversationPO conversation);
}
