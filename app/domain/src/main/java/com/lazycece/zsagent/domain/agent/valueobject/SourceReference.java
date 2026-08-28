package com.lazycece.zsagent.domain.agent.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

/**
 * 来源引用（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record SourceReference(
        /** 来源文档ID */
        String documentId,
        /** 文档标题（快照，不随原文变更） */
        String documentTitle,
        /** 文本块ID */
        String chunkId,
        /** 引用片段（前200字） */
        String contentSnippet,
        /** 相似度得分 */
        Float score
) {
}
