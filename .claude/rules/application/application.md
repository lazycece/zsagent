---
paths:
  - "**/application/**/*.java"
---

# application 规范

- 以业务领域 {agg} 定义子package，如order、goods等，在{agg}子包下实现工程`facade`模块定义的api接口
- facade实现服务用`@ApplicationService`注解注入，同时标记`@Primary`
- 请求的参数校验如果很复杂，则抽象为`XxxRequestValidator`类单独实现，放在`validator`包下
- 涉及对象转换则抽象为`{领域}Converter`类实现，放在`converter`包下
- 涉及复杂对象构建编译可抽象为`{领域}Assembler`类实现，放在`assembler`包下
- 涉及复杂编排子逻辑，可以放在`handler`包下，按领域视角规划子包结构
- `handler`处理器用`@ApplicationHandler`注入



