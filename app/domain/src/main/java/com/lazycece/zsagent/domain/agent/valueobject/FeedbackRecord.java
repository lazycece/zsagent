package com.lazycece.zsagent.domain.agent.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;
import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户反馈记录（值对象）
 *
 * @author lazycece
 */
@Getter
@Setter
@ValueObject
public class FeedbackRecord {

    /** 用户ID */
    private String userId;
    /** 对话ID */
    private String conversationId;
    /** 被评价的消息ID */
    private String messageId;
    /** 反馈类型 */
    private FeedbackType type;
    /** 反馈原因（仅 NOT_USEFUL 时可能填写） */
    private String reason;
}
