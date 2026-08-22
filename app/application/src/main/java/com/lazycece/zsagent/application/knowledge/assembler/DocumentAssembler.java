package com.lazycece.zsagent.application.knowledge.assembler;

import com.lazycece.rapidf.utils.EnumUtils;
import com.lazycece.zsagent.domain.common.utils.FileUtils;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.utils.DocumentUtils;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDocumentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.RollbackDocumentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentContentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentMetadataCmd;
import com.lazycece.zsagent.facade.knowledge.request.DocumentCreateRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRollbackRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateContentRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateMetadataRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * 文档组装器。
 * 负责从请求体构建领域命令对象：格式检测、标题推导、枚举转换等组装逻辑统一收拢于此。
 *
 * @author lazycece
 */
public final class DocumentAssembler {

    private DocumentAssembler() {
    }

    /**
     * 从创建请求构建创建文档命令。
     * 标题为空时取文件名（去扩展名）作为默认标题。
     */
    public static CreateDocumentCmd toCreateDocumentCmd(DocumentCreateRequest request) {
        CreateDocumentCmd command = new CreateDocumentCmd();
        command.setUserId(request.getUserId());
        command.setTitle(StringUtils.isNotBlank(request.getTitle())
                ? request.getTitle()
                : extractFileNameWithoutExtension(request.getFilePath()));
        command.setFormat(DocumentUtils.detectFormat(request.getFilePath()));
        command.setFilePath(request.getFilePath());
        command.setDirectoryId(request.getDirectoryId());
        command.setTags(request.getTags());
        command.setVisibility(EnumUtils.getEnum(Visibility.class, request.getVisibility()));
        command.setVisibleTo(request.getVisibleTo());
        return command;
    }

    /**
     * 从更新元数据请求构建更新元数据命令。
     */
    public static UpdateDocumentMetadataCmd toUpdateMetadataCmd(DocumentUpdateMetadataRequest request) {
        UpdateDocumentMetadataCmd command = new UpdateDocumentMetadataCmd();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setTitle(request.getTitle());
        command.setSummary(request.getSummary());
        command.setDirectoryId(request.getDirectoryId());
        command.setTags(request.getTags());
        command.setVisibility(StringUtils.isNotBlank(request.getVisibility())
                ? EnumUtils.getEnum(Visibility.class, request.getVisibility())
                : null);
        command.setVisibleTo(request.getVisibleTo());
        return command;
    }

    /**
     * 从更新内容请求构建更新内容命令。
     */
    public static UpdateDocumentContentCmd toUpdateContentCmd(DocumentUpdateContentRequest request) {
        UpdateDocumentContentCmd command = new UpdateDocumentContentCmd();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setFilePath(request.getFilePath());
        command.setChangeLog(request.getChangeLog());
        return command;
    }

    /**
     * 从回滚请求构建回滚命令。
     */
    public static RollbackDocumentCmd toRollbackCmd(DocumentRollbackRequest request) {
        RollbackDocumentCmd command = new RollbackDocumentCmd();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setTargetVersionId(request.getTargetVersionId());
        return command;
    }

    /**
     * 从路径提取文件名（去除扩展名），用作默认标题。
     */
    private static String extractFileNameWithoutExtension(String filePath) {
        String filename = FileUtils.extractFilename(filePath);
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(0, dotIndex) : filename;
    }
}
