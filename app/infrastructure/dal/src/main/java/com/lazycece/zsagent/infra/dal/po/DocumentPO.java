package com.lazycece.zsagent.infra.dal.po;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * agent_document 表 PO
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentPO {

    /** 主键 */
    private Long id;
    /** 文档唯一标识 */
    private String documentId;
    /** 文档标题 */
    private String title;
    /** 自动生成摘要 */
    private String summary;
    /** 文件格式 */
    private String format;
    /** 当前版本文件大小（字节） */
    private Long fileSize;
    /** 当前版本文件存储路径 */
    private String filePath;
    /** 所属目录ID */
    private String directoryId;
    /** 标签列表 JSON 数组 */
    private String tags;
    /** 可见范围 */
    private String visibility;
    /** 可见对象列表 JSON 数组 */
    private String visibleTo;
    /** 文档状态 */
    private String status;
    /** ETL 处理状态 */
    private String etlStatus;
    /** ETL 失败原因 */
    private String etlErrorMsg;
    /** 当前版本号 */
    private Integer currentVersion;
    /** 删除时间（回收站 30 天计时） */
    private LocalDateTime deletedTime;
    /** 创建人 */
    private String creator;
    /** 更新人 */
    private String updater;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 逻辑删除 */
    private Boolean deleted;
}
