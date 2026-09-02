/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
    UPDATE_TIME("update_time", "更新时间"),
    ;
    private final String code;
    private final String desc;
}
