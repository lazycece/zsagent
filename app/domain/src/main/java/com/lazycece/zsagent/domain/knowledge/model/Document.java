package com.lazycece.zsagent.domain.knowledge.model;

import com.google.common.collect.Lists;
import com.lazycece.cell.specification.CellHelper;
import com.lazycece.rapidf.domain.anotation.DomainAggregate;
import com.lazycece.rapidf.domain.model.Aggregate;
import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.zsagent.domain.common.enums.CellEnum;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentStatus;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDocumentCmd;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档聚合根
 *
 * @author lazycece
 */
@Getter
@Setter
@DomainAggregate
public class Document extends Aggregate<String> {

    /** 文档唯一标识 */
    private String documentId;
    /** 文档标题 */
    private String title;
    /** 自动生成摘要 */
    private String summary;
    /** 文件格式 */
    private DocumentFormat format;
    /** 当前版本文件大小（字节） */
    private Long fileSize;
    /** 当前版本文件存储路径 */
    private String filePath;
    /** 所属目录ID */
    private String directoryId;
    /** 标签列表 */
    private List<String> tags = new ArrayList<>();
    /** 可见范围 */
    private Visibility visibility;
    /** 可见对象列表（用户ID或部门ID） */
    private List<String> visibleTo = new ArrayList<>();
    /** 文档状态 */
    private DocumentStatus status;
    /** ETL 处理状态 */
    private EtlStatus etlStatus;
    /** ETL 错误信息 */
    private String etlErrorMessage;
    /** 当前版本号 */
    private Integer currentVersion;
    /** 版本历史列表 */
    private List<DocumentVersion> versions = new ArrayList<>();
    /** 删除时间（回收站 30 天计时） */
    private LocalDateTime deletedTime;

    @Override
    public String getId() {
        return this.documentId;
    }

    // ======================== 工厂方法 ========================

    /**
     * 创建文档聚合根。
     * 初始版本由调用方通过 {@link #createNewVersion(String, Long, String)} 创建。
     */
    public static Document create(CreateDocumentCmd command) {
        Document document = new Document();
        document.documentId = CellHelper.getInstance().generateId(CellEnum.DOCUMENT);
        document.title = command.getTitle();
        document.format = command.getFormat();
        document.directoryId = command.getDirectoryId();
        document.tags = DefaultUtils.defaultList(command.getTags());
        document.visibility = command.getVisibility();
        document.visibleTo = DefaultUtils.defaultList(command.getVisibleTo());
        document.status = DocumentStatus.DRAFT;
        document.etlStatus = EtlStatus.PENDING;
        document.currentVersion = 0;
        document.versions = Lists.newArrayList();
        document.setCreator(command.getUserId());
        document.setUpdater(command.getUserId());
        document.setCreateTime(LocalDateTime.now());
        document.setUpdateTime(LocalDateTime.now());
        document.setDeleted(false);
        return document;
    }

    // ======================== 行为方法 ========================

    /**
     * 发布文档——ETL 完成后调用，将状态从 DRAFT 转为 PUBLISHED。
     */
    public void publish() {
        this.etlStatus = EtlStatus.COMPLETED;
        this.status = DocumentStatus.PUBLISHED;
    }

    /**
     * 归档文档。
     */
    public void archive() {
        this.status = DocumentStatus.ARCHIVED;
    }

    /**
     * 删除文档——移入回收站，记录删除时间用于 30 天自动清理。
     */
    public void delete() {
        this.status = DocumentStatus.DELETED;
        this.deletedTime = LocalDateTime.now();
    }

    /**
     * 从回收站恢复文档。
     */
    public void restore() {
        this.status = DocumentStatus.PUBLISHED;
        this.deletedTime = null;
    }

    /**
     * 创建新版本——仅当文件内容变更时调用。
     * 同时更新当前版本号与当前文件路径。
     *
     * @param filePath  新版本文件存储路径
     * @param fileSize  新版本文件大小
     * @param changeLog 变更说明
     * @return 新创建的版本
     */
    public DocumentVersion createNewVersion(String filePath, Long fileSize, String changeLog) {
        this.currentVersion = this.currentVersion + 1;
        this.filePath = filePath;
        this.fileSize = fileSize;
        DocumentVersion version = DocumentVersion.create(
                this.documentId, this.currentVersion, filePath, fileSize, changeLog, this.getUpdater());
        this.versions.add(version);
        return version;
    }

    /**
     * 更新元数据——标题、标签、目录、可见范围，不产生新版本。
     */
    public void updateMetadata(String title, String summary, String directoryId,
                               List<String> tags, Visibility visibility, List<String> visibleTo) {
        if (StringUtils.isNotBlank(title)) {
            this.title = title;
        }
        if (summary != null) {
            this.summary = summary;
        }
        if (directoryId != null) {
            this.directoryId = directoryId;
        }
        if (tags != null) {
            this.tags = new ArrayList<>(tags);
        }
        if (visibility != null) {
            this.visibility = visibility;
        }
        if (visibleTo != null) {
            this.visibleTo = new ArrayList<>(visibleTo);
        }
    }

    /**
     * 更新 ETL 状态。
     */
    public void updateEtlStatus(EtlStatus status) {
        this.etlStatus = status;
    }

    /**
     * 标记 ETL 失败。
     */
    public void markEtlFailed(String errorMessage) {
        this.etlStatus = EtlStatus.FAILED;
        this.etlErrorMessage = errorMessage;
    }
}
