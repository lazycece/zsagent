package com.lazycece.zsagent.domain.knowledge.valueobject.cmd;

import lombok.Getter;
import lombok.Setter;

/**
 * 回滚文档命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class RollbackDocumentCmd {

    /** 操作主体标识 */
    private String userId;
    /** 文档ID */
    private String documentId;
    /** 目标版本ID */
    private String targetVersionId;
}
