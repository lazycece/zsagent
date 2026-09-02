/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lazycece.zsagent.infra.acl.repository;

import com.lazycece.rapidf.domain.anotation.DomainRepository;
import com.lazycece.zsagent.domain.knowledge.model.Directory;
import com.lazycece.zsagent.domain.knowledge.repository.DirectoryRepository;
import com.lazycece.zsagent.infra.acl.converter.DocumentInfraConverter;
import com.lazycece.zsagent.infra.dal.mapper.udf.DirectoryUdfMapper;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 目录仓储 MyBatis 实现。
 *
 * @author lazycece
 */
@DomainRepository
public class DirectoryRepositoryImpl implements DirectoryRepository {

    private final DirectoryUdfMapper directoryMapper;

    public DirectoryRepositoryImpl(DirectoryUdfMapper directoryMapper) {
        this.directoryMapper = directoryMapper;
    }

    @Override
    public void save(Directory directory) {
        directoryMapper.insert(DocumentInfraConverter.toDirectoryPO(directory));
    }

    @Override
    public Directory findByDirectoryId(String directoryId) {
        return DocumentInfraConverter.toDirectory(directoryMapper.selectById(directoryId));
    }

    @Override
    public List<Directory> findByParentId(String parentId) {
        return directoryMapper.selectByParentId(parentId).stream()
                .map(DocumentInfraConverter::toDirectory)
                .collect(Collectors.toList());
    }

    @Override
    public List<Directory> findAll() {
        return directoryMapper.selectAll().stream()
                .map(DocumentInfraConverter::toDirectory)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Directory directory) {
        directoryMapper.update(DocumentInfraConverter.toDirectoryPO(directory));
    }

    @Override
    public int countByParentId(String parentId) {
        return directoryMapper.countByParentId(parentId);
    }
}
