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
package com.lazycece.zsagent.domain.knowledge.valueobject.query;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentStatus;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 文档计数查询条件
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentCountQuery {

    /** 操作主体标识 */
    private String userId;
    /** 用户所属部门列表 */
    private List<String> userDepts;
    /** 按目录过滤（可空） */
    private String directoryId;
    /** 按状态过滤（可空） */
    private DocumentStatus status;
    /** 标题/标签模糊搜索（可空） */
    private String keyword;
}
