package com.lazycece.zsagent.facade.knowledge.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * ETL 状态查询结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class EtlStatusResult implements Serializable {

    /** 文档ID */
    private String documentId;

    /** ETL 状态 */
    private String etlStatus;

    /** 错误信息（仅 FAILED 时有值） */
    private String errorMessage;

    /** 文档状态 */
    private String documentStatus;
}
