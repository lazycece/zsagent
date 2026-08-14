package com.lazycece.zsagent.facade.knowledge.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 更新文档元数据请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentUpdateMetadataRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 文档ID */
    @NotBlank(message = "documentId不能为空")
    private String documentId;

    /** 文档标题 */
    private String title;

    /** 文档摘要 */
    private String summary;

    /** 所属目录ID */
    private String directoryId;

    /** 标签列表 */
    private List<String> tags;

    /** 可见范围：public / department / specific */
    private String visibility;

    /** 可见对象列表 */
    private List<String> visibleTo;
}
