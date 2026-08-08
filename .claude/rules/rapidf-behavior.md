---
paths:
  - "**/*.java"
---

## Java基础
- 类、成员变量、方法等需要有注释
- 接口实现类以`impl`为后缀

## 逻辑规范
- if条件的字符串判空用 `StringUtils.isNotBlank()`

## spring
- 使用构造器模式注入，禁止以`@Autowired`单独注入

## 通用领域
- 实现`BaseEnum<T>`接口，已定义在`rapidf-domain`中，标记`@Getter`、`@AllArgsConstructor`不单独实现构造器方法
- 分页对象用`Pagination`,内部已定义`page`,`size`,`count`
- 分页结果数据用`PageData<T>`保障，已定义在`rapidf-restful`中
- converter方法需要对入参对象做 `if null` 软处理

## 状态码
现有状态码定义在`RespStatus`中，如下所示：
> 尽量使用现有范围内的状态码，如果要自定义则需要符合现有的`Family`归类

| 范围 | Family | 关键常量 |
|------|--------|----------|
| 1xx | AUTH | AUTH_FAIL(100)、AUTH_TOKEN_FAIL(101)、AUTH_SIGN_FAIL(102)、AUTH_PARAM_FAIL(103) |
| 2xx | SUCCESS | SUCCESS(200) |
| 4xx | CLIENT | CLIENT_ERROR(400)、ACCESS_DENIED(403)、SERVICE_NOT_FOUND(404) |
| 5xx | SERVER | INTERNAL_SERVER_ERROR(500)、DB_EXCEPTION(501) |
| 6xx | INTEGRATION | INTEGRATION_ERROR(600) |
| 7xx | FAIL | FAIL(700)、PARAM_ERROR(701)、DATA_NOT_EXIST(702)、DATA_STATUS_ERROR(703)、NEED_TO_RETRY(704)、USER_BIZ_FAIL(705) |


## 异常体系
异常处理必须使用如下异常体系：
```
AbstractBaseException (abstract)
├── AuthException          → 100 (AUTH_FAIL)
├── ClientException        → 400 (CLIENT_ERROR)
├── ParamException         → 701 (PARAM_ERROR)
├── BusinessException      → 700 (FAIL)
├── ServerException        → 500 (INTERNAL_SERVER_ERROR)
├── IntegrationException   → 600 (INTEGRATION_ERROR)
├── UserBizException       → 705 (USER_BIZ_FAIL)
├── CommonException        → Status 可配置
└── AssertException        → Status 可配置（由 Assert.* 抛出）
```

原定义在`rapidf-restful`组件中：
> 非必要不要单独定义异常，如果要自定义须继承 `AbstractBaseException` 且不覆写 `getMessage()`
- 参数异常用`ParamException`，
- 业务异常用`BusinessException`
- 非预期异常用`ServerException`
- infra的integration下继承下游服务异常用`IntegrationException`
- 用户相关业务异常用`UserBizException`
- 断言校验异常用`AssertException`，可单独配置`Status`
- 无法归纳到上诉某类异常时用`CommonException`，可单独配置`Status`

编码时使用`ExceptionFactory`抛出异常，如下：
```java
ExceptionFactory.paramException("message");
ExceptionFactory.businessException("message");
```

## Assert 断言
方法入参校验用断言`Assert`模式，原定义在`rapidf-restful`组件中:
> 携带指定的 `Status` + 格式化消息；消息格式使用 `{}` 占位符（SLF4J 风格）

参考案例如下：
```java
// 空值检查
Assert.notNull(user, RespStatus.PARAM_ERROR, "user not found: id={}", userId);
Assert.notBlank(name, RespStatus.PARAM_ERROR, "name required");
Assert.notEmpty(items, RespStatus.PARAM_ERROR, "items required");

// 布尔守卫
Assert.isTrue(amount > 0, RespStatus.PARAM_ERROR, "amount must be > 0: {}", amount);
Assert.isFalse(locked, RespStatus.FAIL, "resource locked");

// 比较
Assert.greater(amount, 0, RespStatus.PARAM_ERROR, "amount must be > 0");
Assert.less(amount, 100, RespStatus.PARAM_ERROR, "amount must be < 100");
Assert.greaterOrEqual(age, 18, RespStatus.PARAM_ERROR, "age must be >= 18");
Assert.lessOrEqual(count, limit, RespStatus.FAIL, "exceeded limit");

// 相等 & 类型
Assert.equal(status, "ACTIVE", RespStatus.FAIL, "invalid status: {}", status);
Assert.assignableFrom(clazz, MyInterface.class, RespStatus.FAIL, "type mismatch");
```

### RespData<T> 使用规范

```java
// 成功
RespData.success(orderData);            // code=200, body=orderData
RespData.success();                     // code=200, body=null

// 失败
RespData.fail();                        // code=700 (FAIL)
RespData.fail("reason");
RespData.fail(RespStatus.PARAM_ERROR, "message required");

// 只设置Status
RespData.status(RespStatus.NEED_TO_RETRY);

// 运行时检查
respData.isSuccess();    // code == 200
respData.isNeedRetry();  // code == 704
```



