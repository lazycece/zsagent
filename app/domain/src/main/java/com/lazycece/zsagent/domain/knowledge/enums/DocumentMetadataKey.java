package com.lazycece.zsagent.domain.knowledge.enums;

import com.lazycece.rapidf.domain.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lazycece
 */
@Getter
@AllArgsConstructor
public enum DocumentMetadataKey implements BaseEnum<String> {

    DOCUMENT_ID("document_id", "文档ID"),
    TITLE("title", "文档标题"),
    FORMAT("format", "文档格式"),
    PERMISSION_TYPE("permission_type", "权限类型"),
    PERMISSION_DEPTS("permission_depts", "部门权限"),
    PERMISSION_USERS("permission_users", "用户权限"),
    CURRENT_VERSION("current_version", "文档版本"),
    ;
    private final String code;
    private final String desc;
}
