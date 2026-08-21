package com.lazycece.zsagent.facade.knowledge.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文档创建结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentCreateResult implements Serializable {

    /** 新建文档 ID */
    private String documentId;

    /** 初始 ETL 状态 */
    private String etlStatus;
}
