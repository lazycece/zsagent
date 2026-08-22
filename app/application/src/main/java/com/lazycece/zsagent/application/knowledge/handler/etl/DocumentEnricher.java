package com.lazycece.zsagent.application.knowledge.handler.etl;

import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.utils.json.JsonUtils;
import com.lazycece.zsagent.domain.knowledge.valueobject.EnrichResult;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

/**
 * 文档增强器，通过 LLM 生成摘要与候选标签。
 *
 * @author lazycece
 */
@ApplicationHandler
public class DocumentEnricher {

    private static final Logger log = LoggerFactory.getLogger(DocumentEnricher.class);

    private static final int PREVIEW_LENGTH = 2000;

    private final ChatClient.Builder chatClientBuilder;

    public DocumentEnricher(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    /**
     * 生成文档摘要与标签。
     */
    public EnrichResult enrich(ParsedDocument parsed, String title) {
        try {
            String previewContent = parsed.fullText();
            if (previewContent.length() > PREVIEW_LENGTH) {
                previewContent = previewContent.substring(0, PREVIEW_LENGTH);
            }
            String prompt = buildPrompt(title, previewContent);
            String response = chatClientBuilder.build().prompt().user(prompt).call().content();
            EnrichResult result = JsonUtils.parseObject(response, EnrichResult.class);
            if (result == null) {
                return EnrichResult.empty();
            }
            return result;
        } catch (Exception e) {
            log.warn("摘要/标签生成失败，返回空结果, title={}", title, e);
            return EnrichResult.empty();
        }
    }

    private String buildPrompt(String title, String content) {
        return "你是一个知识管理助手。根据以下文档内容完成两个任务：\n"
                + "1. 生成一段简洁的摘要（不超过 200 字）\n"
                + "2. 提取 3~5 个关键词标签\n\n"
                + "## 文档标题\n" + title + "\n\n"
                + "## 文档内容（前 " + PREVIEW_LENGTH + " 字）\n" + content + "\n\n"
                + "## 输出格式（严格按 JSON 输出，不要输出其他内容）\n"
                + "{\"summary\": \"文档摘要\", \"tags\": [\"标签1\", \"标签2\", \"标签3\"]}";
    }
}
