package com.lazycece.zsagent.facade.knowledge.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 创建目录结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class DirectoryCreateResult implements Serializable {

    /** 新建目录 ID */
    private String directoryId;
}
