package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;

import java.util.List;

/**
 * 更新文档元数据命令（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record UpdateDocumentMetadataCommand(
        /** 操作主体标识 */
        String userId,
        /** 文档ID */
        String documentId,
        /** 文档标题 */
        String title,
        /** 文档摘要 */
        String summary,
        /** 所属目录ID */
        String directoryId,
        /** 标签列表 */
        List<String> tags,
        /** 可见范围 */
        Visibility visibility,
        /** 可见对象列表 */
        List<String> visibleTo
) {
}
