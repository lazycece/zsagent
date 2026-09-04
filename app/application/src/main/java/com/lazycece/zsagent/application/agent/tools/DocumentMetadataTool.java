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
import com.lazycece.zsagent.domain.knowledge.model.Document;
import com.lazycece.zsagent.domain.knowledge.repository.DocumentRepository;
import com.lazycece.zsagent.domain.knowledge.valueobject.query.DocumentQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 文档元数据获取 function-calling 工具。 根据文档 ID 获取标题、作者、更新时间与访问链接，用于回答侧"引用溯源"标注。 工具名：get_document_metadata。
 *
 * <p>信任边界：仅允许读取当前会话用户（{@link ToolContext}）有权限阅读的文档元数据——无会话身份或文档越权一律返回不可用，
 * 禁止用 documentId 探测他人受限文档的标题/作者等元数据。 取数走仓储权限过滤查询（{@link DocumentRepository#findByDocumentId}）， 与
 * {@link DocumentPermissionTool} 共用同一信任边界。
 *
 * @author lazycece
 */
@Component
public class DocumentMetadataTool {

    private final DocumentRepository documentRepository;
    private final String docBaseUrl;

    public DocumentMetadataTool(
            DocumentRepository documentRepository,
            @Value("${zsagent.web.doc-base-url:}") String docBaseUrl) {
        this.documentRepository = documentRepository;
        this.docBaseUrl = docBaseUrl;
    }

    /**
     * 获取文档元数据（仅当前会话用户可读的文档）。
     *
     * @param documentId  目标知识文档ID
     * @param toolContext 工具调用上下文，携带当前会话用户
     * @return 文档元数据结果；无会话身份或无权限时 available=false 并附说明
     */
    @Tool(
            name = AgentToolsConstants.GET_DOCUMENT_METADATA,
            description = "根据文档ID获取标题、作者、更新时间与访问链接，用于回答时标注引用来源；" + "仅返回当前会话用户有权限阅读的文档元数据，无权限时返回不可用")
    public DocumentMetadataResult getDocumentMetadata(
            @ToolParam(description = "目标知识文档ID") String documentId, ToolContext toolContext) {
        Assert.notBlank(documentId, RespStatus.PARAM_ERROR, "documentId 不能为空");

        String currentUserId = resolveCurrentUserId(toolContext);
        if (StringUtils.isBlank(currentUserId)) {
            return DocumentMetadataResult.blocked("无法确认当前会话用户，拒绝返回文档元数据");
        }

        DocumentQuery query = new DocumentQuery();
        query.setUserId(currentUserId);
        query.setUserDepts(getUserDepts());
        query.setDocumentId(documentId);
        Document document = documentRepository.findByDocumentId(query);
        if (document == null) {
            return DocumentMetadataResult.blocked("文档不存在或当前用户无访问权限");
        }
        String link = normalizeBaseUrl(docBaseUrl) + "/docs/" + documentId;
        return DocumentMetadataResult.success(document, link);
    }

    /**
     * 从工具调用上下文解析当前会话用户；无上下文或未携带时返回空。
     */
    private String resolveCurrentUserId(ToolContext toolContext) {
        if (toolContext == null || toolContext.getContext() == null) {
            return null;
        }
        Object currentUserId =
                toolContext.getContext().get(DocumentPermissionTool.CONTEXT_KEY_CURRENT_USER_ID);
        return currentUserId == null ? null : String.valueOf(currentUserId);
    }

    /**
     * 获取用户所属部门列表。
     * TODO: 接入用户服务获取真实部门，当前 stub 返回空列表。
     */
    private List<String> getUserDepts() {
        return Collections.emptyList();
    }

    /**
     * 归一化文档基础地址，去掉尾部斜杠，避免拼出双斜杠链接。
     */
    private String normalizeBaseUrl(String baseUrl) {
        if (StringUtils.isBlank(baseUrl)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.stripEnd(baseUrl, "/");
    }

    /**
     * 文档元数据结果。
     *
     * @param available 是否成功获取到元数据
     * @param title     文档标题
     * @param author    作者（上传者用户ID，待用户服务接入后解析姓名）
     * @param updatedAt 更新时间（yyyy-MM-dd HH:mm:ss）
     * @param link      文档访问链接（baseUrl + /docs/{documentId}）
     * @param message   不可用时的原因说明
     */
    public record DocumentMetadataResult(
            boolean available,
            String title,
            String author,
            String updatedAt,
            String link,
            String message) {

        static DocumentMetadataResult success(Document document, String link) {
            return new DocumentMetadataResult(
                    true,
                    document.getTitle(),
                    document.getCreator(),
                    formatUpdateTime(document.getUpdateTime()),
                    link,
                    null);
        }

        static DocumentMetadataResult blocked(String message) {
            return new DocumentMetadataResult(false, null, null, null, null, message);
        }
    }

    private static String formatUpdateTime(LocalDateTime updateTime) {
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return updateTime == null ? null : dt.format(updateTime);
    }
}
