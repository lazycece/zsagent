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
package com.lazycece.zsagent.domain.knowledge.valueobject.cmd;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 创建文档命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class CreateDocumentCmd {

    /** 操作主体标识 */
    private String userId;
    /** 文档标题 */
    private String title;
    /** 文件格式 */
    private DocumentFormat format;
    /** 文件大小（字节） */
    private Long fileSize;
    /** 文件存储路径 */
    private String filePath;
    /** 所属目录ID（可空） */
    private String directoryId;
    /** 标签列表 */
    private List<String> tags;
    /** 可见范围 */
    private Visibility visibility;
    /** 可见对象列表 */
    private List<String> visibleTo;
}
