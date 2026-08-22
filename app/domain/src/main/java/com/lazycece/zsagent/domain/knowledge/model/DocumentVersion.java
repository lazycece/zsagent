package com.lazycece.zsagent.domain.knowledge.model;

import com.lazycece.cell.specification.CellHelper;
import com.lazycece.rapidf.domain.anotation.DomainEntity;
import com.lazycece.rapidf.domain.model.Entity;
import com.lazycece.zsagent.domain.common.enums.CellEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 文档版本实体
 *
 * @author lazycece
 */
@Getter
@Setter
@DomainEntity
public class DocumentVersion extends Entity<String> {

    /** 版本唯一标识 */
    private String versionId;
    /** 所属文档ID */
    private String documentId;
    /** 版本号（1, 2, 3...） */
    private Integer versionNumber;
    /** 该版本文件存储路径 */
    private String filePath;
    /** 该版本文件大小（字节） */
    private Long fileSize;
    /** 变更说明 */
    private String changeLog;

    @Override
    public String getId() {
        return this.versionId;
    }

    /**
     * 创建版本实体。
     */
    static DocumentVersion create(String documentId, Integer versionNumber, String filePath,
                                  Long fileSize, String changeLog, String creator) {
        DocumentVersion version = new DocumentVersion();
        version.versionId = CellHelper.getInstance().generateId(CellEnum.DOCUMENT_VERSION);
        version.documentId = documentId;
        version.versionNumber = versionNumber;
        version.filePath = filePath;
        version.fileSize = fileSize;
        version.changeLog = changeLog;
        version.setCreator(creator);
        version.setUpdater(creator);
        version.setCreateTime(LocalDateTime.now());
        version.setUpdateTime(LocalDateTime.now());
        version.setDeleted(false);
        return version;
    }
}
