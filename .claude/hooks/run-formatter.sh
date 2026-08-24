#!/usr/bin/env bash
# ============================================================
# PostToolUse Hook — run-formatter.sh
# 触发时机: Edit/Write 工具调用后 (matcher: "Edit|Write")
# 作用: 对被修改的 Java 文件执行代码格式化
# ============================================================
set -euo pipefail

INPUT=$(cat 2>/dev/null || echo "")

# 提取被修改的文件路径
FILE_PATH=$(echo "$INPUT" | grep -o '"file_path":"[^"]*"' 2>/dev/null | head -1 | sed 's/"file_path":"//;s/"$//' || echo "")

if [ -z "$FILE_PATH" ]; then
  exit 0
fi

# 仅处理 Java 源文件
if [[ "$FILE_PATH" != *.java ]]; then
  exit 0
fi

echo "[formatter] 检查格式: $FILE_PATH"

# ---------- 方式一: Spotless Maven Plugin（若已配置）----------
# 取消注释以启用:
MODULE_DIR=$(echo "$FILE_PATH" | grep -oP '^[^/]+/[^/]+' || echo "")
if [ -n "$MODULE_DIR" ]; then
  mvn spotless:apply -pl "$MODULE_DIR" -q 2>/dev/null && echo "[formatter] Spotless applied." || true
fi

# ---------- 方式二: Google Java Format（独立工具）----------
# 取消注释以启用:
# if command -v google-java-format &> /dev/null; then
#   google-java-format -i "$FILE_PATH" && echo "[formatter] google-java-format applied."
# fi

# ---------- 当前：仅报告，不自动格式化 ----------
echo "[formatter] 未配置自动格式化工具。建议在 pom.xml 中添加 spotless-maven-plugin。"
echo "[formatter] 参考配置见: .claude/templates/spotless-pom.xml.tmpl"

exit 0
