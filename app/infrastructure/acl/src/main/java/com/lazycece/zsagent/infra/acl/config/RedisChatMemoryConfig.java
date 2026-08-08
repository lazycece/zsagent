package com.lazycece.zsagent.infra.acl.config;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对话记忆基础设施配置。
 * {@link ChatMemoryRepository}（RedisChatMemoryRepository）由
 * {@code RedisChatMemoryAutoConfiguration} 自动创建，此处仅基于它构建
 * {@link ChatMemory} 和 {@link MessageChatMemoryAdvisor} Bean。
 *
 * @author lazycece
 */
@Configuration
public class RedisChatMemoryConfig {

    /**
     * 基于自动配置的 ChatMemoryRepository（Redis 实现）构建 ChatMemory。
     * MessageWindowChatMemory 在 repository 之上提供窗口截断、自动追加等能力。
     */
    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .build();
    }

    /**
     * 创建 MessageChatMemoryAdvisor Bean。
     * 注入 ChatClient 的 advisor 链后，自动在每次对话中注入历史上下文
     * 并在流结束后将本轮问答追加到 ChatMemory。
     */
    @Bean
    public MessageChatMemoryAdvisor messageChatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }
}
