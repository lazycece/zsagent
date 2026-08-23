package com.lazycece.zsagent.application.knowledge.handler.etl.transformer;

import com.knuddels.jtokkit.api.EncodingType;
import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.utils.DefaultUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

import java.util.List;

/**
 * @author lazycece
 */
@ApplicationHandler
public class DocumentTokenTextSplitter implements DocumentTransformer {

    private final TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
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
            .withPunctuationMarks(List.of('。', '？', '！', '；', '.', '?', '!', '\n', ';', ':', '。'))
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
