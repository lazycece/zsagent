package com.lazycece.zsagent.domain.knowledge.service.acl;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import java.io.InputStream;
import java.util.List;
import org.springframework.ai.document.Document;

/**
 * 文档解析器接口（领域 SPI）。 由 infrastructure 层提供按格式的多态实现。
 *
 * @author lazycece
 */
public interface DocumentParseHandler {

    /**
     * 支持的文档格式。
     *
     * @return 文档格式
     */
    DocumentFormat supportedFormat();

    /**
     * 解析文档，提取结构化内容。 解析器不负责关闭输入流，由调用方管理其生命周期。
     *
     * @param inputStream 文档内容输入流
     * @return 结构化文档内容
     */
    List<Document> parse(InputStream inputStream);
}
