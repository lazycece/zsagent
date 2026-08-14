package com.lazycece.zsagent.infra.acl.repository;

import com.lazycece.rapidf.domain.anotation.DomainRepository;
import com.lazycece.rapidf.domain.model.Pagination;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.valueobject.DocumentCountQuery;
import com.lazycece.zsagent.domain.knowledge.valueobject.DocumentListQuery;
import com.lazycece.zsagent.domain.knowledge.valueobject.DocumentQuery;
import com.lazycece.zsagent.infra.acl.converter.DocumentInfraConverter;
import com.lazycece.zsagent.infra.dal.dto.DocumentQueryDTO;
import com.lazycece.zsagent.infra.dal.mapper.udf.DocumentUdfMapper;
import com.lazycece.zsagent.infra.dal.po.DocumentPO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档仓储 MyBatis 实现。
 *
 * @author lazycece
 */
@DomainRepository
public class DocumentRepositoryImpl implements DocumentRepository {

    private final DocumentUdfMapper documentMapper;

    public DocumentRepositoryImpl(DocumentUdfMapper documentMapper) {
        this.documentMapper = documentMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Document document) {
        documentMapper.insert(DocumentInfraConverter.toDocumentPO(document));
    }

    @Override
    public Document findByDocumentId(DocumentQuery query) {
        DocumentQueryDTO dto = buildQueryDTO(query.getUserId(), query.getUserDepts(),
                query.getDocumentId(), null, null, null);
        DocumentPO po = documentMapper.selectByDocumentId(dto);
        return DocumentInfraConverter.toDocument(po);
    }

    @Override
    public Document findById(String documentId) {
        DocumentPO po = documentMapper.selectById(documentId);
        return DocumentInfraConverter.toDocument(po);
    }

    @Override
    public List<Document> findByUserId(DocumentListQuery query, Pagination pagination) {
        DocumentQueryDTO dto = buildQueryDTO(query.getUserId(), query.getUserDepts(),
                null, query.getDirectoryId(),
                query.getStatus() != null ? query.getStatus().getCode() : null,
                query.getKeyword());
        long total = documentMapper.countByUserId(dto);
        pagination.setCount(total);

        int offset = (pagination.getPage() - 1) * pagination.getSize();
        List<DocumentPO> pos = documentMapper.selectByUserId(dto, offset, pagination.getSize());
        return pos.stream()
                .map(DocumentInfraConverter::toDocument)
                .collect(Collectors.toList());
    }

    @Override
    public int countByUserId(DocumentCountQuery query) {
        DocumentQueryDTO dto = buildQueryDTO(query.getUserId(), query.getUserDepts(),
                null, query.getDirectoryId(),
                query.getStatus() != null ? query.getStatus().getCode() : null,
                query.getKeyword());
        return (int) documentMapper.countByUserId(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Document document) {
        documentMapper.update(DocumentInfraConverter.toDocumentPO(document));
    }

    @Override
    public int countByDirectoryId(String directoryId) {
        return documentMapper.countByDirectoryId(directoryId);
    }

    /**
     * 构建数据库访问层查询 DTO。
     */
    private DocumentQueryDTO buildQueryDTO(String userId, List<String> userDepts, String documentId,
                                           String directoryId, String status, String keyword) {
        DocumentQueryDTO dto = new DocumentQueryDTO();
        dto.setUserId(userId);
        dto.setUserDepts(userDepts);
        dto.setDocumentId(documentId);
        dto.setDirectoryId(directoryId);
        dto.setStatus(status);
        dto.setKeyword(keyword);
        return dto;
    }
}
