package com.lazycece.zsagent.application.agent.handler.rag.pre;

import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.stereotype.Component;

/**
 * MVP 直通查询处理器（Stage 1）。
 * 不对用户原始问题做任何变换，原文直送检索阶段。
 * 后续可替换为 LLM 驱动的查询扩展、多角度查询生成等。
 *
 * @author lazycece
 */
@Component
public class PassThroughQueryTransformer implements QueryTransformer {

    @Override
    public Query transform(Query query) {
        return query;
    }
}
