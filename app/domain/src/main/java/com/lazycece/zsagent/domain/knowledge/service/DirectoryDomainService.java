package com.lazycece.zsagent.domain.knowledge.service;

import com.lazycece.zsagent.domain.knowledge.valueobject.CreateDirectoryCommand;

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
    String createDirectory(CreateDirectoryCommand command);

    /**
     * 重命名目录。
     *
     * @param userId      操作者
     * @param directoryId 目录ID
     * @param newName     新名称
     */
    void rename(String userId, String directoryId, String newName);

    /**
     * 移动目录（改变父级）。
     *
     * @param userId      操作者
     * @param directoryId 目录ID
     * @param newParentId 新父目录ID
     */
    void moveTo(String userId, String directoryId, String newParentId);

    /**
     * 删除目录（需先检查无子目录、无关连文档）。
     *
     * @param userId      操作者
     * @param directoryId 目录ID
     */
    void delete(String userId, String directoryId);
}
