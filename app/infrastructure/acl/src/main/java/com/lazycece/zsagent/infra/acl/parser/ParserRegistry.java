package com.lazycece.zsagent.infra.acl.parser;

import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.DocumentParser;
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
public class ParserRegistry {

    private final Map<DocumentFormat, DocumentParser> parserMap;

    public ParserRegistry(List<DocumentParser> parsers) {
        this.parserMap = parsers.stream()
                .collect(Collectors.toMap(DocumentParser::supportedFormat, Function.identity()));
    }

    /**
     * 按格式获取解析器。
     *
     * @param format 文档格式
     * @return 对应解析器
     */
    public DocumentParser getParser(DocumentFormat format) {
        DocumentParser parser = parserMap.get(format);
        if (parser == null) {
            throw ExceptionFactory.businessException("不支持的文件格式: " + format.getCode());
        }
        return parser;
    }
}
