package com.lazycece.zsagent.facade.knowledge.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 创建目录请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DirectoryCreateRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 父目录ID（可空，根目录） */
    private String parentId;

    /** 目录名称 */
    @NotBlank(message = "name不能为空")
    private String name;
}
