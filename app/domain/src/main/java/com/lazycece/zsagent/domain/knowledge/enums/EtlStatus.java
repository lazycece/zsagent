package com.lazycece.zsagent.domain.knowledge.enums;

import com.lazycece.rapidf.domain.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ETL 处理状态枚举
 *
 * @author lazycece
 */
@Getter
@AllArgsConstructor
public enum EtlStatus implements BaseEnum<String> {

    PENDING("pending", "等待处理"),
    PARSING("parsing", "正在解析"),
    CHUNKING("chunking", "正在分块"),
    ENRICHING("enriching", "正在生成摘要"),
    EMBEDDING("embedding", "正在向量化"),
    INDEXING("indexing", "正在索引"),
    COMPLETED("completed", "已完成"),
    FAILED("failed", "处理失败"),
    ;

    private final String code;
    private final String desc;
}
