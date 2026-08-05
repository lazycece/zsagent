package com.lazycece.zsagent.domain.agent.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;
import com.lazycece.zsagent.domain.agent.enums.FeedbackType;

/**
 * 用户反馈记录（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record FeedbackRecord(
        /** 用户ID */
        String userId,
        /** 对话ID */
        String conversationId,
        /** 被评价的消息ID */
        String messageId,
        /** 反馈类型 */
        FeedbackType type,
        /** 反馈原因（仅 NOT_USEFUL 时可能填写） */
        String reason
) {
}
