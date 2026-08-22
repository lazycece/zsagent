package com.lazycece.zsagent.application.agent.assembler;

import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.AssistantMessageCmd;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.FeedbackCmd;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.UserMessageCmd;
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
     * 从 AskQuestionRequest 构建用户提问命令。
     */
    public static UserMessageCmd assembleUserMessageCmd(AskQuestionRequest request) {
        UserMessageCmd command = new UserMessageCmd();
        command.setUserId(request.getUserId());
        command.setConversationId(request.getConversationId());
        command.setContent(request.getQuestion());
        return command;
    }

    /**
     * 构建助手回答命令。
     */
    public static AssistantMessageCmd assembleAssistantMessageCmd(
            String userId, String conversationId, String content, List<SourceReference> sources) {
        AssistantMessageCmd command = new AssistantMessageCmd();
        command.setUserId(userId);
        command.setConversationId(conversationId);
        command.setContent(content);
        command.setSources(sources);
        return command;
    }

    /**
     * 从 FeedbackRequest 构建反馈命令。
     * 将请求中的 type 字符串转换为 FeedbackType 枚举。
     */
    public static FeedbackCmd assembleFeedbackCmd(FeedbackRequest request) {
        FeedbackType feedbackType = FeedbackType.valueOf(request.getType().toUpperCase());
        FeedbackCmd command = new FeedbackCmd();
        command.setUserId(request.getUserId());
        command.setConversationId(request.getConversationId());
        command.setMessageId(request.getMessageId());
        command.setType(feedbackType);
        command.setReason(request.getReason());
        return command;
    }
}
