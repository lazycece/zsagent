package com.lazycece.zsagent.domain.agent.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 助手回答记录（值对象）
 *
 * @author lazycece
 */
@Getter
@Setter
@ValueObject
public class AssistantMessageRecord {

    /** 用户ID */
    private String userId;
    /** 对话ID */
    private String conversationId;
    /** 回答内容 */
    private String content;
    /** 来源引用列表 */
    private List<SourceReference> sources;
}
