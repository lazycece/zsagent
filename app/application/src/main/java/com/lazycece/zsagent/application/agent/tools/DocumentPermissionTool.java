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
package com.lazycece.zsagent.application.agent.tools;

import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.zsagent.domain.agent.constants.AgentToolsConstants;
import com.lazycece.zsagent.domain.knowledge.enums.DocumentAccessLevel;
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 文档权限检查 function-calling 工具。 判定指定用户对目标知识文档的访问级别（read/write/none），供问答引用兜底与 文档读写类工具前置守卫复用。
 * 工具名：check_user_permission。
 *
 * <p>信任边界：当工具调用上下文（{@link ToolContext}）携带当前会话用户时，模型传入的 userId 必须与之一致，
 * 否则拒绝并返回 none，禁止跨用户探测他人权限。
 *
 * @author lazycece
 */
@Component
public class DocumentPermissionTool {

    /**
     * 工具调用上下文中当前会话用户的键
     */
    public static final String CONTEXT_KEY_CURRENT_USER_ID = "currentUserId";

    private final DocumentRepository documentRepository;

    public DocumentPermissionTool(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * 检查用户对文档的访问级别。
     *
     * @param userId      操作者用户ID
     * @param documentId  目标知识文档ID
     * @param toolContext 工具调用上下文，携带当前会话用户
     * @return 访问级别结果
     */
    @Tool(
            name = AgentToolsConstants.CHECK_USER_PERMISSION,
            description = "检查指定用户对某篇知识文档的访问级别，用于在引用文档内容或执行文档读写操作前进行权限判断，返回 read/write/none 三态")
    public AccessResult checkUserPermission(
            @ToolParam(description = "操作者用户ID，需为当前会话用户") String userId,
            @ToolParam(description = "目标知识文档ID") String documentId,
            ToolContext toolContext) {
        Assert.notBlank(userId, RespStatus.PARAM_ERROR, "userId 不能为空");
        Assert.notBlank(documentId, RespStatus.PARAM_ERROR, "documentId 不能为空");

        if (toolContext != null) {
            Object currentUserId = toolContext.getContext().get(CONTEXT_KEY_CURRENT_USER_ID);
            if (currentUserId != null && !String.valueOf(currentUserId).equals(userId)) {
                return AccessResult.none("userId 与当前会话用户不一致，禁止跨用户探测");
            }
        }

        Document document = documentRepository.findById(documentId);
        if (document == null) {
            return AccessResult.none("文档不存在或无权访问");
        }
        return AccessResult.of(document.resolveAccess(userId, getUserDepts()));
    }

    /**
     * 获取用户所属部门列表。
     * TODO: 接入用户服务获取真实部门，当前 stub 返回空列表。
     */
    private List<String> getUserDepts() {
        return Collections.emptyList();
    }

    /**
     * 权限检查结果。
     *
     * @param access  访问级别：none / read / write
     * @param message 结果说明
     */
    public record AccessResult(String access, String message) {

        static AccessResult of(DocumentAccessLevel level) {
            return new AccessResult(level.getCode(), "访问级别：" + level.getDesc());
        }

        static AccessResult none(String message) {
            return new AccessResult(DocumentAccessLevel.NONE.getCode(), message);
        }
    }
}
