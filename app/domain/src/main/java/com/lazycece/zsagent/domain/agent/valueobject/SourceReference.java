package com.lazycece.zsagent.domain.agent.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 来源引用（值对象）
 *
 * @author lazycece
 */
@Getter
@Setter
@ValueObject
public class SourceReference {

    /** 来源文档ID */
    private String documentId;
    /** 文档标题（快照，不随原文变更） */
    private String documentTitle;
    /** 文本块ID */
    private String chunkId;
    /** 引用片段（前200字） */
    private String contentSnippet;
    /** 相似度得分 */
    private Float score;
}
