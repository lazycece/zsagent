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

import com.lazycece.zsagent.infra.dal.po.DirectoryPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * agent_directory 表用户自定义 Mapper
 *
 * @author lazycece
 */
@Mapper
public interface DirectoryUdfMapper {

    /**
     * 插入目录记录。
     */
    int insert(DirectoryPO directory);

    /**
     * 按 directoryId 查询。
     */
    DirectoryPO selectById(@Param("directoryId") String directoryId);

    /**
     * 查询指定父目录下的所有子目录。
     */
    List<DirectoryPO> selectByParentId(@Param("parentId") String parentId);

    /**
     * 查询所有目录。
     */
    List<DirectoryPO> selectAll();

    /**
     * 更新目录。
     */
    int update(DirectoryPO directory);

    /**
     * 统计子目录数。
     */
    int countByParentId(@Param("parentId") String parentId);
}
