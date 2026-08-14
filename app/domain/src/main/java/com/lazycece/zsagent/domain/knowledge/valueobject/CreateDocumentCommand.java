package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 创建文档命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class CreateDocumentCommand {

    /** 操作主体标识 */
    private String userId;
    /** 文档标题 */
    private String title;
    /** 文件格式 */
    private DocumentFormat format;
    /** 文件大小（字节） */
    private Long fileSize;
    /** 文件存储路径 */
    private String filePath;
    /** 所属目录ID（可空） */
    private String directoryId;
    /** 标签列表 */
    private List<String> tags;
    /** 可见范围 */
    private Visibility visibility;
    /** 可见对象列表 */
    private List<String> visibleTo;
}
