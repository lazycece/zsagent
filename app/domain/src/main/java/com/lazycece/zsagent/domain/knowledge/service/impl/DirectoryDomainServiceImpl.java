package com.lazycece.zsagent.domain.knowledge.service.impl;

import com.lazycece.rapidf.domain.anotation.DomainService;
import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.zsagent.domain.knowledge.model.Directory;
import com.lazycece.zsagent.domain.knowledge.repository.DirectoryRepository;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.service.DirectoryDomainService;
import com.lazycece.zsagent.domain.knowledge.valueobject.CreateDirectoryCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.MoveDirectoryCommand;
import com.lazycece.zsagent.domain.knowledge.valueobject.RenameDirectoryCommand;

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
        Assert.notBlank(command.getName(), RespStatus.PARAM_ERROR, "name 不能为空");
        Directory directory = Directory.create(command.getUserId(), command.getParentId(), command.getName());
        directoryRepository.save(directory);
        return directory.getDirectoryId();
    }

    /**
     * 重命名目录。
     */
    @Override
    public void rename(RenameDirectoryCommand command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        Assert.notBlank(command.getNewName(), RespStatus.PARAM_ERROR, "newName 不能为空");
        Directory directory = directoryRepository.findByDirectoryId(command.getDirectoryId());
        Assert.notNull(directory, RespStatus.PARAM_ERROR, "目录不存在");
        directory.setUpdater(command.getUserId());
        directory.setUpdateTime(LocalDateTime.now());
        directory.rename(command.getNewName());
        directoryRepository.update(directory);
    }

    /**
     * 移动目录。
     */
    @Override
    public void moveTo(MoveDirectoryCommand command) {
        Assert.notNull(command, RespStatus.PARAM_ERROR, "command 不能为 null");
        Directory directory = directoryRepository.findByDirectoryId(command.getDirectoryId());
        Assert.notNull(directory, RespStatus.PARAM_ERROR, "目录不存在");
        directory.setUpdater(command.getUserId());
        directory.setUpdateTime(LocalDateTime.now());
        directory.moveTo(command.getNewParentId());
        directoryRepository.update(directory);
    }

    /**
     * 删除目录（需先检查无子目录、无关连文档）。
     */
    @Override
    public void delete(String userId, String directoryId) {
        Assert.notBlank(userId, RespStatus.PARAM_ERROR, "userId 不能为空");
        Assert.notBlank(directoryId, RespStatus.PARAM_ERROR, "directoryId 不能为空");
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
