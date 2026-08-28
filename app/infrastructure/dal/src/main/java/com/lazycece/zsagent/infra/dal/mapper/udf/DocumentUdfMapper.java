package com.lazycece.zsagent.infra.dal.mapper.udf;

import com.lazycece.zsagent.infra.dal.dto.DocumentQueryDTO;
import com.lazycece.zsagent.infra.dal.po.DocumentPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * agent_document 表用户自定义 Mapper
 *
 * @author lazycece
 */
@Mapper
public interface DocumentUdfMapper {

    /**
     * 插入文档记录。
     */
    int insert(DocumentPO document);

    /**
     * 按 documentId 查询（无权限过滤，内部使用）。
     */
    DocumentPO selectById(@Param("documentId") String documentId);

    /**
     * 按 documentId 查询（含权限过滤）。
     */
    DocumentPO selectByDocumentId(@Param("query") DocumentQueryDTO query);

    /**
     * 分页查询文档列表（含权限过滤与筛选条件）。
     */
    List<DocumentPO> selectByUserId(@Param("query") DocumentQueryDTO query,
                                    @Param("offset") int offset,
                                    @Param("size") int size);

    /**
     * 统计文档数（含权限过滤与筛选条件）。
     */
    long countByUserId(@Param("query") DocumentQueryDTO query);

    /**
     * 更新文档。
     */
    int update(DocumentPO document);

    /**
     * 按目录统计文档数。
     */
    int countByDirectoryId(@Param("directoryId") String directoryId);
}
