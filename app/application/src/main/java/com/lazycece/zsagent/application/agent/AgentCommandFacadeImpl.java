package com.lazycece.zsagent.application.agent;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.application.agent.assembler.AgentAssembler;
import com.lazycece.zsagent.application.agent.rag.post.DocumentCachePostProcessor;
import com.lazycece.zsagent.domain.agent.service.ConversationDomainService;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import com.lazycece.zsagent.facade.agent.api.AgentCommandFacade;
import com.lazycece.zsagent.facade.agent.request.AskQuestionRequest;
import com.lazycece.zsagent.facade.agent.request.FeedbackRequest;
import com.lazycece.zsagent.facade.agent.result.FeedbackResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;

/**
 * Agent 命令门面实现。
 * 负责问答流程编排：记录用户消息 → 执行 RAG 流水线 → 流式返回 → 记录助手消息。
 *
 * @author lazycece
 */
@Primary
@ApplicationService
public class AgentCommandFacadeImpl implements AgentCommandFacade {

    private static final Logger log = LoggerFactory.getLogger(AgentCommandFacadeImpl.class);

    private final ChatClient.Builder chatClientBuilder;
    private final RetrievalAugmentationAdvisor ragAdvisor;
    private final MessageChatMemoryAdvisor memoryAdvisor;
    private final ConversationDomainService conversationDomainService;
    private final DocumentCachePostProcessor documentCachePostProcessor;

    public AgentCommandFacadeImpl(
            ChatClient.Builder chatClientBuilder,
            RetrievalAugmentationAdvisor ragAdvisor,
            MessageChatMemoryAdvisor memoryAdvisor,
            ConversationDomainService conversationDomainService,
            DocumentCachePostProcessor documentCachePostProcessor) {
        this.chatClientBuilder = chatClientBuilder;
        this.ragAdvisor = ragAdvisor;
        this.memoryAdvisor = memoryAdvisor;
        this.conversationDomainService = conversationDomainService;
        this.documentCachePostProcessor = documentCachePostProcessor;
    }

    @Override
    public Flux<ServerSentEvent<String>> askQuestion(AskQuestionRequest request) {
        String userId = request.getUserId();
        String conversationId = request.getConversationId();
        String question = request.getQuestion();

        // 阶段 A: 记录用户消息（domain 层）
        conversationDomainService.recordUserMessage(
                AgentAssembler.assembleUserMessageCmd(request));

        // 阶段 B: 通过 ChatClient + RAG Advisor + Memory Advisor 执行流水线
        StringBuilder fullAnswer = new StringBuilder();

        return chatClientBuilder.build().prompt()
                .advisors(ragAdvisor, memoryAdvisor)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .advisors(a -> a.param("user_id", userId))
                .advisors(a -> a.param("user_depts", getUserDepts()))
                .user(question)
                .stream()
                .content()
                .doOnNext(fullAnswer::append)
                .doOnComplete(() -> {
                    // 阶段 C: 流结束后记录完整的助手消息（含来源引用）
                    List<Document> docs = documentCachePostProcessor.getLastRetrievedDocuments(conversationId);
                    List<SourceReference> sources = documentCachePostProcessor.extractSources(docs);
                    conversationDomainService.recordAssistantMessage(
                            AgentAssembler.assembleAssistantMessageCmd(
                                    userId, conversationId, fullAnswer.toString(), sources));
                    documentCachePostProcessor.clearDocuments(conversationId);
                    log.info("问答完成: userId={}, conversationId={}, 答案长度={}, 来源数={}",
                            userId, conversationId, fullAnswer.length(), sources.size());
                })
                .doOnError(error -> {
                    log.error("RAG 流水线异常: userId={}, conversationId={}", userId, conversationId, error);
                })
                .map(chunk -> ServerSentEvent.<String>builder().data(chunk).build())
                .concatWith(Flux.just(ServerSentEvent.<String>builder().event("done").data("[DONE]").build()));
    }

    @Override
    public RespData<FeedbackResult> submitFeedback(FeedbackRequest request) {
        conversationDomainService.recordFeedback(
                AgentAssembler.assembleFeedbackCmd(request));
        return RespData.success(new FeedbackResult());
    }

    /**
     * 获取当前用户的所属部门列表。
     * TODO: 接入用户服务获取真实部门，当前 stub 返回空列表。
     */
    private List<String> getUserDepts() {
        return Collections.emptyList();
    }
}
