package com.lazycece.zsagent.facade.knowledge.result;

import com.lazycece.zsagent.facade.knowledge.dto.DocumentVersionDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 文档版本历史查询结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentVersionListResult implements Serializable {

    /** 版本列表 */
    private List<DocumentVersionDTO> versions;
}
