package com.lazycece.zsagent.facade.knowledge.result;

import com.lazycece.zsagent.facade.knowledge.dto.DocumentDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文档详情查询结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentDetailResult implements Serializable {

    /** 文档详情 */
    private DocumentDTO document;
}
