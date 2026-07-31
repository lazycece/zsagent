---
paths:
  - "**/adapter/web/**/*.java"
---

# adapter web 规范

以业务聚合 {agg} 定义子package，如order、goods等，在{agg}子包下定义web的控制器controller。

## URL

- 统一前缀 `/api/v1/{agg}` 放在类上，用`@RequestMapping`承接
- url资源语义同方法名保持一致，若方法名`createOrder`则资源路径为`/api/v1/{agg}/create-order`
- 只使用 GET、POST两种请求协议，禁止使用PUT、DELETE等

## 请求

- GET 请求参数用 `@RequestParam` 或 `@PathVariable`
- POST 请求体用 `@RequestBody`，并用 `@Validated`校验
- POST 请求体request直接复用工程架构中facade模块的定义
- 分页参数统一 `page`（从 1 开始）、`size`（默认 20）

## 响应

- 统一包装用`RespData<T>`，已定义在`rapidf-restful`中直接使用即可




