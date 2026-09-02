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
package com.lazycece.zsagent.infra.acl.config;

import lombok.Getter;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author lazycece
 */
@Configuration
public class EmbeddingConfig {

    @Value("${spring.ai.dashscope.embedding.base-url}")
    private String baseUrl;

    @Value("${spring.ai.dashscope.embedding.api-key}")
    private String apiKey;

    @Value("${spring.ai.dashscope.embedding.options.model}")
    private String model;

    @Value("${spring.ai.dashscope.embedding.options.dimensions}")
    private Integer dimensions;

    @Getter
    @Value("${spring.ai.dashscope.embedding.batch-num}")
    private Integer bathNum;

    @Bean
    @Primary
    public EmbeddingModel embeddingModel() {
        // Spring AI 2.0：OpenAiEmbeddingOptions 自带 baseUrl、apiKey 等连接信息
        // 不再需要单独构建 OpenAiApi，官方 SDK 自动处理 /embeddings 路径
        OpenAiEmbeddingOptions options =
                OpenAiEmbeddingOptions.builder()
                        .baseUrl(baseUrl)
                        .apiKey(apiKey)
                        .model(model)
                        .dimensions(dimensions)
                        .build();

        return OpenAiEmbeddingModel.builder()
                .options(options)
                .metadataMode(MetadataMode.EMBED)
                .build();
    }
}
