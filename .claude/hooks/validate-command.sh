#!/usr/bin/env bash
# ============================================================
# PreToolUse Hook — validate-command.sh
# 触发时机: Bash 工具调用前 (matcher: "Bash")
# 作用: 拦截危险命令，对风险操作发出警告
# ============================================================
set -euo pipefail

INPUT=$(cat 2>/dev/null || echo "")

# 从 Claude Code hook JSON 中提取 command 字段
COMMAND=$(echo "$INPUT" | grep -o '"command":"[^"]*"' 2>/dev/null | head -1 | sed 's/"command":"//;s/"$//' || echo "")

# 无法解析时放行，不阻塞正常工作
if [ -z "$COMMAND" ]; then
  exit 0
fi

# ---------- 阻断级：绝对禁止的危险模式 ----------
BLOCK_PATTERNS=(
  "rm -rf / "
  "rm -rf /$"
  "rm -rf ~ "
  "rm -rf ~/$"
  "rm -rf ."
  "git push --force origin main"
  "git push --force origin master"
  "git push -f origin main"
  "git push -f origin master"
  "mkfs."
  "> /dev/sda"
  "dd if=/dev/zero of=/dev/sd"
  ":(){ :|:& };:"
)

for pattern in "${BLOCK_PATTERNS[@]}"; do
  if [[ "$COMMAND" == *"$pattern"* ]]; then
    echo "============================================"
    echo " BLOCKED: 危险命令已被拦截"
    echo " 匹配模式: $pattern"
    echo " 完整命令: $COMMAND"
    echo "============================================"
    exit 1
  fi
done

# ---------- 警告级：高风险但可能合法的操作 ----------
WARN_PATTERNS=(
  "git push --force"
  "git push -f"
  "git reset --hard"
  "git clean -f"
  "DROP TABLE"
  "DROP DATABASE"
  "TRUNCATE TABLE"
  "rm -rf"
  "chmod 777"
)

for pattern in "${WARN_PATTERNS[@]}"; do
  if [[ "$COMMAND" == *"$pattern"* ]]; then
    echo "============================================"
    echo " WARNING: 检测到高风险操作"
    echo " 匹配模式: $pattern"
    echo " 完整命令: $COMMAND"
    echo " 继续执行，请确认无误..."
    echo "============================================"
  fi
done

exit 0
