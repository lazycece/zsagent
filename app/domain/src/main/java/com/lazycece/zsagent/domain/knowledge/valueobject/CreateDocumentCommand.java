package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;

import java.util.List;

/**
 * 创建文档命令（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record CreateDocumentCommand(
        /** 操作主体标识 */
        String userId,
        /** 文档标题 */
        String title,
        /** 文件格式 */
        DocumentFormat format,
        /** 文件大小（字节） */
        Long fileSize,
        /** 文件存储路径 */
        String filePath,
        /** 所属目录ID（可空） */
        String directoryId,
        /** 标签列表 */
        List<String> tags,
        /** 可见范围 */
        Visibility visibility,
        /** 可见对象列表 */
        List<String> visibleTo
) {
}
