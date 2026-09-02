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
package com.lazycece.zsagent.domain.knowledge.utils;

import com.lazycece.zsagent.domain.common.utils.FileUtils;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lazycece
 */
public class DocumentUtils {

    /**
     * 根据文件扩展名识别格式。
     */
    public static DocumentFormat detectFormat(String filePath) {

        String suffix = FileUtils.extractFileSuffix(filePath);
        if (StringUtils.isBlank(suffix)) {
            return DocumentFormat.OTHER;
        }
        String ext = suffix.toLowerCase();
        return switch (ext) {
            case "pdf" -> DocumentFormat.PDF;
            case "docx" -> DocumentFormat.DOCX;
            case "md", "markdown" -> DocumentFormat.MD;
            case "html", "htm" -> DocumentFormat.HTML;
            case "txt" -> DocumentFormat.TXT;
            case "xlsx" -> DocumentFormat.XLSX;
            case "pptx" -> DocumentFormat.PPTX;
            case "csv" -> DocumentFormat.CSV;
            default -> DocumentFormat.OTHER;
        };
    }
}
