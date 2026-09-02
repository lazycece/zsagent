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
package com.lazycece.zsagent.facade.agent.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 来源引用DTO
 *
 * @author lazycece
 */
@Getter
@Setter
public class SourceReferenceDTO {

    /** 来源文档ID */
    private String documentId;

    /** 文档标题 */
    private String documentTitle;

    /** 引用片段 */
    private String contentSnippet;

    /** 相似度得分 */
    private Float score;
}
