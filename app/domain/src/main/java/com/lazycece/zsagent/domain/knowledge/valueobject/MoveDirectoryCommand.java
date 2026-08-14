package com.lazycece.zsagent.domain.knowledge.valueobject;

import lombok.Getter;
import lombok.Setter;

/**
 * 移动目录命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class MoveDirectoryCommand {

    /** 操作主体标识 */
    private String userId;
    /** 目录ID */
    private String directoryId;
    /** 新父目录ID */
    private String newParentId;
}
