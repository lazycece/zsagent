package com.lazycece.zsagent.domain.knowledge.service.impl;

import com.lazycece.rapidf.domain.anotation.DomainService;
import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.zsagent.domain.knowledge.model.Directory;
import com.lazycece.zsagent.domain.knowledge.repository.DirectoryRepository;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.service.DirectoryDomainService;
import com.lazycece.zsagent.domain.knowledge.valueobject.CreateDirectoryCommand;

import java.time.LocalDateTime;

/**
 * 目录领域服务实现。
 *
 * @author lazycece
 */
@DomainService
public class DirectoryDomainServiceImpl implements DirectoryDomainService {

    private final DirectoryRepository directoryRepository;
    private final DocumentRepository documentRepository;

    public DirectoryDomainServiceImpl(DirectoryRepository directoryRepository,
                                      DocumentRepository documentRepository) {
        this.directoryRepository = directoryRepository;
        this.documentRepository = documentRepository;
    }

    /**
     * 创建目录。
     */
    @Override
    public String createDirectory(CreateDirectoryCommand command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        Assert.notNull(command.userId(), RespStatus.PARAM_ERROR, "userId 不能为 null");
        Assert.notNull(command.name(), RespStatus.PARAM_ERROR, "name 不能为 null");
        Directory directory = Directory.create(command.userId(), command.parentId(), command.name());
        directoryRepository.save(directory);
        return directory.getDirectoryId();
    }

    /**
     * 重命名目录。
     */
    @Override
    public void rename(String userId, String directoryId, String newName) {
        Directory directory = directoryRepository.findByDirectoryId(directoryId);
        Assert.notNull(directory, RespStatus.PARAM_ERROR, "目录不存在");
        Assert.notNull(newName, RespStatus.PARAM_ERROR, "newName 不能为 null");
        directory.setUpdater(userId);
        directory.setUpdateTime(LocalDateTime.now());
        directory.rename(newName);
        directoryRepository.update(directory);
    }

    /**
     * 移动目录。
     */
    @Override
    public void moveTo(String userId, String directoryId, String newParentId) {
        Directory directory = directoryRepository.findByDirectoryId(directoryId);
        Assert.notNull(directory, RespStatus.PARAM_ERROR, "目录不存在");
        directory.setUpdater(userId);
        directory.setUpdateTime(LocalDateTime.now());
        directory.moveTo(newParentId);
        directoryRepository.update(directory);
    }

    /**
     * 删除目录（需先检查无子目录、无关连文档）。
     */
    @Override
    public void delete(String userId, String directoryId) {
        Directory directory = directoryRepository.findByDirectoryId(directoryId);
        Assert.notNull(directory, RespStatus.PARAM_ERROR, "目录不存在");
        int childCount = directoryRepository.countByParentId(directoryId);
        Assert.isTrue(childCount == 0, RespStatus.FAIL, "目录下存在子目录，无法删除");
        int documentCount = documentRepository.countByDirectoryId(directoryId);
        Assert.isTrue(documentCount == 0, RespStatus.FAIL, "目录下存在文档，无法删除");
        directory.setUpdater(userId);
        directory.setUpdateTime(LocalDateTime.now());
        directory.setDeleted(true);
        directoryRepository.update(directory);
    }
}
