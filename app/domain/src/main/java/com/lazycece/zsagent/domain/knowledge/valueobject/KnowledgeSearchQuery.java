package com.lazycece.zsagent.domain.knowledge.valueobject;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 知识块权限检索条件
 *
 * @author lazycece
 */
@Getter
@Setter
public class KnowledgeSearchQuery {

    /** 查询文本 */
    private String query;
    /** 返回数量上限 */
    private int topK;
    /** 当前用户 ID */
    private String userId;
    /** 当前用户所属部门列表 */
    private List<String> userDepts;
}
