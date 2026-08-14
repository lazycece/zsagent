package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;

import java.util.List;

/**
 * 解析后的结构化文档（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record ParsedDocument(
        /** 文档标题（从内容/元数据提取） */
        String title,
        /** 完整纯文本内容 */
        String fullText,
        /** 按章节组织的结构 */
        List<Section> sections
) {
}
