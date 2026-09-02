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
package com.lazycece.zsagent.application.knowledge;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.application.knowledge.converter.DirectoryConverter;
import com.lazycece.zsagent.domain.knowledge.model.Directory;
import com.lazycece.zsagent.domain.knowledge.repository.DirectoryRepository;
import com.lazycece.zsagent.facade.knowledge.api.DirectoryQueryFacade;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryChildrenQueryRequest;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryListResult;
import java.util.List;
import org.springframework.context.annotation.Primary;

/**
 * 目录查询门面实现。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class DirectoryQueryFacadeImpl implements DirectoryQueryFacade {

    private final DirectoryRepository directoryRepository;

    public DirectoryQueryFacadeImpl(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    @Override
    public RespData<DirectoryListResult> listChildren(DirectoryChildrenQueryRequest request) {
        List<Directory> directories = directoryRepository.findByParentId(request.getParentId());
        DirectoryListResult result = new DirectoryListResult();
        result.setDirectories(DirectoryConverter.toDirectoryDTOList(directories));
        return RespData.success(result);
    }

    @Override
    public RespData<DirectoryListResult> tree() {
        List<Directory> directories = directoryRepository.findAll();
        DirectoryListResult result = new DirectoryListResult();
        result.setDirectories(DirectoryConverter.toDirectoryTree(directories));
        return RespData.success(result);
    }
}
