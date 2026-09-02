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
package com.lazycece.zsagent.infra.acl.service.parser;

import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.service.acl.DocumentParseHandler;
import com.lazycece.zsagent.domain.knowledge.service.acl.DocumentParseHandlerRegistry;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 解析器注册中心，按文档格式路由到对应解析器。
 *
 * @author lazycece
 */
@Component
public class ParseHandlerRegistry implements DocumentParseHandlerRegistry {

    private final Map<DocumentFormat, DocumentParseHandler> parserMap;

    public ParseHandlerRegistry(List<DocumentParseHandler> parsers) {
        this.parserMap =
                parsers.stream()
                        .collect(
                                Collectors.toMap(
                                        DocumentParseHandler::supportedFormat,
                                        Function.identity()));
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
