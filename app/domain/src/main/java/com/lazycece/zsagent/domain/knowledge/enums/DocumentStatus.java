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
