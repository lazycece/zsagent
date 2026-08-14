---
paths:
  - "**/domain/**/*.java"
---

# domain 规范

以业务领域 {agg} 定义子package，如order、goods等，在{agg}子包下实现领域业务

## model

- 聚合标记`@DomainAggregate`注解并继承`Aggregate<T>`基类，均引用自`rapidf-domain`组件
- 实体标记`@DomainEntity`注解并继承`Entity<T>`基类，均引用自`rapidf-domain`组件
- 聚合和实体不需要单独定义`creator`,`updater`,`createTime`,`updateTime`,`deleted`属性，基类中已有定义，但需要填充值
- 聚合和实体在领域定位上均需要以充血模型来实现

## valueobject

- 标记`@ValueObject`注解，引用自`rapidf-domain`组件
- 除非特殊要求用`class`，否则一律用java的`record`实现

## service

- 领域服务需要定义接口+实现类模式，实现类用`@DomainService`注解注入
- 属于充血模型的范围，就不要放在领域服务内实现
- 领域服务方法入参超过三个时需要封装成`class`对象, 放在`valueobject`子包中

## factory

- `factory`用 `@DomainFactory`标记

## repository
- 仓储方法入参超过三个时需要封装成值对象







