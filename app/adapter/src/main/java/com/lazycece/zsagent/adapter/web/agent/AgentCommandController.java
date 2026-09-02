/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lazycece.zsagent.adapter.web.agent;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.agent.api.AgentCommandFacade;
import com.lazycece.zsagent.facade.agent.request.AskQuestionRequest;
import com.lazycece.zsagent.facade.agent.request.FeedbackRequest;
import com.lazycece.zsagent.facade.agent.result.FeedbackResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @PostMapping(value = "/ask-question", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<ServerSentEvent<String>>> askQuestion(
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
