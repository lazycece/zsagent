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

import com.lazycece.zsagent.infra.dal.po.AgentMessagePO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    int deleteByConversationId(@Param("conversationId") String conversationId);
}
