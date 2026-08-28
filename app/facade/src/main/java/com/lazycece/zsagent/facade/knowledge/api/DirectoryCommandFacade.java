package com.lazycece.zsagent.facade.knowledge.api;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryCreateRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryDeleteRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryMoveRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryRenameRequest;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryCreateResult;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryDeleteResult;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryMoveResult;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryRenameResult;

/**
 * 目录命令门面接口
 *
 * @author lazycece
 */
public interface DirectoryCommandFacade {

    /**
     * 创建目录。
     *
     * @param request 创建目录请求
     * @return 创建结果
     */
    RespData<DirectoryCreateResult> create(DirectoryCreateRequest request);

    /**
     * 重命名目录。
     *
     * @param request 重命名请求
     * @return 操作结果
     */
    RespData<DirectoryRenameResult> rename(DirectoryRenameRequest request);

    /**
     * 移动目录。
     *
     * @param request 移动请求
     * @return 操作结果
     */
    RespData<DirectoryMoveResult> move(DirectoryMoveRequest request);

    /**
     * 删除目录。
     *
     * @param request 删除请求
     * @return 操作结果
     */
    RespData<DirectoryDeleteResult> delete(DirectoryDeleteRequest request);
}
