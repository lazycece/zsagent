---
paths:
  - "**/*Test.java"
---

# test规范

## 结构

文件存放在 `test` 模块下，工程测试文件各模块划分结构如下（包路径：`{groupId}/test/`）
- 各模块的测试文件放在对应的模块下
- 根据原类文件包路径自行完善测试文件的包路径

```
.
|-- app
|   |-- adapter
|   |-- application
|   |-- domain
|   `-- infrastructure
|       |-- acl
|       |-- dal
|       `-- integration
|-- bootstrap
`-- utils
```

## 编码规范

- 一个类对应一个测试类，一个方法对应一个测试方法，方法的多场景测试直接在一个方法内完成
- 类和方法的测试全部以单测处理，复杂逻辑必须有单测，简单逻辑无效单测
- 集成测试用`.http`文件的方式进行驱动
