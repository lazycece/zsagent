package com.lazycece.zsagent.infra.dal.po;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * agent_directory 表 PO
 *
 * @author lazycece
 */
@Getter
@Setter
public class DirectoryPO {

    /** 主键 */
    private Long id;
    /** 目录唯一标识 */
    private String directoryId;
    /** 父目录ID */
    private String parentId;
    /** 目录名称 */
    private String name;
    /** 排序序号 */
    private Integer sortOrder;
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
