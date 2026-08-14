package com.lazycece.zsagent.domain.knowledge.valueobject;

import lombok.Getter;
import lombok.Setter;

/**
 * 重命名目录命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class RenameDirectoryCommand {

    /** 操作主体标识 */
    private String userId;
    /** 目录ID */
    private String directoryId;
    /** 新名称 */
    private String newName;
}
