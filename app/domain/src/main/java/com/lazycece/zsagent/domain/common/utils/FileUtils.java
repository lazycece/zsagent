package com.lazycece.zsagent.domain.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lazycece
 */
public class FileUtils {

    /**
     * 从原始文件名提取安全的文件后缀（含点，如 ".pdf"），无有效后缀返回空串。
     */
    public static String extractFileSuffix(String originalFilename) {
        if (StringUtils.isBlank(originalFilename)) {
            return "";
        }
        String name = originalFilename;
        int slash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
        if (slash >= 0) {
            name = name.substring(slash + 1);
        }
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == name.length() - 1) {
            return "";
        }
        String suffix = name.substring(dotIndex).replaceAll("[^A-Za-z0-9._-]", "_");
        return ".".equals(suffix) ? "" : suffix;
    }

    /**
     * 从路径中提取文件名。
     */
    public static String extractFilename(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        int slash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return slash >= 0 ? path.substring(slash + 1) : path;
    }
}
