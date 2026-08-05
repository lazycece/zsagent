package com.lazycece.zsagent.domain.agent.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

/**
 * 用户提问记录（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record UserMessageRecord(
        /** 用户ID */
        String userId,
        /** 对话ID */
        String conversationId,
        /** 问题内容 */
        String content
) {
}
