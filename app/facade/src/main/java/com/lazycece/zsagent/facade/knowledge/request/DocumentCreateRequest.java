package com.lazycece.zsagent.facade.knowledge.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 文档创建请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentCreateRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 文档标题（未填则取文件名） */
    private String title;

    /** 所属目录ID（可空） */
    private String directoryId;

    /** 标签列表（可空） */
    private List<String> tags;

    /** 可见范围：public / department / specific */
    @NotBlank(message = "visibility不能为空")
    private String visibility;

    /** 可见对象列表（DEPARTMENT/SPECIFIC 时必填） */
    private List<String> visibleTo;

    /** 文件相对路径（先经文件上传接口获取） */
    @NotBlank(message = "filePath不能为空")
    private String filePath;
}
