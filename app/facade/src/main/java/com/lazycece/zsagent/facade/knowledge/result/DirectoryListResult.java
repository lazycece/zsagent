package com.lazycece.zsagent.facade.knowledge.result;

import com.lazycece.zsagent.facade.knowledge.dto.DirectoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 目录列表查询结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class DirectoryListResult implements Serializable {

    /** 目录列表（或目录树） */
    private List<DirectoryDTO> directories;
}
