package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新 ETL 状态命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class UpdateEtlStatusCommand {

    /** 文档ID */
    private String documentId;
    /** ETL 状态 */
    private EtlStatus status;
    /** 错误信息（仅 FAILED 时有值） */
    private String errorMessage;
}
