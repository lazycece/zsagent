package com.lazycece.zsagent.domain.knowledge.valueobject.cmd;

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
public class UpdateEtlStatusCmd {

    /** 文档ID */
    private String documentId;
    /** ETL 状态 */
    private EtlStatus status;
    /** 错误信息（仅 FAILED 时有值） */
    private String errorMessage;

    public static UpdateEtlStatusCmd build(String documentId, EtlStatus status, String errorMessage){
        UpdateEtlStatusCmd cmd = new UpdateEtlStatusCmd();
        cmd.setDocumentId(documentId);
        cmd.setStatus(status);
        cmd.setErrorMessage(errorMessage);
        return cmd;
    }
}
