package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

/**
 * 更新文档内容命令（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record UpdateDocumentContentCommand(
        /** 操作主体标识 */
        String userId,
        /** 文档ID */
        String documentId,
        /** 新文件存储路径 */
        String filePath,
        /** 新文件大小（字节） */
        Long fileSize,
        /** 变更说明 */
        String changeLog
) {
}
