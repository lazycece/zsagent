package com.lazycece.zsagent.facade.knowledge.request;

import com.lazycece.rapidf.restful.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 子目录查询请求
 *
 * @author lazycece
 */
@Getter
@Setter
public class DirectoryChildrenQueryRequest extends BaseRequest implements Serializable {

    /** 父目录ID（可空，查询根级目录） */
    private String parentId;
}
