package com.lazycece.zsagent.application.agent.assembler;

import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import com.lazycece.zsagent.domain.agent.valueobject.AssistantMessageRecord;
import com.lazycece.zsagent.domain.agent.valueobject.FeedbackRecord;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import com.lazycece.zsagent.domain.agent.valueobject.UserMessageRecord;
import com.lazycece.zsagent.facade.agent.request.AskQuestionRequest;
import com.lazycece.zsagent.facade.agent.request.FeedbackRequest;

import java.util.List;

/**
 * Agent 数据组装器。
 * 负责从 Request 对象构建领域值对象（Record）。
 *
 * @author lazycece
 */
public final class AgentAssembler {

    /**
     * 从 AskQuestionRequest 构建用户提问记录。
     */
    public static UserMessageRecord toUserMessageRecord(AskQuestionRequest request) {
        return new UserMessageRecord(
                request.getUserId(),
                request.getConversationId(),
                request.getQuestion()
        );
    }

    /**
     * 构建助手回答记录。
     */
    public static AssistantMessageRecord toAssistantMessageRecord(
            String userId, String conversationId, String content, List<SourceReference> sources) {
        return new AssistantMessageRecord(userId, conversationId, content, sources);
    }

    /**
     * 从 FeedbackRequest 构建反馈记录。
     * 将请求中的 type 字符串转换为 FeedbackType 枚举。
     */
    public static FeedbackRecord toFeedbackRecord(FeedbackRequest request) {
        FeedbackType feedbackType = FeedbackType.valueOf(request.getType().toUpperCase());
        return new FeedbackRecord(
                request.getUserId(),
                request.getConversationId(),
                request.getMessageId(),
                feedbackType,
                request.getReason()
        );
    }
}
