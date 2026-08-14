package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 文档列表查询条件
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentListQuery {

    /** 操作主体标识 */
    private String userId;
    /** 用户所属部门列表 */
    private List<String> userDepts;
    /** 按目录过滤（可空） */
    private String directoryId;
    /** 按状态过滤（可空） */
    private DocumentStatus status;
    /** 标题/标签模糊搜索（可空） */
    private String keyword;
}
