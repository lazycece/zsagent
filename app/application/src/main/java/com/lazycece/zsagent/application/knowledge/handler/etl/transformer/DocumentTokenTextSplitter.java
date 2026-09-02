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
package com.lazycece.zsagent.application.knowledge.handler.etl.transformer;

import com.knuddels.jtokkit.api.EncodingType;
import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.utils.DefaultUtils;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

/**
 * @author lazycece
 */
@ApplicationHandler
public class DocumentTokenTextSplitter implements DocumentTransformer {

    private final TokenTextSplitter tokenTextSplitter =
            TokenTextSplitter.builder()
                    // 分词器编码类型（默认值：CL100K_BASE）。支持的值包括 CL100K_BASE、P50K_BASE 和 O200K_BASE
                    .withEncodingType(EncodingType.CL100K_BASE)
                    // 每个文本块的目标大小（以词元为单位）
                    .withChunkSize(500)
                    // 每个文本块的最小长度（以字符为单位）
                    .withMinChunkSizeChars(200)
                    // 要包含的数据块的最小长度（默认值：5）
                    .withMinChunkLengthToEmbed(10)
                    // 从文本生成的最大块数
                    .withMaxNumChunks(10000)
                    // 是否在数据块中保留分隔符（例如换行符）
                    .withKeepSeparator(true)
                    // 用于分割句子的字符列表
                    .withPunctuationMarks(
                            List.of('。', '？', '！', '；', '.', '?', '!', '\n', ';', ':', '。'))
                    //
                    .build();

    @Override
    public List<Document> apply(List<Document> documents) {
        if (DefaultUtils.defaultList(documents).isEmpty()) {
            return List.of();
        }
        return tokenTextSplitter.apply(documents);
    }
}
