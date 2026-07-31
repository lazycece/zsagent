# Commit Message Template (Conventional Commits)

## 格式
```
<type>(<scope>): <subject>

<body>

<footer>
```

## Type
- feat: 新功能
- fix: Bug 修复
- refactor: 重构
- test: 添加测试
- docs: 文档
- chore: 构建/工具
- style: 格式（不影响代码含义）
- perf: 性能优化

## Scope（可选）
模块名，如 order、payment、user

## Subject
祈使句，首字母小写，不超过 50 字符

## Body（可选）
解释 why 和 what，每行 72 字符内

## Footer（可选）
- BREAKING CHANGE: 破坏性变更说明
- Closes #123, #456

## 示例
feat(order): add discount calculation for bulk orders

Implement volume-based tiered pricing logic in OrderService.
Discount percentage increases with quantity.

Closes #89