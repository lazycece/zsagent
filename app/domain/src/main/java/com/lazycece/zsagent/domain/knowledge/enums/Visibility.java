package com.lazycece.zsagent.domain.knowledge.enums;

import com.lazycece.rapidf.domain.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档可见范围枚举
 *
 * @author lazycece
 */
@Getter
@AllArgsConstructor
public enum Visibility implements BaseEnum<String> {

    PUBLIC("public", "全员可见"),
    DEPARTMENT("department", "指定部门"),
    SPECIFIC("specific", "指定人员"),
    ;

    private final String code;
    private final String desc;
}
