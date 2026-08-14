package com.lazycece.zsagent.domain.knowledge.valueobject;

import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 更新文档元数据命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class UpdateDocumentMetadataCommand {

    /** 操作主体标识 */
    private String userId;
    /** 文档ID */
    private String documentId;
    /** 文档标题 */
    private String title;
    /** 文档摘要 */
    private String summary;
    /** 所属目录ID */
    private String directoryId;
    /** 标签列表 */
    private List<String> tags;
    /** 可见范围 */
    private Visibility visibility;
    /** 可见对象列表 */
    private List<String> visibleTo;
}
