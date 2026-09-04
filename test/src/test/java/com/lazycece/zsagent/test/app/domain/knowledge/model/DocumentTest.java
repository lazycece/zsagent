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
package com.lazycece.zsagent.test.app.domain.knowledge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lazycece.zsagent.domain.knowledge.enums.DocumentAccessLevel;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * {@link Document#resolveAccess(String, List)} 访问级别判定测试。
 *
 * @author lazycece
 */
public class DocumentTest {

    @Test
    public void resolveAccess() {
        // 创建者（即使不在可见白名单内）始终可写
        Document creatorDoc = newDocument("u1", Visibility.SPECIFIC, List.of("u2"));
        assertEquals(DocumentAccessLevel.WRITE, creatorDoc.resolveAccess("u1", List.of()));
        // 可见范围或可见列表为空时也不影响创建者写权限
        assertEquals(
                DocumentAccessLevel.WRITE,
                newDocument("u1", Visibility.PUBLIC, List.of()).resolveAccess("u1", List.of()));
        assertEquals(
                DocumentAccessLevel.WRITE, newDocument("u1", null, null).resolveAccess("u1", null));

        // 公开文档对任意非创建者只读
        Document publicDoc = newDocument("u1", Visibility.PUBLIC, List.of());
        assertEquals(DocumentAccessLevel.READ, publicDoc.resolveAccess("u2", List.of()));

        // 指定可见：命中白名单只读，未命中无权限
        Document specificDoc = newDocument("u1", Visibility.SPECIFIC, List.of("u2", "u3"));
        assertEquals(DocumentAccessLevel.READ, specificDoc.resolveAccess("u3", List.of()));
        assertEquals(DocumentAccessLevel.NONE, specificDoc.resolveAccess("u4", List.of()));
        // 指定可见但可见列表为空 → 无权限
        assertEquals(
                DocumentAccessLevel.NONE,
                newDocument("u1", Visibility.SPECIFIC, List.of()).resolveAccess("u2", List.of()));

        // 部门可见：命中用户所属部门只读
        Document deptDoc = newDocument("u1", Visibility.DEPARTMENT, List.of("deptA", "deptB"));
        assertEquals(DocumentAccessLevel.READ, deptDoc.resolveAccess("u2", List.of("deptB")));
        // 部门可见但用户无部门或部门不匹配 → 无权限
        assertEquals(DocumentAccessLevel.NONE, deptDoc.resolveAccess("u2", List.of("deptC")));
        assertEquals(DocumentAccessLevel.NONE, deptDoc.resolveAccess("u2", List.of()));
        assertEquals(DocumentAccessLevel.NONE, deptDoc.resolveAccess("u2", null));

        // 空 userId → 无权限
        assertEquals(DocumentAccessLevel.NONE, publicDoc.resolveAccess(" ", List.of()));
        assertEquals(DocumentAccessLevel.NONE, publicDoc.resolveAccess(null, List.of()));
    }

    private Document newDocument(String creator, Visibility visibility, List<String> visibleTo) {
        Document document = new Document();
        document.setCreator(creator);
        document.setVisibility(visibility);
        document.setVisibleTo(visibleTo);
        return document;
    }
}
