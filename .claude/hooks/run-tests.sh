#!/usr/bin/env bash
# ============================================================
# Stop Hook — run-tests.sh
# 触发时机: 会话结束时 (matcher: "*")
# 作用: 运行项目测试，确保离开时代码可编译且测试通过
# ============================================================
set -euo pipefail

# 获取项目根目录（.claude 的父目录）
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

cd "$PROJECT_ROOT"

echo "============================================"
echo " Stop Hook: 运行编译检查与测试"
echo " 项目目录: $PROJECT_ROOT"
echo "============================================"

# 检查是否有代码变更（变更的 java 文件 或 pom.xml）
CHANGED_FILES=$(git diff --name-only HEAD 2>/dev/null || true)
UNTRACKED_SRC=$(git ls-files --others --exclude-standard '*.java' '*.xml' 2>/dev/null || true)

if [ -z "$CHANGED_FILES" ] && [ -z "$UNTRACKED_SRC" ]; then
  echo ""
  echo "  无代码变更，跳过编译与测试"
  exit 0
fi

echo ""
echo "  检测到代码变更:"
if [ -n "$CHANGED_FILES" ]; then
  echo "$CHANGED_FILES" | sed 's/^/    /'
fi
if [ -n "$UNTRACKED_SRC" ]; then
  echo "$UNTRACKED_SRC" | sed 's/^/    (new) /'
fi

# Step 1: 编译检查（快速反馈）
echo ""
echo "[1/2] 编译检查..."
if mvn compile -q 2>&1; then
  echo "  ✓ 编译通过"
else
  echo "  ✗ 编译失败，请检查代码"
  exit 1
fi

# Step 2: 运行测试
echo ""
echo "[2/2] 运行测试..."
if mvn test -q 2>&1; then
  echo "  ✓ 全部测试通过"
else
  echo "  ✗ 存在测试失败，请检查"
  exit 1
fi

echo ""
echo "============================================"
echo " 编译与测试全部通过 ✓"
echo "============================================"
exit 0
