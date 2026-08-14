package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

import java.util.List;

/**
 * 单文档查询条件（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record DocumentQuery(
        /** 操作主体标识 */
        String userId,
        /** 用户所属部门列表 */
        List<String> userDepts,
        /** 目标文档ID */
        String documentId
) {
}
