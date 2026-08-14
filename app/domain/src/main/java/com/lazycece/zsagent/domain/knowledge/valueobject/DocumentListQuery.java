package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentStatus;

import java.util.List;

/**
 * 文档列表查询条件（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record DocumentListQuery(
        /** 操作主体标识 */
        String userId,
        /** 用户所属部门列表 */
        List<String> userDepts,
        /** 按目录过滤（可空） */
        String directoryId,
        /** 按状态过滤（可空） */
        DocumentStatus status,
        /** 标题/标签模糊搜索（可空） */
        String keyword
) {
}
