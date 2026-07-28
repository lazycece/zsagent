package com.lazycece.zsagent.domain.agent.model;

import com.lazycece.rapidf.domain.anotation.DomainEntity;
import com.lazycece.rapidf.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 知识块实体 —— RAG 检索的召回单位
 *
 * @author lazycece
 */
@Getter
@Setter
@DomainEntity
public class KnowledgeChunk extends Entity<String> {

    /** ES 文档 ID */
    private String chunkId;
    /** 来源文档ID */
    private String documentId;
    /** 文档标题 */
    private String documentTitle;
    /** 文本内容 */
    private String content;
    /** 向量表示（仅索引时使用） */
    private float[] embedding;
    /** 扩展元数据（页码、章节等） */
    private Map<String, Object> metadata;

    @Override
    public String getId() {
        return this.chunkId;
    }
}
