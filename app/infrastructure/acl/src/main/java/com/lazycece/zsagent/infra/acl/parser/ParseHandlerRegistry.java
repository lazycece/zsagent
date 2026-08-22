package com.lazycece.zsagent.infra.acl.parser;

import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.handler.parse.DocumentParseHandler;
import com.lazycece.zsagent.domain.knowledge.service.handler.parse.DocumentParseHandlerRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 解析器注册中心，按文档格式路由到对应解析器。
 *
 * @author lazycece
 */
@Component
public class ParseHandlerRegistry implements DocumentParseHandlerRegistry {

    private final Map<DocumentFormat, DocumentParseHandler> parserMap;

    public ParseHandlerRegistry(List<DocumentParseHandler> parsers) {
        this.parserMap = parsers.stream()
                .collect(Collectors.toMap(DocumentParseHandler::supportedFormat, Function.identity()));
    }

    @Override
    public DocumentParseHandler getParser(DocumentFormat format) {
        DocumentParseHandler parser = parserMap.get(format);
        if (parser == null) {
            throw ExceptionFactory.businessException("不支持的文件格式: " + format.getCode());
        }
        return parser;
    }
}
