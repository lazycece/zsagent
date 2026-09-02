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
 * 文档格式枚举
 *
 * @author lazycece
 */
@Getter
@AllArgsConstructor
public enum DocumentFormat implements BaseEnum<String> {
    PDF("pdf", "PDF"),
    DOCX("docx", "Word 文档"),
    MD("md", "Markdown"),
    HTML("html", "HTML"),
    TXT("txt", "纯文本"),
    XLSX("xlsx", "Excel 表格"),
    PPTX("pptx", "PowerPoint"),
    CSV("csv", "CSV"),
    IMAGE("image", "图片"),
    OTHER("other", "其他"),
    ;

    private final String code;
    private final String desc;
}
