package com.lazycece.zsagent.domain.knowledge.valueobject.query;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 单文档查询条件
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentQuery {

    /** 操作主体标识 */
    private String userId;
    /** 用户所属部门列表 */
    private List<String> userDepts;
    /** 目标文档ID */
    private String documentId;
}
