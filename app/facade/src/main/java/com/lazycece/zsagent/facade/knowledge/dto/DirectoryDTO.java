package com.lazycece.zsagent.facade.knowledge.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 目录 DTO
 *
 * @author lazycece
 */
@Getter
@Setter
public class DirectoryDTO {

    /** 目录唯一标识 */
    private String directoryId;

    /** 父目录ID */
    private String parentId;

    /** 目录名称 */
    private String name;

    /** 排序序号 */
    private Integer sortOrder;

    /** 子目录（仅 tree 接口填充） */
    private List<DirectoryDTO> children;

    /** 关联文档数（可选） */
    private Integer documentCount;

    /** 创建时间 */
    private LocalDateTime createTime;
}
