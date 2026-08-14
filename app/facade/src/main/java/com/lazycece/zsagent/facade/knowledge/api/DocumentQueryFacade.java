package com.lazycece.zsagent.facade.knowledge.api;

import com.lazycece.rapidf.restful.dto.PageData;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.dto.DocumentDTO;
import com.lazycece.zsagent.facade.knowledge.request.DocumentDetailQueryRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentListQueryRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentVersionListRequest;
import com.lazycece.zsagent.facade.knowledge.request.EtlStatusQueryRequest;
import com.lazycece.zsagent.facade.knowledge.result.DocumentDetailResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentVersionListResult;
import com.lazycece.zsagent.facade.knowledge.result.EtlStatusResult;

/**
 * 文档查询门面接口
 *
 * @author lazycece
 */
public interface DocumentQueryFacade {

    /**
     * 查询文档详情。
     *
     * @param request 详情查询请求
     * @return 文档详情
     */
    RespData<DocumentDetailResult> getDocument(DocumentDetailQueryRequest request);

    /**
     * 分页查询文档列表。
     *
     * @param request 列表查询请求
     * @return 分页文档列表
     */
    RespData<PageData<DocumentDTO>> listDocuments(DocumentListQueryRequest request);

    /**
     * 查询文档版本历史。
     *
     * @param request 版本历史查询请求
     * @return 版本历史列表
     */
    RespData<DocumentVersionListResult> listVersions(DocumentVersionListRequest request);

    /**
     * 查询 ETL 处理状态。
     *
     * @param request ETL 状态查询请求
     * @return ETL 状态
     */
    RespData<EtlStatusResult> getEtlStatus(EtlStatusQueryRequest request);
}
