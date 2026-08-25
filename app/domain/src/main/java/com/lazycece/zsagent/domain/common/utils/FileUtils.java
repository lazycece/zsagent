package com.lazycece.zsagent.domain.common.utils;

import com.lazycece.rapidf.utils.UUIDUtils;
import com.lazycece.rapidf.utils.constants.SymbolConstants;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lazycece
 */
public class FileUtils {


    /**
     * <p>目录格式（upload/yyyy/MM/dd）
     * <p>文件名由 uuid + unix时间戳 + 原始文件名及后缀组成
     */
    public static String assembleFilePath(LocalDateTime now, String originalFilename) {
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        long milliTime = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        String fileName =
                UUIDUtils.uuid() + SymbolConstants.UNDERLINE + milliTime + SymbolConstants.UNDERLINE
                        + originalFilename;
        return "upload/" + datePath + "/" + fileName;
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

    /**
     * 从原始文件名提取安全的文件后缀（如 "pdf"），无有效后缀返回空串。
     */
    public static String extractFileSuffix(String path) {
        String filename = extractFilename(path);
        int dotIndex = filename.lastIndexOf(SymbolConstants.DOT);
        return dotIndex > 0 ? filename.substring(dotIndex + 1).toLowerCase() : "";
    }

    public static String extractOriginalFilename(String path) {
        String filename = extractFilename(path);
        int underlineIndex = filename.lastIndexOf(SymbolConstants.UNDERLINE);
        return underlineIndex > 0 ? filename.substring(underlineIndex + 1) : filename;
    }


}
