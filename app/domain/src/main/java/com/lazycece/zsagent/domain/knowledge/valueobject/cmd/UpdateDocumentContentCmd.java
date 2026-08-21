package com.lazycece.zsagent.domain.knowledge.valueobject.cmd;

import lombok.Getter;
import lombok.Setter;

/**
 * 更新文档内容命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class UpdateDocumentContentCmd {

    /** 操作主体标识 */
    private String userId;
    /** 文档ID */
    private String documentId;
    /** 新文件存储路径 */
    private String filePath;
    /** 新文件大小（字节） */
    private Long fileSize;
    /** 变更说明 */
    private String changeLog;
}
