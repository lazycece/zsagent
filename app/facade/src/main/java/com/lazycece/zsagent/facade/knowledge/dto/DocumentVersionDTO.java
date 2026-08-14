package com.lazycece.zsagent.facade.knowledge.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 文档版本 DTO
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentVersionDTO {

    /** 版本唯一标识 */
    private String versionId;

    /** 版本号 */
    private Integer versionNumber;

    /** 文件大小（字节） */
    private Long fileSize;

    /** 变更说明 */
    private String changeLog;

    /** 创建人 */
    private String creator;

    /** 创建时间 */
    private LocalDateTime createTime;
}
