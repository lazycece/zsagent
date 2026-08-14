package com.lazycece.zsagent.facade.knowledge.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 删除目录请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DirectoryDeleteRequest extends BaseRequest implements Serializable {

    /** 操作主体标识 */
    @NotBlank(message = "userId不能为空")
    private String userId;

    /** 目录ID */
    @NotBlank(message = "directoryId不能为空")
    private String directoryId;
}
