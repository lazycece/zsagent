package com.lazycece.zsagent.domain.knowledge.model;

import com.lazycece.rapidf.domain.anotation.DomainAggregate;
import com.lazycece.rapidf.domain.model.Aggregate;
import com.lazycece.rapidf.utils.UUIDUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 目录聚合根
 *
 * @author lazycece
 */
@Getter
@Setter
@DomainAggregate
public class Directory extends Aggregate<String> {

    /** 目录唯一标识 */
    private String directoryId;
    /** 父目录ID（根目录为 null） */
    private String parentId;
    /** 目录名称 */
    private String name;
    /** 排序序号（值越小越靠前） */
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
        directory.directoryId = UUIDUtils.uuid();
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
    public void rename(String newName) {
        this.name = newName;
    }

    /**
     * 移动目录（改变父级）。
     */
    public void moveTo(String newParentId) {
        this.parentId = newParentId;
    }
}
