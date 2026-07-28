# zsagent 工程规范

## 技术栈


- 基础依赖：**Java 17**、**Spring Boot 4.1.0**、**Spring AI 2.0.0**
- 数据库依赖：**MyBatis** 4.1 + **MySQL** 8.0.27 + **Druid** 1.2.15（连接池）
- 自研框架：**rapidf** 3.0.2 — 自研 DDD/CQRS/REST 框架，来自 `com.lazycece.rapidf`

## 构建与运行

```bash
# 编译所有模块
mvn clean compile
# 运行所有测试
mvn clean test
# 打包应用
mvn clean package
# 运行 Spring Boot 应用（从 bootstrap 模块）
mvn spring-boot:run -pl bootstrap
# 运行单个测试类
mvn -pl test -Dtest=YourTestClass test
```

## 架构

项目采用 **DDD（领域驱动设计）+ 六边形架构**，以 Maven 多模块方式组织。依赖方向：所有模块向内依赖 domain 层。

```
Adapter（REST 控制器）
  → Application（facade 实现、CQRS 处理器、校验器、组装器）
    → Domain（领域模型、服务、仓储接口、事件）
      ← Infrastructure/ACL（仓储实现、外部集成客户端）
```

**模块依赖链**：`bootstrap` → `adapter` → `application` → `domain` ← `infrastructure`

- **`app/facade`** — 对外 API 接口（`*Facade`）、DTO、请求/响应对象。可独立发布为 JAR（附带 source/javadoc 插件）。
- **`app/domain`** — 领域层
- **`app/infrastructure`** — 父 POM，包含三个子模块：
    - `dal` — MyBatis 映射器、PO/DTO 类、MyBatis XML 映射文件
    - `integration` — 外部服务集成
    - `acl` — 防腐层
- **`app/application`** — 实现 facade 接口。包含 CQRS 命令/查询处理器、校验器、组装器（Assembler：从请求构建领域对象）、转换器（Converter：领域模型 → DTO）。
- **`app/adapter`** — 适配器
- **`bootstrap`** — Spring Boot 启动入口（`BootstrapApplication.java`）
- **`test`** — 测试模块，依赖 `zsagent-bootstrap`

## 配置

- `conf/environment/` 中的环境配置文件会在构建时复制到 bootstrap 的 resources 目录。
- 默认 profile：`dev`（MySQL 地址 `localhost:3306`，库 `test`，用户 `root` / 密码 `123456`）。
- 应用配置使用 `${placeholder}` 语法，带默认值：`${profile:dev}`、`${app.port:8080}`。

## 约定

- 所有 request/result DTO 实现 `Serializable`。
- MyBatis 映射 XML 文件放在 dal 模块的 `resources/mapper/**/*Mapper.xml` 路径下。
- domain 层保持干净，不引入基础设施/框架依赖，只使用 `rapidf-domain` 注解。
- 仓储在 domain 层定义接口，在 `infrastructure/acl` 中实现。

## 文档
```bash
# 需求文档
document/brd
# 设计文档
document/design
```