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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lazycece.zsagent.application.agent.tools.DocumentPermissionTool;
import com.lazycece.zsagent.application.agent.tools.DocumentPermissionTool.AccessResult;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ToolContext;

/**
 * {@link DocumentPermissionTool#checkUserPermission(String, String, ToolContext)} 权限守卫测试。
 *
 * @author lazycece
 */
public class DocumentPermissionToolTest {

    @Test
    public void checkUserPermission() {
        // 当前会话用户与操作者一致 → 按领域规则判定（创建者可写）
        DocumentRepository repository = mock(DocumentRepository.class);
        when(repository.findById("d1"))
                .thenReturn(newDocument("u1", Visibility.SPECIFIC, List.of("u1")));
        DocumentPermissionTool tool = new DocumentPermissionTool(repository);
        assertEquals("write", tool.checkUserPermission("u1", "d1", contextOf("u1")).access());

        // 公开文档对当前会话的普通读者只读
        when(repository.findById("d1")).thenReturn(newDocument("u1", Visibility.PUBLIC, List.of()));
        assertEquals("read", tool.checkUserPermission("u2", "d1", contextOf("u2")).access());

        // 未携带会话上下文时，仍按 userId 正常判定
        when(repository.findById("d1")).thenReturn(newDocument("u1", Visibility.PUBLIC, List.of()));
        assertEquals("write", tool.checkUserPermission("u1", "d1", null).access());

        // 文档不存在 → 无权限并提示不存在
        when(repository.findById("d9")).thenReturn(null);
        AccessResult notFound = tool.checkUserPermission("u1", "d9", contextOf("u1"));
        assertEquals("none", notFound.access());
        assertTrue(notFound.message().contains("不存在"));

        // userId 与当前会话用户不一致 → 拒绝并禁止查库，防跨用户探测
        DocumentRepository guardedRepository = mock(DocumentRepository.class);
        DocumentPermissionTool guardedTool = new DocumentPermissionTool(guardedRepository);
        AccessResult mismatch = guardedTool.checkUserPermission("u2", "d1", contextOf("u1"));
        assertEquals("none", mismatch.access());
        assertTrue(mismatch.message().contains("不一致"));
        verify(guardedRepository, never()).findById(anyString());
    }

    private Document newDocument(String creator, Visibility visibility, List<String> visibleTo) {
        Document document = new Document();
        document.setCreator(creator);
        document.setVisibility(visibility);
        document.setVisibleTo(visibleTo);
        return document;
    }

    private ToolContext contextOf(String currentUserId) {
        return new ToolContext(
                Map.of(DocumentPermissionTool.CONTEXT_KEY_CURRENT_USER_ID, currentUserId));
    }
}
