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
package com.lazycece.zsagent.application.knowledge.converter;

import com.lazycece.zsagent.domain.knowledge.model.Directory;
import com.lazycece.zsagent.facade.knowledge.dto.DirectoryDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * 目录数据转换器，负责将领域对象转换为 Facade DTO。
 *
 * @author lazycece
 */
public final class DirectoryConverter {

    private DirectoryConverter() {}

    /**
     * 目录聚合根 → DirectoryDTO。
     */
    public static DirectoryDTO toDirectoryDTO(Directory directory) {
        if (directory == null) {
            return null;
        }
        DirectoryDTO dto = new DirectoryDTO();
        dto.setDirectoryId(directory.getDirectoryId());
        dto.setParentId(directory.getParentId());
        dto.setName(directory.getName());
        dto.setSortOrder(directory.getSortOrder());
        dto.setCreator(directory.getCreator());
        dto.setUpdater(directory.getUpdater());
        dto.setCreateTime(directory.getCreateTime());
        dto.setUpdateTime(directory.getUpdateTime());
        return dto;
    }

    /**
     * 目录列表 → DirectoryDTO 列表。
     */
    public static List<DirectoryDTO> toDirectoryDTOList(List<Directory> directories) {
        if (directories == null) {
            return Collections.emptyList();
        }
        return directories.stream()
                .map(DirectoryConverter::toDirectoryDTO)
                .collect(Collectors.toList());
    }

    /**
     * 目录列表 → 目录树（按 parentId 递归组装）。
     */
    public static List<DirectoryDTO> toDirectoryTree(List<Directory> allDirectories) {
        if (allDirectories == null || allDirectories.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, DirectoryDTO> dtoMap = new HashMap<>();
        for (Directory directory : allDirectories) {
            dtoMap.put(directory.getDirectoryId(), toDirectoryDTO(directory));
        }

        List<DirectoryDTO> roots = new ArrayList<>();
        for (DirectoryDTO dto : dtoMap.values()) {
            if (StringUtils.isBlank(dto.getParentId()) || !dtoMap.containsKey(dto.getParentId())) {
                roots.add(dto);
            } else {
                DirectoryDTO parent = dtoMap.get(dto.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(dto);
            }
        }
        return roots;
    }
}
