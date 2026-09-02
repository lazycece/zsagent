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
 * agent_document_version 表 PO
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentVersionPO {

    /** 主键 */
    private Long id;
    /** 版本唯一标识 */
    private String versionId;
    /** 所属文档ID */
    private String documentId;
    /** 版本号 */
    private Integer versionNumber;
    /** 该版本文件存储路径 */
    private String filePath;
    /** 该版本文件大小（字节） */
    private Long fileSize;
    /** 变更说明 */
    private String changeLog;
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
