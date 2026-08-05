package com.lazycece.zsagent.facade.agent.api;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.agent.request.AskQuestionRequest;
import com.lazycece.zsagent.facade.agent.request.FeedbackRequest;
import com.lazycece.zsagent.facade.agent.result.FeedbackResult;
import reactor.core.publisher.Flux;

/**
 * Agent 命令门面接口
 *
 * @author lazycece
 */
public interface AgentCommandFacade {

    /**
     * 发起提问，返回 SSE 流式答案。
     *
     * @param request 提问请求
     * @return SSE 流式答案
     */
    Flux<String> askQuestion(AskQuestionRequest request);

    /**
     * 提交答案反馈。
     *
     * @param request 反馈请求
     * @return 操作结果
     */
    RespData<FeedbackResult> submitFeedback(FeedbackRequest request);
}
