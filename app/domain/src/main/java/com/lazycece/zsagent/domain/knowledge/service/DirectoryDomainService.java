package com.lazycece.zsagent.domain.knowledge.service;

import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.CreateDirectoryCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.MoveDirectoryCmd;
import com.lazycece.zsagent.domain.knowledge.valueobject.cmd.RenameDirectoryCmd;

/**
 * 目录领域服务接口
 *
 * @author lazycece
 */
public interface DirectoryDomainService {

    /**
     * 创建目录。
     *
     * @param command 创建目录命令
     * @return directoryId
     */
    String createDirectory(CreateDirectoryCmd command);

    /**
     * 重命名目录。
     *
     * @param command 重命名目录命令
     */
    void rename(RenameDirectoryCmd command);

    /**
     * 移动目录（改变父级）。
     *
     * @param command 移动目录命令
     */
    void moveTo(MoveDirectoryCmd command);

    /**
     * 删除目录（需先检查无子目录、无关连文档）。
     *
     * @param command 删除目录命令
     */
    void delete(String userId, String directoryId);
}
