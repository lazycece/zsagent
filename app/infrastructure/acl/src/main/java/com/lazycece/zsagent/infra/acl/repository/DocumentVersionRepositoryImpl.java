/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
        List<DocumentVersionPO> pos =
                safeVersions.stream()
                        .map(DocumentInfraConverter::toVersionPO)
                        .collect(Collectors.toList());
        versionMapper.insertBatch(pos);
    }

    @Override
    public List<DocumentVersion> findByDocumentId(String documentId) {
        List<DocumentVersionPO> pos = versionMapper.selectByDocumentId(documentId);
        return pos.stream().map(DocumentInfraConverter::toVersion).collect(Collectors.toList());
    }

    @Override
    public DocumentVersion findByVersionId(String versionId) {
        DocumentVersionPO po = versionMapper.selectByVersionId(versionId);
        return DocumentInfraConverter.toVersion(po);
    }
}
