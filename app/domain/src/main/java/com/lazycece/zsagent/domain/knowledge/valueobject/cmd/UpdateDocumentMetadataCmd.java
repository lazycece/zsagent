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

import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新文档元数据命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class UpdateDocumentMetadataCmd {

    /** 操作主体标识 */
    private String userId;
    /** 文档ID */
    private String documentId;

    /* ======= metadata ============  */
    /** 文档标题 */
    private String title;
    /** 文档摘要 */
    private String summary;
    /** 所属目录ID */
    private String directoryId;
    /** 标签列表 */
    private List<String> tags;
    /** 可见范围 */
    private Visibility visibility;
    /** 可见对象列表 */
    private List<String> visibleTo;
}
