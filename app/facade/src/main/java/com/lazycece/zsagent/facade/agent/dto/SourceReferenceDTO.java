package com.lazycece.zsagent.facade.agent.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 来源引用DTO
 *
 * @author lazycece
 */
@Getter
@Setter
public class SourceReferenceDTO {

    /** 来源文档ID */
    private String documentId;

    /** 文档标题 */
    private String documentTitle;

    /** 引用片段 */
    private String contentSnippet;

    /** 相似度得分 */
    private Float score;
}
