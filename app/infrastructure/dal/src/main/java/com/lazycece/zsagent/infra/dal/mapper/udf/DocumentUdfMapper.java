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
package com.lazycece.zsagent.infra.dal.mapper.udf;

import com.lazycece.zsagent.infra.dal.dto.DocumentQueryDTO;
import com.lazycece.zsagent.infra.dal.po.DocumentPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * agent_document 表用户自定义 Mapper
 *
 * @author lazycece
 */
@Mapper
public interface DocumentUdfMapper {

    /**
     * 插入文档记录。
     */
    int insert(DocumentPO document);

    /**
     * 按 documentId 查询（无权限过滤，内部使用）。
     */
    DocumentPO selectById(@Param("documentId") String documentId);

    /**
     * 按 documentId 查询（含权限过滤）。
     */
    DocumentPO selectByDocumentId(@Param("query") DocumentQueryDTO query);

    /**
     * 分页查询文档列表（含权限过滤与筛选条件）。
     */
    List<DocumentPO> selectByUserId(
            @Param("query") DocumentQueryDTO query,
            @Param("offset") int offset,
            @Param("size") int size);

    /**
     * 统计文档数（含权限过滤与筛选条件）。
     */
    long countByUserId(@Param("query") DocumentQueryDTO query);

    /**
     * 更新文档。
     */
    int update(DocumentPO document);

    /**
     * 按目录统计文档数。
     */
    int countByDirectoryId(@Param("directoryId") String directoryId);
}
