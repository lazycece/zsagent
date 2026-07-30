---
name: design-patterns
description: 常见设计模式及 Java 示例（工厂、建造者、策略、观察者、装饰器等）。当用户要求"实现某个模式"、"使用工厂"、"策略模式"或设计可扩展组件时使用。
---

# 设计模式技能（Design Patterns Skill）

Java 常见设计模式快速参考指南。

## 使用场景

- 用户要求实现某个特定模式
- 设计可扩展/灵活的组件
- 重构僵化的代码

## 快速参考：何时使用哪种模式

| 问题场景 | 模式 | 使用时机 |
|---------|------|----------|
| 复杂对象构建 | **建造者（Builder）** | 参数众多，部分可选 |
| 不指定具体类创建对象 | **工厂（Factory）** | 类型在运行时确定 |
| 多种算法，运行时切换 | **策略（Strategy）** | 行为因上下文而异 |
| 不修改类而添加行为 | **装饰器（Decorator）** | 需要动态组合 |
| 通知多个对象状态变化 | **观察者（Observer）** | 一对多依赖关系 |
| 转换不兼容的接口 | **适配器（Adapter）** | 集成遗留/第三方代码 |

---

## 创建型模式

### 建造者（Builder）

**问题：** 构造函数参数 telescoping（层层嵌套），大量可选参数

```java
// ✅ 建造者模式
public class User {
    private final String name;  // 必填
    private final String email; // 必填
    private final int age;      // 可选

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
    }

    public static Builder builder(String name, String email) {
        return new Builder(name, email);
    }

    public static class Builder {
        private final String name;
        private final String email;
        private int age = 0;

        private Builder(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

// 使用方式
User user = User.builder("John", "john@example.com")
    .age(30)
    .build();
```

### 工厂（Factory）

**问题：** 在不确定具体类的情况下创建对象

```java
// ✅ 工厂模式
public interface Notification {
    void send(String message);
}

public class NotificationFactory {
    public static Notification create(String type) {
        return switch (type.toUpperCase()) {
            case "EMAIL" -> new EmailNotification();
            case "SMS" -> new SmsNotification();
            case "PUSH" -> new PushNotification();
            default -> throw new IllegalArgumentException("Unknown: " + type);
        };
    }
}

// Spring 版本——推荐
@Component
public class NotificationFactory {
    private final Map<String, NotificationSender> senders;

    public NotificationFactory(List<NotificationSender> senderList) {
        this.senders = senderList.stream()
            .collect(Collectors.toMap(
                NotificationSender::getType,
                Function.identity()
            ));
    }

    public NotificationSender get(String type) {
        return Optional.ofNullable(senders.get(type))
            .orElseThrow(() -> new IllegalArgumentException("Unknown: " + type));
    }
}
```

---

## 行为型模式

### 策略（Strategy）

**问题：** 同一操作有多种算法，需要在运行时选择

```java
// ✅ 策略模式
public interface PaymentStrategy {
    void pay(BigDecimal amount);
}

public class CreditCardPayment implements PaymentStrategy {
    private final String cardNumber;

    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " with card");
    }
}

public class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout(BigDecimal total) {
        paymentStrategy.pay(total);
    }
}

// 使用方式
cart.setPaymentStrategy(new CreditCardPayment("4111..."));
cart.checkout(new BigDecimal("99.99"));

// 函数式变体（Java 8+）
@FunctionalInterface
public interface PaymentStrategy {
    void pay(BigDecimal amount);
}

PaymentStrategy creditCard = amount -> System.out.println("Card: " + amount);
cart.setPaymentStrategy(creditCard);
```

### 观察者（Observer）

**问题：** 状态变化时通知多个对象

```java
// ✅ Spring 事件（推荐）
public record OrderPlacedEvent(Order order) {}

@Service
public class OrderService {
    private final ApplicationEventPublisher eventPublisher;

    public void placeOrder(Order order) {
        saveOrder(order);
        eventPublisher.publishEvent(new OrderPlacedEvent(order));
    }
}

@Component
public class InventoryListener {
    @EventListener
    public void handleOrderPlaced(OrderPlacedEvent event) {
        // 扣减库存
    }
}

@Component
public class EmailListener {
    @EventListener
    @Async
    public void handleOrderPlaced(OrderPlacedEvent event) {
        // 发送邮件
    }
}
```

---

## 结构型模式

### 装饰器（Decorator）

**问题：** 在不修改类的前提下动态添加行为

```java
// ✅ 装饰器模式
public interface Coffee {
    String getDescription();
    BigDecimal getCost();
}

public class SimpleCoffee implements Coffee {
    public String getDescription() { return "Coffee"; }
    public BigDecimal getCost() { return new BigDecimal("2.00"); }
}

public abstract class CoffeeDecorator implements Coffee {
    protected final Coffee coffee;
    public CoffeeDecorator(Coffee coffee) { this.coffee = coffee; }
}

public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) { super(coffee); }

    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    public BigDecimal getCost() {
        return coffee.getCost().add(new BigDecimal("0.50"));
    }
}

// 使用方式
Coffee coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
coffee = new SugarDecorator(coffee);
```

### 适配器（Adapter）

**问题：** 让不兼容的接口协同工作

```java
// ✅ 适配器模式
public interface MediaPlayer {
    void play(String filename);
}

// 遗留代码
public class LegacyAudioPlayer {
    public void playMp3(String filename) { /* ... */ }
}

// 适配器
public class Mp3PlayerAdapter implements MediaPlayer {
    private final LegacyAudioPlayer legacyPlayer = new LegacyAudioPlayer();

    @Override
    public void play(String filename) {
        legacyPlayer.playMp3(filename);
    }
}

// 使用方式
MediaPlayer player = new Mp3PlayerAdapter();
player.play("song.mp3");
```

---

## 模式选择指南

| 场景 | 适用模式 |
|------|----------|
| 对象创建逻辑复杂 | 建造者、工厂 |
| 需要动态添加功能 | 装饰器 |
| 算法有多种实现 | 策略 |
| 响应状态变化 | 观察者 |
| 集成遗留代码 | 适配器 |

## 应避免的反模式

| 反模式 | 问题 | 更好的方案 |
|--------|------|------------|
| 滥用单例（Singleton abuse） | 全局状态，难以测试 | 依赖注入（Dependency Injection） |
| 到处使用工厂 | 过度设计 | 类型已知时直接用 `new` |
| 深层装饰器链 | 难以调试 | 组合优于继承，保持链简短 |
