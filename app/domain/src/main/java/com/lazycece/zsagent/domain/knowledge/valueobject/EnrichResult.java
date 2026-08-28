package com.lazycece.zsagent.domain.knowledge.valueobject;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * LLM 增强结果。
 *
 * @author lazycece
 */
@Getter
@Setter
public class EnrichResult {

    private boolean empty = false;

    /** 文档摘要 */
    private String summary;
    /** 候选标签 */
    private List<String> tags;

    public static EnrichResult empty() {
        EnrichResult result = new EnrichResult();
        result.setEmpty(true);
        result.setSummary("");
        result.setTags(new ArrayList<>());
        return result;
    }
}
