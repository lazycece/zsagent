package com.lazycece.zsagent.domain.knowledge.valueobject;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建目录命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class CreateDirectoryCommand {

    /** 操作主体标识 */
    private String userId;
    /** 父目录ID（可空，根目录） */
    private String parentId;
    /** 目录名称 */
    private String name;
}
