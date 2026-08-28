# /review-pr

## 用法
`/review-pr`

## 行为
审查当前分支相对于 main 的所有改动，按 `code-reviewer` 代理的标准输出
Critical / Suggestions / Praise 三部分。使用 `git diff main...HEAD` 获取变更。