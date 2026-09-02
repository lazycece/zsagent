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

import com.lazycece.zsagent.infra.dal.po.DocumentVersionPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * agent_document_version 表用户自定义 Mapper
 *
 * @author lazycece
 */
@Mapper
public interface DocumentVersionUdfMapper {

    /**
     * 批量插入版本记录。
     */
    int insertBatch(List<DocumentVersionPO> versions);

    /**
     * 按 documentId 查询所有版本（按版本号倒序）。
     */
    List<DocumentVersionPO> selectByDocumentId(@Param("documentId") String documentId);

    /**
     * 按 versionId 查询单个版本。
     */
    DocumentVersionPO selectByVersionId(@Param("versionId") String versionId);
}
