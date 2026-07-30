package com.lazycece.zsagent.domain.agent.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户提问记录（值对象）
 *
 * @author lazycece
 */
@Getter
@Setter
@ValueObject
public class UserMessageRecord {

    /** 用户ID */
    private String userId;
    /** 对话ID */
    private String conversationId;
    /** 问题内容 */
    private String content;
}
