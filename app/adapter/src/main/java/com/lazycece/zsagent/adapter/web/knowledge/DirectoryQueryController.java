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
package com.lazycece.zsagent.adapter.web.knowledge;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.api.DirectoryQueryFacade;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryChildrenQueryRequest;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryListResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 目录查询控制器
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/directory")
public class DirectoryQueryController implements DirectoryQueryFacade {

    private final DirectoryQueryFacade directoryQueryFacade;

    public DirectoryQueryController(DirectoryQueryFacade directoryQueryFacade) {
        this.directoryQueryFacade = directoryQueryFacade;
    }

    /**
     * 查询子目录列表。
     */
    @Override
    @GetMapping("/list-children")
    public RespData<DirectoryListResult> listChildren(
            @Validated DirectoryChildrenQueryRequest request) {
        return directoryQueryFacade.listChildren(request);
    }

    /**
     * 查询完整目录树。
     */
    @Override
    @GetMapping("/tree")
    public RespData<DirectoryListResult> tree() {
        return directoryQueryFacade.tree();
    }
}
