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
package com.lazycece.zsagent.domain.knowledge.model;

import com.lazycece.cell.specification.CellHelper;
import com.lazycece.rapidf.domain.anotation.DomainAggregate;
import com.lazycece.rapidf.domain.model.Aggregate;
import com.lazycece.zsagent.domain.common.enums.CellEnum;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 目录聚合根
 *
 * @author lazycece
 */
@Getter
@Setter
@DomainAggregate
public class Directory extends Aggregate<String> {

    /**
     * 目录唯一标识
     */
    private String directoryId;
    /**
     * 父目录ID（根目录为 null）
     */
    private String parentId;
    /**
     * 目录名称
     */
    private String name;
    /**
     * 排序序号（值越小越靠前）
     */
    private Integer sortOrder;

    @Override
    public String getId() {
        return this.directoryId;
    }

    // ======================== 工厂方法 ========================

    /**
     * 创建目录聚合根。
     */
    public static Directory create(String userId, String parentId, String name) {
        Directory directory = new Directory();
        directory.directoryId = CellHelper.getInstance().generateId(CellEnum.DIRECTORY);
        directory.parentId = parentId;
        directory.name = name;
        directory.sortOrder = 0;
        directory.setCreator(userId);
        directory.setUpdater(userId);
        directory.setCreateTime(LocalDateTime.now());
        directory.setUpdateTime(LocalDateTime.now());
        directory.setDeleted(false);
        return directory;
    }

    // ======================== 行为方法 ========================

    /**
     * 修改目录名称。
     */
    public void rename(String userId, String newName) {
        this.name = newName;
        super.setUpdater(userId);
        super.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 移动目录（改变父级）。
     */
    public void moveTo(String userId, String newParentId) {
        this.parentId = newParentId;
        super.setUpdater(userId);
        super.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 删除目录
     */
    public void delete(String userId) {
        super.setDeleted(true);
        super.setUpdater(userId);
        super.setUpdateTime(LocalDateTime.now());
    }
}
