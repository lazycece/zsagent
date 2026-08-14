package com.lazycece.zsagent.infra.dal.mapper.udf;

import com.lazycece.zsagent.infra.dal.po.DocumentVersionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * agent_document_version 表用户自定义 Mapper
 *
 * @author lazycece
 */
@Mapper
public interface DocumentVersionUdfMapper {

    /**
     * 批量插入版本记录。
     */
    int insertBatch(List<DocumentVersionPO> versions);

    /**
     * 按 documentId 查询所有版本（按版本号倒序）。
     */
    List<DocumentVersionPO> selectByDocumentId(@Param("documentId") String documentId);

    /**
     * 按 versionId 查询单个版本。
     */
    DocumentVersionPO selectByVersionId(@Param("versionId") String versionId);
}
