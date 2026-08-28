package com.lazycece.zsagent.application.knowledge.converter;

import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.model.DocumentVersion;
import com.lazycece.zsagent.facade.knowledge.dto.DocumentDTO;
import com.lazycece.zsagent.facade.knowledge.dto.DocumentVersionDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档数据转换器，负责将领域对象转换为 Facade DTO。
 *
 * @author lazycece
 */
public final class DocumentConverter {

    private DocumentConverter() {
    }

    /**
     * 文档聚合根 → DocumentDTO。
     */
    public static DocumentDTO toDocumentDTO(Document document) {
        if (document == null) {
            return null;
        }
        DocumentDTO dto = new DocumentDTO();
        dto.setDocumentId(document.getDocumentId());
        dto.setTitle(document.getTitle());
        dto.setSummary(document.getSummary());
        dto.setFormat(document.getFormat() != null ? document.getFormat().getCode() : null);
        dto.setFileSize(document.getFileSize());
        dto.setDirectoryId(document.getDirectoryId());
        dto.setTags(document.getTags());
        dto.setVisibility(document.getVisibility() != null ? document.getVisibility().getCode() : null);
        dto.setStatus(document.getStatus() != null ? document.getStatus().getCode() : null);
        dto.setEtlStatus(document.getEtlStatus() != null ? document.getEtlStatus().getCode() : null);
        dto.setEtlErrorMessage(document.getEtlErrorMessage());
        dto.setCurrentVersion(document.getCurrentVersion());
        dto.setCreator(document.getCreator());
        dto.setCreateTime(document.getCreateTime());
        dto.setUpdateTime(document.getUpdateTime());
        return dto;
    }

    /**
     * 版本实体 → DocumentVersionDTO。
     */
    public static DocumentVersionDTO toVersionDTO(DocumentVersion version) {
        if (version == null) {
            return null;
        }
        DocumentVersionDTO dto = new DocumentVersionDTO();
        dto.setVersionId(version.getVersionId());
        dto.setVersionNumber(version.getVersionNumber());
        dto.setFileSize(version.getFileSize());
        dto.setChangeLog(version.getChangeLog());
        dto.setCreator(version.getCreator());
        dto.setCreateTime(version.getCreateTime());
        return dto;
    }

    /**
     * 版本列表 → DocumentVersionDTO 列表。
     */
    public static List<DocumentVersionDTO> toVersionDTOList(List<DocumentVersion> versions) {
        if (versions == null) {
            return Collections.emptyList();
        }
        return versions.stream()
                .map(DocumentConverter::toVersionDTO)
                .collect(Collectors.toList());
    }
}
