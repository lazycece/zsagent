---
name: rapidf-statemachine
description: >-
  当需要设计、实现或修改本项目的有限状态机（FSM）时使用。
  涵盖状态定义、事件、转换、守卫条件、动作和持久化。
  支持 rapidf-domain 组件中的状态机模式来实现状态机。
  当任务涉及状态生命周期、状态流转或工作流自动化时激活。
---

# rapidf-domain statemachine

## 核心 API

| 接口/类 | 作用 |
|---------|------|
| `State<T>` extends `BaseEnum<T>` | 状态枚举 |
| `StateEvent<T>` extends `BaseEnum<T>` | 事件枚举 |
| `StateApply` | `State<?> getState()` — 实体需实现此接口 |
| `AbstractTransition(event, sourceState, targetState)` | 转移——构造函数绑定三者 |
| `AbstractStateMachine(List<Transition>)` | 状态机——内部构建 `Map<sourceState, List<Transition>>` |
| `StateMachine.execute(StateApply, StateEvent)` → `State<?>` | 查找匹配转移 → 执行 → 返回目标状态 |

## 完整示例

```java
// 1. 状态枚举
public enum AuditStatus implements State<String> {
    DRAFT("DRAFT", "草稿"),
    AUDITING("AUDITING", "审核中"),
    PASSED("PASSED", "已通过");
    private final String code, desc;
    AuditStatus(String code, String desc) { this.code = code; this.desc = desc; }
    @Override public String getCode() { return code; }
    @Override public String getDesc() { return desc; }
}

// 2. 实体实现 StateApply
public class Goods extends Entity<String> implements StateApply {
    private AuditStatus status;
    @Override public State<?> getState() { return status; }
    public void setState(State<?> state) { this.status = (AuditStatus) state; }
}

// 3. 定义领域业务的抽象Transition
public abstract class AbstractGoodsAuditStateTransition extends AbstractTransition {
    protected AbstractGoodsAuditStateTransition(StateEvent<?> stateEvent, State<?> source, State<?> target) {
        super(stateEvent, source, target);
    }
}

// 4. 转移——构造函数绑定 (event, source, target)
public class AuditSubmitTransition extends AbstractGoodsAuditStateTransition {
    public AuditSubmitTransition() {
        super(AuditEvent.SUBMIT_AUDIT, AuditStatus.DRAFT, AuditStatus.AUDITING);
    }
    @Override
    public void execute(StateApply apply) {
        Goods goods = (Goods) apply;
        // 转移特有的副作用
    }
}

// 5. 状态机
public class GoodsAuditStateMachine extends AbstractStateMachine {
    public GoodsAuditStateMachine(List<AbstractGoodsAuditStateTransition> transitions) {
        super(transitions);
    }
    // 事件枚举通常作为状态机的内部类
    public enum AuditEvent implements StateEvent<String> {
        SUBMIT_AUDIT("SUBMIT_AUDIT", "提交审核"),
        AUDIT_PASS("AUDIT_PASS", "审核通过"),
        AUDIT_REJECT("AUDIT_REJECT", "审核拒绝");
        private final String code, desc;
        AuditEvent(String c, String d) { code = c; desc = d; }
        @Override public String getCode() { return code; }
        @Override public String getDesc() { return desc; }
    }
}

// 5. 执行
State<?> newState = stateMachine.execute(goods, AuditEvent.SUBMIT_AUDIT);
```

## 参考

- 框架源码：https://github.com/lazycece/rapidf/releases ，框架发布版本与当前skill版本对应
- 状态机：`rapidf-components/rapidf-domain/src/main/java/com/lazycece/rapidf/domain/statemachine/`
- 完整示例：`rapidf-samples/rapidf-domain-sample/` — `GoodsAuditStateMachine`
