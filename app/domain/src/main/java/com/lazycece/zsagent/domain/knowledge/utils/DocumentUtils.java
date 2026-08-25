package com.lazycece.zsagent.domain.knowledge.utils;

import com.lazycece.zsagent.domain.common.utils.FileUtils;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lazycece
 */
public class DocumentUtils {

    /**
     * 根据文件扩展名识别格式。
     */
    public static DocumentFormat detectFormat(String filePath) {

        String suffix = FileUtils.extractFileSuffix(filePath);
        if (StringUtils.isBlank(suffix)) {
            return DocumentFormat.OTHER;
        }
        String ext = suffix.toLowerCase();
        return switch (ext) {
            case "pdf" -> DocumentFormat.PDF;
            case "docx" -> DocumentFormat.DOCX;
            case "md", "markdown" -> DocumentFormat.MD;
            case "html", "htm" -> DocumentFormat.HTML;
            case "txt" -> DocumentFormat.TXT;
            case "xlsx" -> DocumentFormat.XLSX;
            case "pptx" -> DocumentFormat.PPTX;
            case "csv" -> DocumentFormat.CSV;
            default -> DocumentFormat.OTHER;
        };
    }
}
