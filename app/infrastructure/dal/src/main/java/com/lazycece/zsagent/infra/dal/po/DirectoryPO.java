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
package com.lazycece.zsagent.infra.dal.po;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * agent_directory 表 PO
 *
 * @author lazycece
 */
@Getter
@Setter
public class DirectoryPO {

    /** 主键 */
    private Long id;
    /** 目录唯一标识 */
    private String directoryId;
    /** 父目录ID */
    private String parentId;
    /** 目录名称 */
    private String name;
    /** 排序序号 */
    private Integer sortOrder;
    /** 创建人 */
    private String creator;
    /** 更新人 */
    private String updater;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 逻辑删除 */
    private Boolean deleted;
}
