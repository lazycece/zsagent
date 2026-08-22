package com.lazycece.zsagent.infra.acl.service.parser;

import org.apache.commons.lang3.StringUtils;

/**
 * 解析器公共工具。
 *
 * @author lazycece
 */
final class ParserSupport {

    private ParserSupport() {
    }

    /**
     * 从全文提取标题（首个非空行，截断至 100 字符）。
     */
    static String extractTitle(String fullText) {
        if (StringUtils.isBlank(fullText)) {
            return "";
        }
        String firstLine = fullText.trim().split("\n", 2)[0].trim();
        return firstLine.length() > 100 ? firstLine.substring(0, 100) : firstLine;
    }
}
