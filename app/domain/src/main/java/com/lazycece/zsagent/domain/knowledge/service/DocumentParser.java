package com.lazycece.zsagent.domain.knowledge.service;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;

import java.io.IOException;

/**
 * 文档解析器接口（领域 SPI）。
 * 由 infrastructure 层提供按格式的多态实现。
 *
 * @author lazycece
 */
public interface DocumentParser {

    /**
     * 解析文档，提取结构化内容。
     *
     * @param filePath 文件存储路径（通过 FileStorage 读取）
     * @return 结构化文档内容
     * @throws IOException IO 异常
     */
    ParsedDocument parse(String filePath) throws IOException;

    /**
     * 支持的文档格式。
     *
     * @return 文档格式
     */
    DocumentFormat supportedFormat();
}
