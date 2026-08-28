package com.lazycece.zsagent.infra.dal.mapper.udf;

import com.lazycece.zsagent.infra.dal.po.DirectoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * agent_directory 表用户自定义 Mapper
 *
 * @author lazycece
 */
@Mapper
public interface DirectoryUdfMapper {

    /**
     * 插入目录记录。
     */
    int insert(DirectoryPO directory);

    /**
     * 按 directoryId 查询。
     */
    DirectoryPO selectById(@Param("directoryId") String directoryId);

    /**
     * 查询指定父目录下的所有子目录。
     */
    List<DirectoryPO> selectByParentId(@Param("parentId") String parentId);

    /**
     * 查询所有目录。
     */
    List<DirectoryPO> selectAll();

    /**
     * 更新目录。
     */
    int update(DirectoryPO directory);

    /**
     * 统计子目录数。
     */
    int countByParentId(@Param("parentId") String parentId);
}
