# MVP Function Calling 可落地拆解

| 文档版本 | 修改日期 | 修改人 | 说明 |
|---------|---------|-----|------|
| V1.0 | 2026-09-03 | ai | MVP 现有能力盘点，仅收录可直接封装为 function calling 的工具 |

---

## 1. 背景与范围

MVP（智能问答 + 知识管理 + 基础对话界面）后端能力已基本实现，但当前问答 Agent 是一条写死的单路 RAG 管道（`RetrievalAugmentationAdvisor`），全仓未注册任何 LLM 工具 / function calling。

本文档只收录 **现有已实现、可直接封装成 function calling 工具** 的能力，每条附拆解原因。依赖新增后端接口或未落地能力（如正文全文读取、回收站列表等）**不在此范围**，详见 §5。

## 2. 拆解标准

一个现有能力要收进本文，需同时满足：

1. **已有可复用的后端能力**：由现有 facade / repository / service 承载，封装成本低；
2. **需要模型自主决策**：是否调用、参数取值、是否连续多步，无法由固定 UI 流程写死；
3. **具备明确 JSON 入参 / 出参契约**：与现有 Request/Result/DTO 一一对应；
4. **读操作可直接开放，写操作须绑定 userId + 权限 + 关键步骤确认**。

## 3. 可落地工具清单

工具按用途分 **检索/查询（读）** 与 **文档/目录操作（写）** 两组。封装方式统一：在 `application/agent/tools` 下以 Spring AI `@Tool` 方法包装现有 facade / domain 服务，工具名使用英文 snake_case。

### 3.1 读：检索 / 查询

#### `search_knowledge(query, topK)`
- **复用现有能力**：ES VectorStore 检索链路（问答已用）、`domain.agent.repository.KnowledgeChunkRepository.search(query, topK)`、`infra.acl.utils.FilterExpressionUtils.permissionFilter(userId, userDepts)`（现成但未接线）。
- **原因**：这是把「问答」从固定 RAG 升级为 agent 决策的核心拆解。模型先自主判断问题是否属于知识库范畴、需要检索哪几个子问题，再决定是否调用、调几次；同时补齐当前缺失的检索侧权限过滤与"库外问题不硬答"能力（BRD 4.1 流程第 2 步）。

#### `list_documents(keyword, directoryId, status, page, size)`
- **复用现有能力**：`facade.knowledge.api.DocumentQueryFacade.listDocuments`（已带 SQL 侧权限过滤）。
- **原因**：承载"有几篇微服务文档 / 最近的文档 / 该目录下有哪些"这类**事实性**问题——纯 RAG 无法给出准确计数与列表，必须由元数据检索工具回答，结果可结构化返回给模型拼入答案。

#### `get_document_detail(documentId)`
- **复用现有能力**：`DocumentQueryFacade.getDocument`。
- **原因**：让模型拿到结构化元数据（标题/摘要/标签/版本/目录/可见范围/ETL 状态），用于溯源、总结、判断某文档是否可用于作答。

#### `list_directory_children(directoryId)` / `get_directory_tree()`
- **复用现有能力**：`DirectoryQueryFacade.listChildren` / `tree`。
- **原因**：支撑"定位知识该放/该找在哪里"的导航类问题，模型可逐级展开目录结构再决定后续操作。

#### `get_conversation(conversationId)` / `list_conversations(userId, page, size)`
- **复用现有能力**：`facade.agent.api.AgentQueryFacade.getConversation` / `listConversations`。
- **原因**：让模型读取历史会话作为事实上下文，支撑"基于我之前问过的……"类指令，弥补当前 RAG 管道只带当前会话窗口、无法引用更早会话的局限。

### 3.2 写：文档 / 目录操作

> 写操作一律经统一封装注入当前 `userId`，并遵循"关键步骤由用户确认"原则（BRD 4.5）。

#### `create_document(filePath, directoryId, title, tags, visibility, visibleTo)`
- **复用现有能力**：`DocumentCommandFacade.create`（触发异步 ETL：解析→摘要/标签→embedding→发布）。
- **原因**：让用户在对话中即可完成"把上传的文件建成文档并归档到某目录"；文件上传（`FileCommandFacade.upload`）保持为 UI/手工步骤产出 `filePath`，再交给本工具——字节流不适合做工具入参。

#### `update_document_metadata(documentId, title, tags, directoryId, visibility, visibleTo)`
- **复用现有能力**：`DocumentCommandFacade.updateMetadata`。
- **原因**：标签/可见范围的修正是典型的"对话式微调"场景，模型自主决定改哪些字段、填什么值。

#### `update_document_content(documentId, newContent)`
- **复用现有能力**：`DocumentCommandFacade.updateContent`（自动生成新版本 + 触发 ETL 重处理）。
- **原因**：版本管理是 MVP 卖点，且该能力链路完整；改写后为异步任务，配合 `get_etl_status` 追踪结果。

#### `delete_document(documentId)` / `restore_document(documentId)`
- **复用现有能力**：`DocumentCommandFacade.delete`（进回收站，软删）/ `restore`。
- **原因**：删除属高风险写操作，正是需要模型理解意图 + 用户确认 + 审计的典型场景；恢复同理。

#### `list_document_versions(documentId)` / `rollback_document(documentId, versionNumber)`
- **复用现有能力**：`DocumentQueryFacade.listVersions` + `DocumentCommandFacade.rollback`。
- **原因**：版本回滚是决策类操作（回滚到哪一版由模型+用户共同判定），工具能先取版本列表再让用户选目标版，天然适合 function calling。

#### `create_directory(parentId, name)` / `rename_directory(directoryId, newName)` / `move_directory(directoryId, newParentId)` / `delete_directory(directoryId)`
- **复用现有能力**：`DirectoryCommandFacade.create/rename/move/delete`（delete 已带空目录守卫）。
- **原因**：目录编排动作简单且互斥，四者参数语义清晰，可支撑"帮我把培训资料整理到一个新目录"这类多步指令。

## 4. 落地方式要点

- **工具归属与分层**：`@Tool` 定义放 `app/application/.../agent/tools`，包装的是同层/下层已实现服务，不引入反向依赖、不新建后端接口。
- **userId 注入**：当前 `userId` 由请求体携带（无 header 校验），工具封装处统一读取并透传，写操作显式二次确认。
- **与流式问答共存**：默认问答仍走现有流式 RAG；仅在模型判定需要上述工具时按需调用，不强制每个问题都走多轮 tool round-trip，避免牺牲体验与延迟。

## 5. 暂不收录（现有能力缺口，不属本文"可落地"范围）

| 能力 | 为何不收 |
|------|---------|
| `read_document_content`（读文档正文/分块/页码） | DocumentDTO 不含正文，需**新增正文/chunk 读取接口**后才可落地 |
| 回收站列表 / 文档计数 / 归档 / 文件下载 | 无对应已实现 facade，属缺口 |
| ETL 全链路（解析/切分/摘要/embedding/索引） | 异步副作用长任务，非同步工具形态 |
| 回答反馈 `submitFeedback` | 用户 UI 行为，非模型决策对象 |
| 纯文件上传 | 字节流不宜作工具入参，保持 UI/手工步骤先行 |
