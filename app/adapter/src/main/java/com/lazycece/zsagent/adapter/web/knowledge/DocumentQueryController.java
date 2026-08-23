package com.lazycece.zsagent.adapter.web.knowledge;

import com.lazycece.rapidf.restful.dto.PageData;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.api.DocumentQueryFacade;
import com.lazycece.zsagent.facade.knowledge.dto.DocumentDTO;
import com.lazycece.zsagent.facade.knowledge.request.DocumentDetailQueryRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentListQueryRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentVersionListRequest;
import com.lazycece.zsagent.facade.knowledge.request.EtlStatusQueryRequest;
import com.lazycece.zsagent.facade.knowledge.result.DocumentDetailResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentVersionListResult;
import com.lazycece.zsagent.facade.knowledge.result.EtlStatusResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档查询控制器
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/document")
public class DocumentQueryController implements DocumentQueryFacade {

    private final DocumentQueryFacade documentQueryFacade;

    public DocumentQueryController(DocumentQueryFacade documentQueryFacade) {
        this.documentQueryFacade = documentQueryFacade;
    }

    /**
     * 查询文档详情。
     */
    @Override
    @GetMapping("/get-document")
    public RespData<DocumentDetailResult> getDocument(
            @Validated DocumentDetailQueryRequest request) {
        return documentQueryFacade.getDocument(request);
    }

    /**
     * 分页查询文档列表。
     */
    @Override
    @GetMapping("/list-documents")
    public RespData<PageData<DocumentDTO>> listDocuments(
            @Validated DocumentListQueryRequest request) {
        return documentQueryFacade.listDocuments(request);
    }

    /**
     * 查询文档版本历史。
     */
    @Override
    @GetMapping("/list-versions")
    public RespData<DocumentVersionListResult> listVersions(
            @Validated DocumentVersionListRequest request) {
        return documentQueryFacade.listVersions(request);
    }

    /**
     * 查询 ETL 处理状态。
     */
    @Override
    @GetMapping("/get-etl-status")
    public RespData<EtlStatusResult> getEtlStatus(@Validated EtlStatusQueryRequest request) {
        return documentQueryFacade.getEtlStatus(request);
    }
}
