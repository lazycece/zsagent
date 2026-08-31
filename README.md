# ZSAGENT（知枢）

基于 RAG 的智能知识库平台 Agent， 项目思路详见 [知识库平台AGENT](/docs/brd/知识库平台 Agent.md)

## 核心能力
当前已实现的核心能力如下：(功能持续丰富中...)

| 模块 | 说明                                                                                  |
|------|-------------------------------------------------------------------------------------|
| 智能问答 | RAG 增强问答、SSE 流式输出、多轮对话（Redis 会话记忆）、引用溯源、答案反馈（有用/无用）                                 |
| 知识管理 | 上传、目录管理、文档管理、多格式解析入库（PDF/Word/Markdown/HTML/TXT/DOCX）、分块与摘要、标签、版本管理与回滚、回收站、ETL 状态跟踪 |

![agent_chat.png](/docs/picture/agent_chat.png)
![agent_chat.png](/docs/picture/document_manage.png)

## 技术栈

### 后端

- **Java 17 · Spring Boot 4.1.0 · Spring AI 2.0.0**
- **MyBatis 4.1 + MySQL 8.0.27 + Druid 1.2.15**（连接池）
- **Elasticsearch**（向量检索） · **Redis**（会话记忆与缓存）
- **AI 模型**：DeepSeek（对话）、Embedding（`text-embedding-v4`）

### 前端

- **Vue 3 + TypeScript + Vite**，基于 [Vue Vben Admin v5](https://github.com/vbenjs/vue-vben-admin)（`web-antd` / Ant Design Vue）
- 代码位于仓库 [zsagent-frontend](https://github.com/lazycece/zsagent-frontend)

## 工程架构
基于 [**DDLA**](https://github.com/lazycece/ddla)实现：

```
.
|-- app
|   |-- adapter          # 适配器层（web/consumer/job/mobile）
|   |-- application      # 应用层
|   |-- domain           # 领域层
|   |-- facade           # 门面层
|   `-- infrastructure
|       |-- acl          # 防腐层（仓储实现、文件解析、缓存、配置）
|       |-- dal          # 数据访问层（po/mapper/dto）
|       `-- integration  # 系统集成（如 user 下游服务）
|-- bootstrap            # 应用启动器与配置
|-- conf                 # 环境配置（environment/*.properties）、代码规范
|-- frontend             # 前端（vben web-antd）
|-- test                 # 测试代码
`-- docs                 # 文档
```


## License

[Apache-2.0](LICENSE)
