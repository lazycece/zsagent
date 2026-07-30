# zsagent 工程规范

## 技术栈

- 基础依赖：**Java 17**、**Spring Boot 4.1.0**、**Spring AI 2.0.0**
- 数据库依赖：**MyBatis** 4.1 + **MySQL** 8.0.27 + **Druid** 1.2.15（连接池）
- 自研框架：**rapidf** 3.0.2 — 自研 DDD/CQRS/REST 框架，来自 `com.lazycece.rapidf`

## 工程架构
工程架构规范详见
```
.claude/rules/ddla-architecture.md
```

## 构建运行

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

## 文档
```bash
# 需求文档
document/brd
# 设计文档
document/design
```