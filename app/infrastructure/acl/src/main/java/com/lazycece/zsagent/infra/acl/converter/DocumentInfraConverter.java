package com.lazycece.zsagent.infra.acl.converter;

import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.rapidf.utils.EnumUtils;
import com.lazycece.rapidf.utils.json.JsonUtils;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentFormat;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentStatus;
import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.model.DocumentVersion;
import com.lazycece.zsagent.domain.knowledge.model.Directory;
import com.lazycece.zsagent.infra.dal.po.DocumentPO;
import com.lazycece.zsagent.infra.dal.po.DocumentVersionPO;
import com.lazycece.zsagent.infra.dal.po.DirectoryPO;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 基础设施层 ↔ 领域层 对象转换器（知识管理模块）。
 *
 * @author lazycece
 */
public final class DocumentInfraConverter {

    private DocumentInfraConverter() {
    }

    // ======================== Document: 领域 → PO ========================

    public static DocumentPO toDocumentPO(Document document) {
        if (document == null) {
            return null;
        }
        DocumentPO po = new DocumentPO();
        po.setDocumentId(document.getDocumentId());
        po.setTitle(document.getTitle());
        po.setSummary(document.getSummary());
        po.setFormat(document.getFormat() != null ? document.getFormat().getCode() : null);
        po.setFileSize(document.getFileSize());
        po.setFilePath(document.getFilePath());
        po.setDirectoryId(document.getDirectoryId());
        po.setTags(JsonUtils.toJSONString(document.getTags()));
        po.setVisibility(document.getVisibility() != null ? document.getVisibility().getCode() : null);
        po.setVisibleTo(JsonUtils.toJSONString(document.getVisibleTo()));
        po.setStatus(document.getStatus() != null ? document.getStatus().getCode() : null);
        po.setEtlStatus(document.getEtlStatus() != null ? document.getEtlStatus().getCode() : null);
        po.setEtlErrorMsg(document.getEtlErrorMessage());
        po.setCurrentVersion(document.getCurrentVersion());
        po.setDeletedTime(document.getDeletedTime());
        po.setCreator(document.getCreator());
        po.setUpdater(document.getUpdater());
        po.setCreateTime(document.getCreateTime());
        po.setUpdateTime(document.getUpdateTime());
        po.setDeleted(document.isDeleted());
        return po;
    }

    // ======================== Document: PO → 领域 ========================

    public static Document toDocument(DocumentPO po) {
        if (po == null) {
            return null;
        }
        Document document = new Document();
        document.setDocumentId(po.getDocumentId());
        document.setTitle(po.getTitle());
        document.setSummary(po.getSummary());
        document.setFormat(EnumUtils.getEnum(DocumentFormat.class, po.getFormat()));
        document.setFileSize(po.getFileSize());
        document.setFilePath(po.getFilePath());
        document.setDirectoryId(po.getDirectoryId());
        document.setTags(JsonUtils.parseArray(po.getTags(), String.class));
        document.setVisibility(EnumUtils.getEnum(Visibility.class, po.getVisibility()));
        document.setVisibleTo(JsonUtils.parseArray(po.getVisibleTo(), String.class));
        document.setStatus(EnumUtils.getEnum(DocumentStatus.class, po.getStatus()));
        document.setEtlStatus(EnumUtils.getEnum(EtlStatus.class, po.getEtlStatus()));
        document.setEtlErrorMessage(po.getEtlErrorMsg());
        document.setCurrentVersion(po.getCurrentVersion());
        document.setDeletedTime(po.getDeletedTime());
        document.setCreator(po.getCreator());
        document.setUpdater(po.getUpdater());
        document.setCreateTime(po.getCreateTime());
        document.setUpdateTime(po.getUpdateTime());
        document.setDeleted(DefaultUtils.defaultValue(po.getDeleted(), false));
        return document;
    }

    // ======================== DocumentVersion: 领域 → PO ========================

    public static DocumentVersionPO toVersionPO(DocumentVersion version) {
        if (version == null) {
            return null;
        }
        DocumentVersionPO po = new DocumentVersionPO();
        po.setVersionId(version.getVersionId());
        po.setDocumentId(version.getDocumentId());
        po.setVersionNumber(version.getVersionNumber());
        po.setFilePath(version.getFilePath());
        po.setFileSize(version.getFileSize());
        po.setChangeLog(version.getChangeLog());
        po.setCreator(version.getCreator());
        po.setUpdater(version.getUpdater());
        po.setCreateTime(version.getCreateTime());
        po.setUpdateTime(version.getUpdateTime());
        po.setDeleted(version.isDeleted());
        return po;
    }

    // ======================== DocumentVersion: PO → 领域 ========================

    public static DocumentVersion toVersion(DocumentVersionPO po) {
        if (po == null) {
            return null;
        }
        DocumentVersion version = new DocumentVersion();
        version.setVersionId(po.getVersionId());
        version.setDocumentId(po.getDocumentId());
        version.setVersionNumber(po.getVersionNumber());
        version.setFilePath(po.getFilePath());
        version.setFileSize(po.getFileSize());
        version.setChangeLog(po.getChangeLog());
        version.setCreator(po.getCreator());
        version.setUpdater(po.getUpdater());
        version.setCreateTime(po.getCreateTime());
        version.setUpdateTime(po.getUpdateTime());
        version.setDeleted(DefaultUtils.defaultValue(po.getDeleted(), false));
        return version;
    }

    // ======================== Directory: 领域 → PO ========================

    public static DirectoryPO toDirectoryPO(Directory directory) {
        if (directory == null) {
            return null;
        }
        DirectoryPO po = new DirectoryPO();
        po.setDirectoryId(directory.getDirectoryId());
        po.setParentId(directory.getParentId());
        po.setName(directory.getName());
        po.setSortOrder(directory.getSortOrder());
        po.setCreator(directory.getCreator());
        po.setUpdater(directory.getUpdater());
        po.setCreateTime(directory.getCreateTime());
        po.setUpdateTime(directory.getUpdateTime());
        po.setDeleted(directory.isDeleted());
        return po;
    }

    // ======================== Directory: PO → 领域 ========================

    public static Directory toDirectory(DirectoryPO po) {
        if (po == null) {
            return null;
        }
        Directory directory = new Directory();
        directory.setDirectoryId(po.getDirectoryId());
        directory.setParentId(po.getParentId());
        directory.setName(po.getName());
        directory.setSortOrder(po.getSortOrder());
        directory.setCreator(po.getCreator());
        directory.setUpdater(po.getUpdater());
        directory.setCreateTime(po.getCreateTime());
        directory.setUpdateTime(po.getUpdateTime());
        directory.setDeleted(DefaultUtils.defaultValue(po.getDeleted(), false));
        return directory;
    }
}
