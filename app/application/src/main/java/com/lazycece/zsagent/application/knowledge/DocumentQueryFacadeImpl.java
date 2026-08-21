package com.lazycece.zsagent.application.knowledge;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.domain.model.Pagination;
import com.lazycece.rapidf.restful.dto.PageData;
import com.lazycece.rapidf.restful.exception.factory.ExceptionFactory;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.rapidf.utils.EnumUtils;
import com.lazycece.zsagent.application.knowledge.converter.DocumentConverter;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentStatus;
import com.lazycece.zsagent.domain.knowledge.model.Directory;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.model.DocumentVersion;
import com.lazycece.zsagent.domain.knowledge.repository.DirectoryRepository;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentVersionRepository;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentListQuery;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentQuery;
import com.lazycece.zsagent.facade.knowledge.api.DocumentQueryFacade;
import com.lazycece.zsagent.facade.knowledge.dto.DocumentDTO;
import com.lazycece.zsagent.facade.knowledge.request.DocumentDetailQueryRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentListQueryRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentVersionListRequest;
import com.lazycece.zsagent.facade.knowledge.request.EtlStatusQueryRequest;
import com.lazycece.zsagent.facade.knowledge.result.DocumentDetailResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentVersionListResult;
import com.lazycece.zsagent.facade.knowledge.result.EtlStatusResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档查询门面实现。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class DocumentQueryFacadeImpl implements DocumentQueryFacade {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;
    private final DirectoryRepository directoryRepository;

    public DocumentQueryFacadeImpl(DocumentRepository documentRepository,
                                   DocumentVersionRepository versionRepository,
                                   DirectoryRepository directoryRepository) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
        this.directoryRepository = directoryRepository;
    }

    @Override
    public RespData<DocumentDetailResult> getDocument(DocumentDetailQueryRequest request) {
        DocumentQuery query = new DocumentQuery();
        query.setUserId(request.getUserId());
        query.setUserDepts(getUserDeptsFromContext());
        query.setDocumentId(request.getDocumentId());

        Document document = documentRepository.findByDocumentId(query);
        if (document == null) {
            throw ExceptionFactory.businessException("文档不存在或无权访问");
        }
        DocumentDetailResult result = new DocumentDetailResult();
        result.setDocument(DocumentConverter.toDocumentDTO(document));
        return RespData.success(result);
    }

    @Override
    public RespData<PageData<DocumentDTO>> listDocuments(DocumentListQueryRequest request) {
        List<String> userDepts = getUserDeptsFromContext();
        DocumentStatus status = StringUtils.isNotBlank(request.getStatus())
                ? EnumUtils.getEnum(DocumentStatus.class, request.getStatus())
                : null;

        DocumentListQuery query = new DocumentListQuery();
        query.setUserId(request.getUserId());
        query.setUserDepts(userDepts);
        query.setDirectoryId(request.getDirectoryId());
        query.setStatus(status);
        query.setKeyword(request.getKeyword());

        Pagination pagination = new Pagination(request.getPage(), request.getSize());
        List<Document> documents = documentRepository.findByUserId(query, pagination);

        List<DocumentDTO> dtos = documents.stream()
                .map(document -> {
                    DocumentDTO dto = DocumentConverter.toDocumentDTO(document);
                    if (StringUtils.isNotBlank(document.getDirectoryId())) {
                        Directory directory = directoryRepository.findByDirectoryId(document.getDirectoryId());
                        if (directory != null) {
                            dto.setDirectoryName(directory.getName());
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        PageData<DocumentDTO> pageData = new PageData<>();
        pageData.setData(dtos);
        pageData.setCount(pagination.getCount());
        pageData.setPage(request.getPage());
        return RespData.success(pageData);
    }

    @Override
    public RespData<DocumentVersionListResult> listVersions(DocumentVersionListRequest request) {
        List<DocumentVersion> versions = versionRepository.findByDocumentId(request.getDocumentId());
        DocumentVersionListResult result = new DocumentVersionListResult();
        result.setVersions(DocumentConverter.toVersionDTOList(versions));
        return RespData.success(result);
    }

    @Override
    public RespData<EtlStatusResult> getEtlStatus(EtlStatusQueryRequest request) {
        // ETL 状态不涉及敏感内容，按 ID 直查即可，无需权限过滤
        Document document = documentRepository.findById(request.getDocumentId());
        if (document == null) {
            throw ExceptionFactory.businessException("文档不存在");
        }
        EtlStatusResult result = new EtlStatusResult();
        result.setDocumentId(document.getDocumentId());
        result.setEtlStatus(document.getEtlStatus() != null ? document.getEtlStatus().getCode() : null);
        result.setErrorMessage(document.getEtlErrorMessage());
        result.setDocumentStatus(document.getStatus() != null ? document.getStatus().getCode() : null);
        return RespData.success(result);
    }

    /**
     * 获取当前用户的所属部门列表。
     * TODO: 接入用户服务获取真实部门，当前 stub 返回空列表。
     */
    private List<String> getUserDeptsFromContext() {
        return Collections.emptyList();
    }
}
