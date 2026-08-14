package com.lazycece.zsagent.infra.dal.po;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * agent_document_version 表 PO
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentVersionPO {

    /** 主键 */
    private Long id;
    /** 版本唯一标识 */
    private String versionId;
    /** 所属文档ID */
    private String documentId;
    /** 版本号 */
    private Integer versionNumber;
    /** 该版本文件存储路径 */
    private String filePath;
    /** 该版本文件大小（字节） */
    private Long fileSize;
    /** 变更说明 */
    private String changeLog;
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
