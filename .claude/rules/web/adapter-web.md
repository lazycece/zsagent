---
paths:
  - "**/adapter/web/**/*.java"
---

# adapter web 规范

- 以业务领域 {agg} 定义子package，如order、goods等，在{agg}子包下定义web的控制器controller
- 控制器命名按CQRS模式拆分: `{领域}{操作类型}Controller`（如 `OrderCommandController`, `OrderQueryController`）

## URL
- url路径全部小写，短横线分割
- 统一前缀 `/api/v1/{agg}` 放在类上，用`@RequestMapping`承接
- url资源语义同方法名保持一致，若方法名`createOrder`则资源路径为`/api/v1/{agg}/create-order`
- 查询用GET，创建、更新、创建等用POST，禁止使用PUT、DELETE等

## 请求

- GET 请求参数用 `@RequestParam` 或 `@PathVariable`
- POST 请求体用 `@RequestBody`，并用 `@Validated`校验
- POST 请求体request直接复用工程架构中facade模块的定义

## 响应

- 统一包装用`RespData<T>`，已定义在`rapidf-restful`中直接使用即可

## 参考

web控制器参考案例如下：
```java
@RestController
@RequestMapping("/api/v1/order")
public class OrderCommandController {

    @Autowired
    private OrderCommandFacade commandFacade;

    @PostMapping("/create")
    public RespData<?> create(@RequestBody @Validated OrderCreateRequest request) {
        return commandFacade.createOrder(request);
    }
}
```


