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
import com.lazycece.rapidf.domain.model.Pagination;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentCountQuery;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentListQuery;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentQuery;
import com.lazycece.zsagent.infra.acl.converter.DocumentInfraConverter;
import com.lazycece.zsagent.infra.dal.dto.DocumentQueryDTO;
import com.lazycece.zsagent.infra.dal.mapper.udf.DocumentUdfMapper;
import com.lazycece.zsagent.infra.dal.po.DocumentPO;
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
    public void save(Document document) {
        documentMapper.insert(DocumentInfraConverter.toDocumentPO(document));
    }

    @Override
    public Document findByDocumentId(DocumentQuery query) {
        DocumentQueryDTO dto =
                DocumentQueryDTO.build(
                        query.getUserId(),
                        query.getUserDepts(),
                        query.getDocumentId(),
                        null,
                        null,
                        null);
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
        DocumentQueryDTO dto =
                DocumentQueryDTO.build(
                        query.getUserId(),
                        query.getUserDepts(),
                        null,
                        query.getDirectoryId(),
                        query.getStatus() != null ? query.getStatus().getCode() : null,
                        query.getKeyword());
        long total = documentMapper.countByUserId(dto);
        pagination.setCount(total);

        int offset = (pagination.getPage() - 1) * pagination.getSize();
        List<DocumentPO> pos = documentMapper.selectByUserId(dto, offset, pagination.getSize());
        return pos.stream().map(DocumentInfraConverter::toDocument).collect(Collectors.toList());
    }

    @Override
    public int countByUserId(DocumentCountQuery query) {
        DocumentQueryDTO dto =
                DocumentQueryDTO.build(
                        query.getUserId(),
                        query.getUserDepts(),
                        null,
                        query.getDirectoryId(),
                        query.getStatus() != null ? query.getStatus().getCode() : null,
                        query.getKeyword());
        return (int) documentMapper.countByUserId(dto);
    }

    @Override
    public void update(Document document) {
        documentMapper.update(DocumentInfraConverter.toDocumentPO(document));
    }

    @Override
    public int countByDirectoryId(String directoryId) {
        return documentMapper.countByDirectoryId(directoryId);
    }
}
