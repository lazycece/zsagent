package com.lazycece.zsagent.domain.agent.service;

import com.lazycece.zsagent.domain.agent.valueobject.AssistantMessageRecord;
import com.lazycece.zsagent.domain.agent.valueobject.FeedbackRecord;
import com.lazycece.zsagent.domain.agent.valueobject.UserMessageRecord;

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
     * @param record 用户提问记录
     */
    void recordUserMessage(UserMessageRecord record);

    /**
     * 记录助手回答消息（含来源引用）。
     *
     * @param record 助手回答记录
     */
    void recordAssistantMessage(AssistantMessageRecord record);

    /**
     * 记录用户对某条助手消息的反馈（有用/无用）。
     *
     * @param record 反馈记录
     */
    void recordFeedback(FeedbackRecord record);
}
