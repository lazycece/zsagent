package com.lazycece.zsagent.domain.knowledge.enums;

import com.lazycece.rapidf.domain.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档状态枚举
 *
 * @author lazycece
 */
@Getter
@AllArgsConstructor
public enum DocumentStatus implements BaseEnum<String> {

    DRAFT("draft", "草稿"),
    PUBLISHED("published", "已发布"),
    ARCHIVED("archived", "已归档"),
    DELETED("deleted", "已删除"),
    ;

    private final String code;
    private final String desc;
}
