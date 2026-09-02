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
package com.lazycece.zsagent.facade.knowledge.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 文档 DTO
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentDTO {

    /** 文档唯一标识 */
    private String documentId;

    /** 文档标题 */
    private String title;

    /** 自动生成摘要 */
    private String summary;

    /** 文件格式 */
    private String format;

    /** 当前版本文件大小（字节） */
    private Long fileSize;

    /** 所属目录ID */
    private String directoryId;

    /** 目录名称（JOIN 查询填充） */
    private String directoryName;

    /** 标签列表 */
    private List<String> tags;

    /** 可见范围 */
    private String visibility;

    /** 文档状态 */
    private String status;

    /** ETL 处理状态 */
    private String etlStatus;

    /** ETL 错误信息（仅 FAILED 时有值） */
    private String etlErrorMessage;

    /** 当前版本号 */
    private Integer currentVersion;

    /** 创建人 */
    private String creator;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
