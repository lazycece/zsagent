---
paths:
  - "**/*.java"
---

## Java基础
- 类、成员变量、方法等需要有注释
- 接口实现类以`impl`为后缀

## 领域
- 实现`BaseEnum<T>`接口，已定义在`rapidf-domain`中，标记`@Getter`、`@AllArgsConstructor`不单独实现构造器方法
- 分页对象用`Pagination`,内部已定义`page`,`size`,`count`

## 异常处理

## spring
- 使用构造器模式注入，禁止以`@Autowired`单独注入

## 逻辑判断
- 字符串判空用 `StringUtils.isNotBlank()`
