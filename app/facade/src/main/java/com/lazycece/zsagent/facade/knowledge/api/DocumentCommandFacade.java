package com.lazycece.zsagent.facade.knowledge.api;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.request.DocumentDeleteRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRestoreRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRollbackRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateContentRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateMetadataRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentCreateRequest;
import com.lazycece.zsagent.facade.knowledge.result.DocumentCreateResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentDeleteResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRestoreResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRollbackResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateContentResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateMetadataResult;

/**
 * 文档命令门面接口
 *
 * @author lazycece
 */
public interface DocumentCommandFacade {

    /**
     * 创建文档，异步触发 ETL。
     *
     * @param request 创建请求
     * @return 创建结果
     */
    RespData<DocumentCreateResult> create(DocumentCreateRequest request);

    /**
     * 更新文档元数据（标题、标签、目录、权限），不产生新版本。
     *
     * @param request 更新元数据请求
     * @return 操作结果
     */
    RespData<DocumentUpdateMetadataResult> updateMetadata(DocumentUpdateMetadataRequest request);

    /**
     * 更新文档文件内容，产生新版本并触发 ETL。
     *
     * @param request 更新内容请求
     * @return 操作结果
     */
    RespData<DocumentUpdateContentResult> updateContent(DocumentUpdateContentRequest request);

    /**
     * 删除文档（移入回收站）。
     *
     * @param request 删除请求
     * @return 操作结果
     */
    RespData<DocumentDeleteResult> delete(DocumentDeleteRequest request);

    /**
     * 从回收站恢复文档。
     *
     * @param request 恢复请求
     * @return 操作结果
     */
    RespData<DocumentRestoreResult> restore(DocumentRestoreRequest request);

    /**
     * 回滚到指定历史版本。
     *
     * @param request 回滚请求
     * @return 操作结果
     */
    RespData<DocumentRollbackResult> rollback(DocumentRollbackRequest request);
}
