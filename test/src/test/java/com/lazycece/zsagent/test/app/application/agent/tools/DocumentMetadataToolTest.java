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
package com.lazycece.zsagent.test.app.application.agent.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lazycece.zsagent.application.agent.tools.DocumentMetadataTool;
import com.lazycece.zsagent.application.agent.tools.DocumentMetadataTool.DocumentMetadataResult;
import com.lazycece.zsagent.application.agent.tools.DocumentPermissionTool;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentQuery;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ToolContext;

/**
 * {@link DocumentMetadataTool#getDocumentMetadata(String, ToolContext)} 引用溯源元数据工具测试。
 *
 * @author lazycece
 */
public class DocumentMetadataToolTest {

    private static final String BASE_URL = "https://kb.example.com";

    @Test
    public void getDocumentMetadata() {
        // 当前会话用户可读 → 返回完整元数据：标题、作者(creator)、更新时间(格式化)、完整可点击链接
        DocumentRepository repository = mock(DocumentRepository.class);
        when(repository.findByDocumentId(any(DocumentQuery.class)))
                .thenReturn(
                        newDocument("d1", "产品手册", "u1", LocalDateTime.of(2026, 9, 5, 10, 30, 0)));
        DocumentMetadataTool tool = new DocumentMetadataTool(repository, BASE_URL);

        DocumentMetadataResult result = tool.getDocumentMetadata("d1", contextOf("u1"));
        assertEquals("产品手册", result.title());
        assertEquals("u1", result.author());
        assertEquals("2026-09-05 10:30:00", result.updatedAt());
        assertEquals(BASE_URL + "/docs/d1", result.link());
        assertTrue(result.available());
        assertNull(result.message());

        // 文档不存在或无权限（仓储按权限过滤返回 null）→ 不可用
        when(repository.findByDocumentId(any(DocumentQuery.class))).thenReturn(null);
        DocumentMetadataResult notFound = tool.getDocumentMetadata("d9", contextOf("u1"));
        assertFalse(notFound.available());
        assertTrue(notFound.message().contains("不存在") || notFound.message().contains("无权限"));

        // baseUrl 尾部斜杠归一，避免双斜杠
        DocumentMetadataTool trailingSlashTool =
                new DocumentMetadataTool(repository, "https://kb.example.com/");
        when(repository.findByDocumentId(any(DocumentQuery.class)))
                .thenReturn(
                        newDocument("d1", "产品手册", "u1", LocalDateTime.of(2026, 9, 5, 10, 30, 0)));
        assertEquals(
                "https://kb.example.com/docs/d1",
                trailingSlashTool.getDocumentMetadata("d1", contextOf("u1")).link());

        // 未携带当前会话用户（无身份可授权）→ 拒绝返回元数据且不查库
        DocumentRepository guardedRepository = mock(DocumentRepository.class);
        DocumentMetadataTool guardedTool = new DocumentMetadataTool(guardedRepository, BASE_URL);
        DocumentMetadataResult noUser = guardedTool.getDocumentMetadata("d1", contextOf());
        assertFalse(noUser.available());
        assertTrue(noUser.message().contains("会话用户"));
        verify(guardedRepository, never()).findByDocumentId(any(DocumentQuery.class));
    }

    private Document newDocument(
            String documentId, String title, String creator, LocalDateTime updateTime) {
        Document document = new Document();
        document.setDocumentId(documentId);
        document.setTitle(title);
        document.setCreator(creator);
        document.setUpdateTime(updateTime);
        return document;
    }

    private ToolContext contextOf(String currentUserId) {
        return new ToolContext(
                Map.of(DocumentPermissionTool.CONTEXT_KEY_CURRENT_USER_ID, currentUserId));
    }

    private ToolContext contextOf() {
        return new ToolContext(Map.of());
    }
}
