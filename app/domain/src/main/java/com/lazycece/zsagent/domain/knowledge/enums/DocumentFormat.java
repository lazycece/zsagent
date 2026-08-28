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
