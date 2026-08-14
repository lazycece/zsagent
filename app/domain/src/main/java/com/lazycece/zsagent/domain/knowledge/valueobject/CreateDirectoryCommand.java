package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

/**
 * 创建目录命令（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record CreateDirectoryCommand(
        /** 操作主体标识 */
        String userId,
        /** 父目录ID（可空，根目录） */
        String parentId,
        /** 目录名称 */
        String name
) {
}
