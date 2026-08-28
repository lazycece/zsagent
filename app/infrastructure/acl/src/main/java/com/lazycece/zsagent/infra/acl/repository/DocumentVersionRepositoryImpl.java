package com.lazycece.zsagent.infra.acl.repository;

import com.lazycece.rapidf.domain.anotation.DomainRepository;
import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.zsagent.domain.knowledge.model.DocumentVersion;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentVersionRepository;
import com.lazycece.zsagent.infra.acl.converter.DocumentInfraConverter;
import com.lazycece.zsagent.infra.dal.mapper.udf.DocumentVersionUdfMapper;
import com.lazycece.zsagent.infra.dal.po.DocumentVersionPO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档版本仓储 MyBatis 实现。
 *
 * @author lazycece
 */
@DomainRepository
public class DocumentVersionRepositoryImpl implements DocumentVersionRepository {

    private final DocumentVersionUdfMapper versionMapper;

    public DocumentVersionRepositoryImpl(DocumentVersionUdfMapper versionMapper) {
        this.versionMapper = versionMapper;
    }

    @Override
    public void save(List<DocumentVersion> versions) {
        List<DocumentVersion> safeVersions = DefaultUtils.defaultList(versions);
        if (safeVersions.isEmpty()) {
            return;
        }
        List<DocumentVersionPO> pos = safeVersions.stream()
                .map(DocumentInfraConverter::toVersionPO)
                .collect(Collectors.toList());
        versionMapper.insertBatch(pos);
    }

    @Override
    public List<DocumentVersion> findByDocumentId(String documentId) {
        List<DocumentVersionPO> pos = versionMapper.selectByDocumentId(documentId);
        return pos.stream()
                .map(DocumentInfraConverter::toVersion)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentVersion findByVersionId(String versionId) {
        DocumentVersionPO po = versionMapper.selectByVersionId(versionId);
        return DocumentInfraConverter.toVersion(po);
    }
}
