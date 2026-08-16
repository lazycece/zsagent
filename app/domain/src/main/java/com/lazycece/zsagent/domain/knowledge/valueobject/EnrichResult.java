package com.lazycece.zsagent.domain.knowledge.valueobject;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * LLM 增强结果。
 *
 * @author lazycece
 */
@Getter
@Setter
public class EnrichResult {

    /** 文档摘要 */
    private String summary;
    /** 候选标签 */
    private List<String> tags;

    public static EnrichResult empty() {
        EnrichResult result = new EnrichResult();
        result.setSummary("");
        result.setTags(new ArrayList<>());
        return result;
    }
}
