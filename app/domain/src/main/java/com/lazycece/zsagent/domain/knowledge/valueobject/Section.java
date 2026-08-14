package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

/**
 * 文档章节（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record Section(
        /** 标题层级：0=无标题, 1=H1, 2=H2... */
        int headingLevel,
        /** 标题文字 */
        String headingText,
        /** 该章节的文本内容 */
        String content,
        /** 起始页码（PDF 专有，其他格式置 0） */
        int pageNumber,
        /** 在全文中的字符偏移量 */
        int charOffset
) {
}
