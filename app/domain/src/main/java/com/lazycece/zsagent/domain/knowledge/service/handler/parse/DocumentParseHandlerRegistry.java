package com.lazycece.zsagent.domain.knowledge.service.handler.parse;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;

/**
 * 文档解析器注册中心（领域 SPI）。
 * 按文档格式路由到对应解析器，由 infrastructure 层提供实现。
 *
 * @author lazycece
 */
public interface DocumentParseHandlerRegistry {

    /**
     * 按格式获取解析器。
     *
     * @param format 文档格式
     * @return 对应解析器
     */
    DocumentParseHandler getParser(DocumentFormat format);
}
