package com.lazycece.zsagent.application.knowledge.assembler;

import com.lazycece.rapidf.utils.EnumUtils;
import com.lazycece.zsagent.domain.common.utils.FileUtils;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentStatus;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.utils.DocumentUtils;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDocumentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.RollbackDocumentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentContentCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.UpdateDocumentMetadataCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentListQuery;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentQuery;
import com.lazycece.zsagent.facade.knowledge.request.DocumentCreateRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentDetailQueryRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentListQueryRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRollbackRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateContentRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateMetadataRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
    public static CreateDocumentCmd assembleCreateDocumentCmd(DocumentCreateRequest request) {
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
    public static UpdateDocumentMetadataCmd assembleUpdateMetadataCmd(DocumentUpdateMetadataRequest request) {
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
    public static UpdateDocumentContentCmd assembleUpdateContentCmd(DocumentUpdateContentRequest request) {
        UpdateDocumentContentCmd command = new UpdateDocumentContentCmd();
        command.setUserId(request.getUserId());
        command.setDocumentId(request.getDocumentId());
        command.setFilePath(request.getFilePath());
        command.setChangeLog(request.getChangeLog());
        return command;
    }

    /**
     * 从详情查询请求构建单文档查询条件。
     */
    public static DocumentQuery assembleDocumentQuery(DocumentDetailQueryRequest request, List<String> userDepts) {
        DocumentQuery query = new DocumentQuery();
        query.setUserId(request.getUserId());
        query.setUserDepts(userDepts);
        query.setDocumentId(request.getDocumentId());
        return query;
    }

    /**
     * 从列表查询请求构建文档列表查询条件。
     */
    public static DocumentListQuery assembleDocumentListQuery(DocumentListQueryRequest request, List<String> userDepts) {
        DocumentListQuery query = new DocumentListQuery();
        query.setUserId(request.getUserId());
        query.setUserDepts(userDepts);
        query.setDirectoryId(request.getDirectoryId());
        query.setStatus(StringUtils.isNotBlank(request.getStatus())
                ? EnumUtils.getEnum(DocumentStatus.class, request.getStatus())
                : null);
        query.setKeyword(request.getKeyword());
        return query;
    }

    /**
     * 从回滚请求构建回滚命令。
     */
    public static RollbackDocumentCmd assembleRollbackCmd(DocumentRollbackRequest request) {
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
