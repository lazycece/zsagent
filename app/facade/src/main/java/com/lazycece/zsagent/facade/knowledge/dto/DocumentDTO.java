package com.lazycece.zsagent.facade.knowledge.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档 DTO
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentDTO {

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

    /** 所属目录ID */
    private String directoryId;

    /** 目录名称（JOIN 查询填充） */
    private String directoryName;

    /** 标签列表 */
    private List<String> tags;

    /** 可见范围 */
    private String visibility;

    /** 文档状态 */
    private String status;

    /** ETL 处理状态 */
    private String etlStatus;

    /** ETL 错误信息（仅 FAILED 时有值） */
    private String etlErrorMessage;

    /** 当前版本号 */
    private Integer currentVersion;

    /** 创建人 */
    private String creator;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
