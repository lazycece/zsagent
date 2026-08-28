package com.lazycece.zsagent.facade.knowledge.api;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.dto.DirectoryDTO;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryChildrenQueryRequest;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryListResult;

/**
 * 目录查询门面接口
 *
 * @author lazycece
 */
public interface DirectoryQueryFacade {

    /**
     * 查询子目录列表。
     *
     * @param request 子目录查询请求
     * @return 子目录列表
     */
    RespData<DirectoryListResult> listChildren(DirectoryChildrenQueryRequest request);

    /**
     * 查询完整目录树。
     *
     * @return 目录树
     */
    RespData<DirectoryListResult> tree();
}
