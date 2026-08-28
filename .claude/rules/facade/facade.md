---
paths:
  - "**/facade/**/*.java"
---

# facade 规范

- 以业务领域 {agg} 定义子package，如order、goods等，在{agg}子包下定义facade门面接口
- facade命名按CQRS模式拆分: `{领域}{操作类型}Controller`（如 `OrderCommandFacade`, `OrderQueryFacade`）
- 工程`facade`模块只定义api接口，不做实现，实现放在工程的`application`模块下


## api

- 入参必须封装成`XxxRequest`请求体，不能用裸参数
- 出参必须封装`XxxResult`对象，即便没有出参参数
- 请求体必须继承`BaseRequest`，已定义在`rapidf-restful`中直接使用即可
- 请求体定义在`request`包中，返回体定义在`result`包中
- 返回体包装用`RespData<T>`，已定义在`rapidf-restful`中直接使用即可

参考案例：
```java
public interface OrderCommandFacade {

    RespData<OrderCreateResult> createOrder(OrderCreateRequest request);
}
```

## request

- 请求体命名模式`{领域}{行为}Request`，如`OrderCreateRequest`
- 均实现`Serializable`接口
- 参数涉及对象情况，如果可复用则定义在`dto`包中，否则用内部类方式
- 参数校验用`@NotBlank`、`@NotEmpty`等等相关的 validation 注解
- 分页参数统一 `page`（从 1 开始）、`size`（默认 20）

## result

- 请求体命名模式`{领域}{行为}Result`，如`OrderCreateResult`
- 均实现`Serializable`接口
- 参数涉及对象情况，如果可复用则定义在`dto`包中，否则用内部类方式

## DTO

- 统一以`DTO`为后缀




