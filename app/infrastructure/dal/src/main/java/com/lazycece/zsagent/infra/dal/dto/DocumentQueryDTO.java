package com.lazycece.zsagent.infra.dal.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 文档查询参数 DTO（数据库访问层）
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentQueryDTO {

    /** 操作主体标识 */
    private String userId;
    /** 用户所属部门列表 */
    private List<String> userDepts;
    /** 目标文档ID */
    private String documentId;
    /** 按目录过滤（可空） */
    private String directoryId;
    /** 按状态过滤（可空） */
    private String status;
    /** 标题/标签模糊搜索（可空） */
    private String keyword;
}
