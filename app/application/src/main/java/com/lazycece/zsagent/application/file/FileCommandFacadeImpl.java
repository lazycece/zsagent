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

/**
 * 文件命令门面实现。
 * 负责文件上传编排：参数校验、文件名清理、路径构建、落盘存储。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class FileCommandFacadeImpl implements FileCommandFacade {

    private final FileStorage fileStorage;

    public FileCommandFacadeImpl(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Override
    public RespData<FileUploadResult> upload(FileUploadRequest request) {
        Assert.notNull(request, RespStatus.PARAM_ERROR, "上传请求不能为空");
        Assert.notNull(request.getFile(), RespStatus.PARAM_ERROR, "文件不能为空");
        Assert.isTrue(!request.getFile().isEmpty(), RespStatus.PARAM_ERROR, "文件内容不能为空");

        String filePath = "upload/" + UUIDUtils.uuid() + "/" + sanitizeFilename(request.getFile().getOriginalFilename());
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
     * 清理文件名，仅保留安全字符，避免路径穿越。
     */
    private String sanitizeFilename(String filename) {
        if (StringUtils.isBlank(filename)) {
            return "file";
        }
        String name = filename;
        int slash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
        if (slash >= 0) {
            name = name.substring(slash + 1);
        }
        String safe = name.replaceAll("[^A-Za-z0-9._-]", "_");
        return StringUtils.isBlank(safe) ? "file" : safe;
    }
}
