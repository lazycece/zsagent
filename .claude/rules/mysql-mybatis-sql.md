---
paths:
  - "**/*.sql"
  - "**/src/main/resources/mapper/**/*.xml" # MyBatis 映射文件纳入检查        
  - "**/infra/dal/mapper/**/*.java" # Mapper 接口中的注解 SQL 也触发
---

# SQL 编写规范（MySQL + MyBatis）

## 命名与标识符
- 表名、列名统一 `snake_case`，全小写
- 表名单数（`user` 不是 `users`）
- 外键列：`user_id` 引用 `user` 表
- 索引命名：`idx_{table}_{column}`，唯一索引 `uk_{table}_{column}`
- 关键字小写（`select` `from` `where`），标识符用反引号 `` ` `` 包裹（防止保留字冲突）
- 别名必须有意义，禁止单字母（CTE 临时表除外）

## 格式与风格
- 根关键字（`select` `from` `where` `join` `group by` `order by` `having`）各自独立成行
- 多字段换行，缩进 4 空格
- 禁止 `select *`，必须显式列出所有列
- 子查询用 CTE（`with ... as`）替代嵌套，提升可读性

## MySQL 方言约定
- 引擎统一 InnoDB，字符集 `utf8mb4`，排序规则 `utf8mb4_unicode_ci`
- 主键用 `bigint unsigned auto_increment`，禁止业务主键
- 时间字段用 `datetime(3)` 或 `timestamp(3)`，统一用 `datetime`（时区无关）
- 布尔字段用 `tinyint(1)`，默认 `0`，禁止 `bit`
- 分页用 `limit #{offset}, #{size}` 或 `limit #{size} offset #{offset}`，禁止 `limit 100000,10` 深翻页
- 插入或更新用 `insert into ... values (...) on duplicate key update ...`
- 禁止 `replace into`（会先 delete 再 insert，破坏自增 ID 连续性）
- 禁止在 where 中对列使用函数（如 `where date(create_time) = '2025-01-01'`），改用范围查询

## MyBatis 映射规范

### mapper
- 裸参数需要用`@Param`标记，其`value`为实际参数名
- 单个方法参数超过三个时封装成dto对象

### 参数占位符
- 所有用户输入必须用 `#{}`，**禁止 `${}`**（除非是动态表名/列名，且必须白名单校验）
- 集合参数用 `<foreach collection="list" item="item" open="(" separator="," close=")">#{item}</foreach>`
- 批量插入用 `<foreach>` 包裹 `values` 子句，注意 MySQL 的 `max_allowed_packet` 限制（单批 ≤ 1000 条）

### resultMap
- 优先用 `autoMapping="true"` 简化映射，但列名与属性名不一致时必须显式写 `<result>`
- 关联查询用 `<association>` / `<collection>`，**禁止** N+1 问题（尽量用 join 一次查出）
- 枚举字段用 `typeHandler` 统一处理，不要在 SQL 里写 `case when`

### 动态 SQL
- `<if test="condition">` 中的 OGNL 表达式：字符串判空用 `!= null and != ''`，数字判 null
- `<where>` 自动处理多余 `and`，禁止手动写 `where 1=1`
- `<set>` 用于 update 动态 set，避免整行更新
- 复杂条件抽成 `<sql id="Base_Column_List">` + `<include refid="Base_Column_List"/>`

## 性能与安全
- 大表查询必须走索引：explain 检查 type 不为 ALL，extra 不含 Using filesort / Using temporary
- 禁止 `like '%keyword'`（前置通配符不走索引），必须走搜索引擎或倒排索引
- 联表查询必须用 inner/left join，**禁止**隐式笛卡尔积（逗号分隔多表）
- 事务内禁止跨库查询（不同数据源），必须用分布式事务或最终一致性
- 所有 SQL 必须参数化，禁止字符串拼接（即使看起来是安全的常量）

## 禁止行为（硬约束）
- 禁止 `delete` 不带 `where`
- 禁止 `update` 不带 `where`
- 禁止在 MyBatis XML 中写 `select *`
- 禁止在 Mapper 接口方法上同时使用 `@Select` 和 XML（二选一，推荐 XML）
- 禁止在循环中逐条执行 SQL（必须 batch 或 foreach）
- 禁止在 `application.yml` 中开启 `mybatis.configuration.map-underscore-to-camel-case` 以外的自动映射（显式映射更可控）

## 已知坑
- MyBatis `<=` 符号在 XML 中必须转义为 `&lt;=` 或用 `<![CDATA[ <= ]]>`
- `<foreach>` 的 `open` `close` `separator` 不要漏掉空格（例如 `open="("` 后面无空格会导致语法错误）
- 分页插件（PageHelper）必须在紧跟的 SQL 之前调用，中间不能有其他数据库操作
- MySQL 8.0 以上 `group by` 不再隐式排序，需显式 `order by`