package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

import java.util.List;

/**
 * 知识块权限检索条件（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record KnowledgeSearchQuery(
        /** 查询文本 */
        String query,
        /** 返回数量上限 */
        int topK,
        /** 当前用户 ID */
        String userId,
        /** 当前用户所属部门列表 */
        List<String> userDepts
) {
}
