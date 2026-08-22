package com.lazycece.zsagent.application.knowledge.handler.etl;

import com.lazycece.rapidf.domain.anotation.ApplicationHandler;
import com.lazycece.rapidf.utils.UUIDUtils;
import com.lazycece.zsagent.domain.agent.model.KnowledgeChunk;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.valueobject.ParsedDocument;
import com.lazycece.zsagent.domain.knowledge.valueobject.Section;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档分块器，按段落递归切分并保留重叠。
 *
 * @author lazycece
 */
@ApplicationHandler
public class DocumentChunker {

    /** 目标 chunk 大小（字符） */
    private static final int CHUNK_SIZE = 500;
    /** 相邻 chunk 重叠（字符） */
    private static final int CHUNK_OVERLAP = 50;
    /** 最小 chunk 大小（小于此值合并到前一个） */
    private static final int MIN_CHUNK_SIZE = 100;

    /**
     * 将解析后的文档切分为知识块列表。
     */
    public List<KnowledgeChunk> chunk(ParsedDocument parsed, Document document) {
        List<KnowledgeChunk> chunks = new ArrayList<>();
        int chunkIndex = 0;

        for (Section section : parsed.sections()) {
            String text = section.content();
            if (StringUtils.isBlank(text)) {
                continue;
            }

            String[] paragraphs = text.split("\n\n");
            StringBuilder currentChunk = new StringBuilder();

            for (String paragraph : paragraphs) {
                if (StringUtils.isBlank(paragraph)) {
                    continue;
                }
                if (currentChunk.length() + paragraph.length() > CHUNK_SIZE
                        && currentChunk.length() >= MIN_CHUNK_SIZE) {
                    chunks.add(buildChunk(currentChunk.toString(), chunkIndex++,
                            section, document));
                    currentChunk = new StringBuilder(currentChunk.substring(
                            Math.max(0, currentChunk.length() - CHUNK_OVERLAP)));
                }
                currentChunk.append(paragraph).append("\n\n");
            }

            if (currentChunk.length() > 0) {
                chunks.add(buildChunk(currentChunk.toString(), chunkIndex++,
                        section, document));
            }
        }
        return chunks;
    }

    /**
     * 构建单个知识块。
     */
    private KnowledgeChunk buildChunk(String content, int chunkIndex,
                                      Section section, Document document) {
        KnowledgeChunk chunk = new KnowledgeChunk();
        chunk.setChunkId(UUIDUtils.uuid());
        chunk.setDocumentId(document.getDocumentId());
        chunk.setDocumentTitle(document.getTitle());
        chunk.setContent(content.trim());
        chunk.setTags(document.getTags());
        chunk.setPermissionType(document.getVisibility() != null
                ? document.getVisibility().getCode() : null);
        if (document.getVisibility() == Visibility.DEPARTMENT) {
            chunk.setPermissionDepts(document.getVisibleTo());
        } else if (document.getVisibility() == Visibility.SPECIFIC) {
            chunk.setPermissionUsers(document.getVisibleTo());
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("chunk_index", chunkIndex);
        metadata.put("page_number", section.pageNumber());
        metadata.put("heading_level", section.headingLevel());
        metadata.put("heading_text", section.headingText());
        chunk.setMetadata(metadata);
        return chunk;
    }
}
