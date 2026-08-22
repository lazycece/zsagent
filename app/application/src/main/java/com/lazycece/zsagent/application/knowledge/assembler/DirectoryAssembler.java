package com.lazycece.zsagent.application.knowledge.assembler;

import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDirectoryCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.MoveDirectoryCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.RenameDirectoryCmd;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryCreateRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryMoveRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryRenameRequest;

/**
 * 目录组装器。
 * 负责从请求体构建领域命令对象，命令对象构建逻辑统一收拢于此。
 *
 * @author lazycece
 */
public final class DirectoryAssembler {

    private DirectoryAssembler() {
    }

    /**
     * 从创建请求构建创建目录命令。
     */
    public static CreateDirectoryCmd toCreateDirectoryCmd(DirectoryCreateRequest request) {
        CreateDirectoryCmd command = new CreateDirectoryCmd();
        command.setUserId(request.getUserId());
        command.setParentId(request.getParentId());
        command.setName(request.getName());
        return command;
    }

    /**
     * 从重命名请求构建重命名目录命令。
     */
    public static RenameDirectoryCmd toRenameDirectoryCmd(DirectoryRenameRequest request) {
        RenameDirectoryCmd command = new RenameDirectoryCmd();
        command.setUserId(request.getUserId());
        command.setDirectoryId(request.getDirectoryId());
        command.setNewName(request.getNewName());
        return command;
    }

    /**
     * 从移动请求构建移动目录命令。
     */
    public static MoveDirectoryCmd toMoveDirectoryCmd(DirectoryMoveRequest request) {
        MoveDirectoryCmd command = new MoveDirectoryCmd();
        command.setUserId(request.getUserId());
        command.setDirectoryId(request.getDirectoryId());
        command.setNewParentId(request.getNewParentId());
        return command;
    }
}
