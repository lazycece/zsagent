package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

/**
 * 回滚文档命令（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record RollbackDocumentCommand(
        /** 操作主体标识 */
        String userId,
        /** 文档ID */
        String documentId,
        /** 目标版本ID */
        String targetVersionId
) {
}
