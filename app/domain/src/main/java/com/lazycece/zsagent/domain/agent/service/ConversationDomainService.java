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
