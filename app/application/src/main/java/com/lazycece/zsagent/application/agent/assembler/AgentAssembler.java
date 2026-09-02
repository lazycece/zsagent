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
package com.lazycece.zsagent.application.agent.assembler;

import com.lazycece.rapidf.utils.DefaultUtils;
import com.lazycece.zsagent.domain.agent.enums.FeedbackType;
import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.AssistantMessageCmd;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.FeedbackCmd;
import com.lazycece.zsagent.domain.agent.valueobject.cmd.UserMessageCmd;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentMetadataKey;
import com.lazycece.zsagent.facade.agent.request.AskQuestionRequest;
import com.lazycece.zsagent.facade.agent.request.FeedbackRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.document.Document;

/**
 * Agent 数据组装器。 负责从 Request 对象构建领域值对象（Record）。
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
     * 从 FeedbackRequest 构建反馈命令。 将请求中的 type 字符串转换为 FeedbackType 枚举。
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

    /**
     * 从检索结果中提取来源引用。
     */
    public static List<SourceReference> assembleSourceReferenceList(List<Document> documents) {
        return DefaultUtils.defaultList(documents).stream()
                .map(AgentAssembler::assembleSourceReference)
                .collect(Collectors.toList());
    }

    private static SourceReference assembleSourceReference(Document doc) {
        Map<String, Object> metadata = doc.getMetadata();
        String documentId =
                (String) metadata.getOrDefault(DocumentMetadataKey.DOCUMENT_ID.getCode(), "");
        String documentTitle =
                (String) metadata.getOrDefault(DocumentMetadataKey.TITLE.getCode(), "未知文档");
        String chunkId = doc.getId();
        String text = doc.getText();
        String contentSnippet =
                text != null ? (text.length() > 200 ? text.substring(0, 200) : text) : "";
        float score = doc.getScore() == null ? 0.0f : doc.getScore().floatValue();
        return new SourceReference(documentId, documentTitle, chunkId, contentSnippet, score);
    }
}
