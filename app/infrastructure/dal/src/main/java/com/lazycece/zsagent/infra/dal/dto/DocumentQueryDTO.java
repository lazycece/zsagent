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
package com.lazycece.zsagent.infra.dal.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 文档查询参数 DTO（数据库访问层）
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentQueryDTO {

    /** 操作主体标识 */
    private String userId;
    /** 用户所属部门列表 */
    private List<String> userDepts;
    /** 目标文档ID */
    private String documentId;
    /** 按目录过滤（可空） */
    private String directoryId;
    /** 按状态过滤（可空） */
    private String status;
    /** 标题/标签模糊搜索（可空） */
    private String keyword;

    /**
     * 构建查询 DTO。
     *
     * @param userId      操作主体标识
     * @param userDepts   用户所属部门列表
     * @param documentId  目标文档ID（可空）
     * @param directoryId 按目录过滤（可空）
     * @param status      按状态过滤（可空）
     * @param keyword     标题/标签模糊搜索（可空）
     * @return 查询 DTO
     */
    public static DocumentQueryDTO build(
            String userId,
            List<String> userDepts,
            String documentId,
            String directoryId,
            String status,
            String keyword) {
        DocumentQueryDTO dto = new DocumentQueryDTO();
        dto.setUserId(userId);
        dto.setUserDepts(userDepts);
        dto.setDocumentId(documentId);
        dto.setDirectoryId(directoryId);
        dto.setStatus(status);
        dto.setKeyword(keyword);
        return dto;
    }
}
