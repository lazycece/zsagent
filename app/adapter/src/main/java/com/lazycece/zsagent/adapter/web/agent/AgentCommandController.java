package com.lazycece.zsagent.adapter.web.agent;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.agent.api.AgentCommandFacade;
import com.lazycece.zsagent.facade.agent.request.AskQuestionRequest;
import com.lazycece.zsagent.facade.agent.request.FeedbackRequest;
import com.lazycece.zsagent.facade.agent.result.FeedbackResult;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Agent 命令控制器，直接实现门面接口，仅负责请求转发。
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/agent")
public class AgentCommandController implements AgentCommandFacade {

    private final AgentCommandFacade agentCommandFacade;

    public AgentCommandController(AgentCommandFacade agentCommandFacade) {
        this.agentCommandFacade = agentCommandFacade;
    }

    /**
     * 流式问答 — SSE。
     */
    @Override
    @PostMapping(value = "/ask-question", produces ="text/event-stream;charset=utf-8")
    public Flux<ServerSentEvent<String>> askQuestion(
            @Validated @RequestBody AskQuestionRequest request) {
        return agentCommandFacade.askQuestion(request);
    }

    /**
     * 提交反馈。
     */
    @Override
    @PostMapping("/submit-feedback")
    public RespData<FeedbackResult> submitFeedback(
            @Validated @RequestBody FeedbackRequest request) {
        return agentCommandFacade.submitFeedback(request);
    }
}
