package com.lazycece.zsagent.infra.dal.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 文档查询参数 DTO（数据库访问层）
 *
 * @author lazycece
 */
@Getter
@Setter
public class DocumentQueryDTO {

    /** 操作主体标识 */
    private String userId;
    /** 用户所属部门列表 */
    private List<String> userDepts;
    /** 目标文档ID */
    private String documentId;
    /** 按目录过滤（可空） */
    private String directoryId;
    /** 按状态过滤（可空） */
    private String status;
    /** 标题/标签模糊搜索（可空） */
    private String keyword;

    /**
     * 构建查询 DTO。
     *
     * @param userId      操作主体标识
     * @param userDepts   用户所属部门列表
     * @param documentId  目标文档ID（可空）
     * @param directoryId 按目录过滤（可空）
     * @param status      按状态过滤（可空）
     * @param keyword     标题/标签模糊搜索（可空）
     * @return 查询 DTO
     */
    public static DocumentQueryDTO build(String userId, List<String> userDepts, String documentId,
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
