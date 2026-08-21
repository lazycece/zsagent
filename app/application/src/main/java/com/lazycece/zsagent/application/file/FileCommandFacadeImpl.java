package com.lazycece.zsagent.application.file;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.rapidf.utils.UUIDUtils;
import com.lazycece.zsagent.domain.knowledge.repository.FileStorage;
import com.lazycece.zsagent.facade.file.api.FileCommandFacade;
import com.lazycece.zsagent.facade.file.request.FileUploadRequest;
import com.lazycece.zsagent.facade.file.result.FileUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 文件命令门面实现。
 * 负责文件上传编排：参数校验、文件命名、日期目录路径构建、落盘存储。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class FileCommandFacadeImpl implements FileCommandFacade {

    /**
     * 日期目录格式（upload/yyyy/MM/dd）
     */
    private static final DateTimeFormatter DATE_PATH_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final FileStorage fileStorage;

    public FileCommandFacadeImpl(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Override
    public RespData<FileUploadResult> upload(FileUploadRequest request) {
        Assert.notNull(request, RespStatus.PARAM_ERROR, "上传请求不能为空");
        Assert.notNull(request.getFile(), RespStatus.PARAM_ERROR, "文件不能为空");
        Assert.isTrue(!request.getFile().isEmpty(), RespStatus.PARAM_ERROR, "文件内容不能为空");

        LocalDateTime now = LocalDateTime.now();
        String filePath = buildFilePath(now, extractFileSuffix(request.getFile().getOriginalFilename()));
        try (InputStream inputStream = request.getFile().getInputStream()) {
            fileStorage.store(filePath, inputStream);
        } catch (IOException e) {
            throw ExceptionFactory.businessException("文件存储失败", e);
        }

        FileUploadResult result = new FileUploadResult();
        result.setFilePath(filePath);
        return RespData.success(result);
    }

    /**
     * 构建存储路径：目录按日期分割（yyyy/MM/dd），文件名由 uuid + unix时间戳 + 原始文件后缀组成。
     */
    private String buildFilePath(LocalDateTime now, String fileSuffix) {
        String datePath = now.format(DATE_PATH_FORMAT);
        String fileName = UUIDUtils.uuid() + "_" + now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + fileSuffix;
        return "upload/" + datePath + "/" + fileName;
    }

    /**
     * 从原始文件名提取安全的文件后缀（含点，如 ".pdf"），无有效后缀返回空串。
     */
    private String extractFileSuffix(String originalFilename) {
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
}
