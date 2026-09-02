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
package com.lazycece.zsagent.domain.agent.service;

import com.lazycece.zsagent.domain.agent.valueobject.cmd.AssistantMessageCmd;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.FeedbackCmd;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.UserMessageCmd;

/**
 * 对话领域服务接口
 *
 * @author lazycece
 */
public interface ConversationDomainService {

    /**
     * 记录用户提问消息。
     * 若对话不存在则自动创建，标题取首条问题前30字。
     *
     * @param command 记录用户提问命令
     */
    void recordUserMessage(UserMessageCmd command);

    /**
     * 记录助手回答消息（含来源引用）。
     *
     * @param command 记录助手回答命令
     */
    void recordAssistantMessage(AssistantMessageCmd command);

    /**
     * 记录用户对某条助手消息的反馈（有用/无用）。
     *
     * @param command 记录反馈命令
     */
    void recordFeedback(FeedbackCmd command);
}
