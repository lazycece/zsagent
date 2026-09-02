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
package com.lazycece.zsagent.facade.knowledge.api;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryChildrenQueryRequest;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryListResult;

/**
 * 目录查询门面接口
 *
 * @author lazycece
 */
public interface DirectoryQueryFacade {

    /**
     * 查询子目录列表。
     *
     * @param request 子目录查询请求
     * @return 子目录列表
     */
    RespData<DirectoryListResult> listChildren(DirectoryChildrenQueryRequest request);

    /**
     * 查询完整目录树。
     *
     * @return 目录树
     */
    RespData<DirectoryListResult> tree();
}
