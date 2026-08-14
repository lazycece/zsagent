package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.rapidf.domain.anotation.ValueObject;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;

/**
 * 更新 ETL 状态命令（值对象）
 *
 * @author lazycece
 */
@ValueObject
public record UpdateEtlStatusCommand(
        /** 文档ID */
        String documentId,
        /** ETL 状态 */
        EtlStatus status,
        /** 错误信息（仅 FAILED 时有值） */
        String errorMessage
) {
}
