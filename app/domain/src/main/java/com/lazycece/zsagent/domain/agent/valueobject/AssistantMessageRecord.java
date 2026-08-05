package com.lazycece.zsagent.domain.agent.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

import java.util.List;

/**
 * 助手回答记录（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record AssistantMessageRecord(
        /** 用户ID */
        String userId,
        /** 对话ID */
        String conversationId,
        /** 回答内容 */
        String content,
        /** 来源引用列表 */
        List<SourceReference> sources
) {
}
