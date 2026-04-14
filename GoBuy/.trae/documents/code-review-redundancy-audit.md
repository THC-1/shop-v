# GoBuy 项目代码审查报告

> 审查目标：排除冗余代码，提升可读性，遵循项目既定规范（DRY、SRP、RESTful、命名规范等）

---

## 一、🔴 严重冗余：重复的认证逻辑（3 处重复）

### 1.1 AuthInterceptor + SecurityConfig + WebMvcConfig 三重认证

当前项目中 JWT 认证逻辑存在 **3 处重复实现**：

| 位置 | 文件 | 作用 |
|------|------|------|
| ① | `AuthInterceptor.preHandle()` | 解析 Token → 设置 UserContextHolder |
| ② | `SecurityConfig.filterChain()` 内的匿名 `OncePerRequestFilter` | 解析 Token → 设置 UserContextHolder |
| ③ | `WebMvcConfig.addInterceptors()` | 注册 AuthInterceptor 拦截器 |

**问题分析：**
- ① 和 ② 的 Token 解析逻辑完全相同（提取 Authorization Header → 截取 Bearer 后部分 → 调用 jwtUtil.getUserIdFromToken → 设置 UserContextHolder）
- ② 中匿名 Filter 没有清理 ThreadLocal（缺少 `afterCompletion` 对应逻辑），可能导致线程复用时数据串包
- ② 中通过 `authInterceptor.getJwtUtil()` 访问 JwtUtil，这是为了绕过直接注入而暴露的 getter，违反了封装原则
- Spring Security Filter 和 MVC Interceptor 同时存在意味着同一请求可能被认证两次

**改进建议：** 保留 SecurityConfig 中的 Filter（因为它在安全链中更早执行），删除 AuthInterceptor 和 WebMvcConfig。在 SecurityConfig 的 Filter 中补充 `afterCompletion` 等价的 ThreadLocal 清理逻辑（使用 `SecurityContextLogoutHandler` 或在 Filter 的 finally 块中清理）。

---

## 二、🔴 严重冗余：双套 Service 接口（每个模块都有）

每个业务模块都存在两个 Service 接口，功能高度重叠：

| 模块 | IXxxService（extends IService） | XxxService（独立接口） | 实际使用 |
|------|------|------|------|
| User | `IUserService` | `UserService` | Controller 用 `IUserService`，`UserService` 未被任何类实现 |
| Product | `IProductService` | `ProductService` | Controller 用 `IProductService`，`ProductService` 未被任何类实现 |
| Order | `IOrderItemService` | `OrderService` | OrderController 用 `OrderService`（ServiceImpl 直接实现） |
| Cart | `ICartService` | `CartService` | Controller 用 `ICartService`，`CartService` 未被任何类实现 |
| Payment | `IPaymentService` | `PaymentService` | Controller 用 `IPaymentService`，`ProductService` 未被任何类实现 |
| Scenario | `IScenarioService` | `ScenarioService` | Controller 用 `IScenarioService`，`ScenarioService` 未被任何类实现 |

**问题分析：**
- `UserService`、`ProductService`、`CartService`、`PaymentService`、`ScenarioService` 这 5 个独立接口 **完全没有实现类**，是纯粹的死代码
- `OrderService` 是唯一被使用的独立接口，但它的实现类 `OrderServiceImpl` 同时继承了 `ServiceImpl`，风格与其他模块不一致

**改进建议：** 统一为单一 Service 接口模式。推荐全部采用 `IXxxService extends IService<Xxx>` 模式（与 MyBatis-Plus 生态一致），删除所有未使用的独立 `XxxService` 接口。Order 模块的 `OrderService` 也应改为 `IOrderService extends IService<Order>`。

---

## 三、🔴 严重冗余：手动 VO 转换 vs MapStruct Assembler

项目引入了 MapStruct 并为每个模块定义了 Assembler，但多处仍在手动转换：

| 位置 | 手动转换方法 | 应使用的 Assembler |
|------|------|------|
| `UserServiceImpl.convertToVO()` | 12 行手动 set | `UserAssembler.toVO()` |
| `ProductServiceImpl.convertToProductVOPage()` | 13 行手动 set | `ProductAssembler.toVO()` |
| `OrderServiceImpl.getOrderDetail()` | 9 行手动 set | `OrderAssembler.toVO()` |
| `OrderServiceImpl.listOrders()` | 9 行手动 set | `OrderAssembler.toVO()` |
| `CartController.convertToCartItemVO()` | 8 行手动 set | `CartAssembler.toVO()` |

**改进建议：** 统一使用 Assembler 进行转换，删除所有手动 `convertToXxxVO` 方法。对于分页转换，MapStruct 的 `toVO()` + `page.convert()` 组合即可。

---

## 四、🟡 冗余类/文件：未使用的死代码

### 4.1 未使用的 Entity

| 类 | 说明 |
|------|------|
| `CartItem` | 映射 `t_cart_item`，但实际使用的是 `Cart`（映射 `carts`），CartItem 从未被引用 |

### 4.2 未使用的 VO

| 类 | 说明 |
|------|------|
| `CartVO` | CartController 使用 `CartItemVO`，CartVO 从未被使用 |
| `ProductDetailVO` | 仅在未实现的 `ProductService` 接口中引用 |
| `ProductSkuVO` | 仅在 `ProductDetailVO` 中引用，而 ProductDetailVO 本身未使用 |
| `ScenarioDetailVO` | 仅在未实现的 `ScenarioService` 接口中引用 |
| `ScenarioProductVO` | 仅在 `ScenarioDetailVO` 中引用 |

### 4.3 未使用的 DTO

| 类 | 说明 |
|------|------|
| `CartDTO` | CartController 使用 `CartAddDTO`/`CartUpdateDTO`，CartDTO 从未被使用 |
| `OrderDTO` | OrderController 使用 `OrderCreateDTO`，OrderDTO 从未被使用 |

### 4.4 未使用的 Mapper

| 类 | 说明 |
|------|------|
| `BrandMapper` | 无任何 Service 注入使用 |
| `CategoryMapper` | 无任何 Service 注入使用 |
| `SkuMapper` | 无任何 Service 注入使用 |
| `ScenarioTagMapper` | 无任何 Service 注入使用 |

### 4.5 未使用的常量类

| 类 | 说明 |
|------|------|
| `RedisKeyConstants` | 项目中无任何 Redis 操作代码，此常量类完全未使用 |

### 4.6 未使用的查询 DTO

| 类 | 说明 |
|------|------|
| `ScenarioQueryDTO` | 仅在未实现的 `ScenarioService` 接口中引用 |

**改进建议：** 删除以上所有未使用的类文件，减少项目体积和认知负担。

---

## 五、🟡 BaseEntity 未被任何 Entity 继承

`BaseEntity` 定义了 `id`、`createTime`、`updateTime`、`isDeleted` 四个公共字段，但所有 Entity 类（User、Product、Order、OrderItem、Cart、Payment、Address、Scenario）都自行定义了这些字段，没有继承 `BaseEntity`。

**具体重复字段：**
- `id` + `createdAt` + `updatedAt`：在 User、Product、Order、OrderItem、Cart、Payment、Address、Scenario 中重复定义了 8 次
- `isDeleted`/`deleted`：Order 使用 `Boolean deleted` + `@TableLogic`，BaseEntity 使用 `Integer isDeleted` + `@TableLogic`，不一致

**改进建议：** 让所有 Entity 继承 `BaseEntity`，删除各 Entity 中重复的字段定义。统一逻辑删除字段类型（建议 `Integer isDeleted`）。

---

## 六、🟡 Controller 中的冗余通用 CRUD 接口

多个 Controller 包含了"代码生成器风格"的通用 CRUD 接口，与业务接口混在一起：

| Controller | 冗余接口 | 问题 |
|------|------|------|
| `UserController` | `list()`, `getById(id)`, `save(dto)`, `update(id, dto)`, `delete(id)` | 返回 `Result<Boolean>` 而非 VO；暴露 Entity 内部结构；与业务接口 `/me` 混淆 |
| `ProductController` | `save(dto)`, `update(id, dto)`, `delete(id)` | 管理接口与公开查询接口混在同一 Controller |
| `OrderController` | `list()`, `getById(id)` | 与业务接口 `/my`、`/{id}/detail` 功能重叠 |
| `PaymentController` | `list()`, `getById(id)`, `save(dto)`, `update(id, dto)`, `delete(id)` | 支付记录不应支持直接创建/修改/删除 |
| `AddressController` | `list()`, `getById(id)` | 与业务接口 `/my` 功能重叠 |
| `OrderItemController` | 整个 Controller | 订单项应通过订单创建管理，不应暴露独立 CRUD |

**改进建议：**
1. 删除 `OrderItemController` 整个文件（订单项只应通过 Order 创建）
2. 删除 PaymentController 中的 `save()`、`update()`、`delete()` 接口（支付记录不应手动增删改）
3. 删除 UserController 中的通用 CRUD（用 `/me` 系列接口替代）
4. 将管理类接口（如商品增删改）拆分到独立的 AdminController 或加权限控制

---

## 七、🟡 DTO 中不应包含 id 字段

以下 DTO 用于创建/更新操作，但包含了 `id` 字段，违反了 DTO 设计原则（ID 应由路径参数传递，不应在请求体中）：

| DTO | 冗余的 id 字段 |
|------|------|
| `UserDTO.id` | 创建时自动生成，更新时通过 `@PathVariable` 传入 |
| `ProductDTO.id` | 同上 |
| `OrderDTO.id` | 同上 |
| `PaymentDTO.id` | 同上 |
| `CartDTO.id` | 同上 |
| `ScenarioDTO.id` | 同上 |

**改进建议：** 从所有创建/更新用 DTO 中移除 `id` 字段。

---

## 八、🟡 枚举类定义了但未使用

| 枚举 | 定义内容 | 实际使用情况 |
|------|------|------|
| `OrderStatusEnum` | PENDING_PAYMENT(0) ~ CANCELLED(5) | OrderServiceImpl 使用硬编码字符串 `"PENDING_PAYMENT"` 等 |
| `PaymentMethodEnum` | ALIPAY(1) ~ BANK_CARD(3) | PaymentServiceImpl 使用硬编码字符串 `"alipay"` 等 |
| `ScenarioTypeEnum` | GAMING(1) ~ HOME(5) | 完全未使用 |

**改进建议：** 将 Service 中的硬编码字符串替换为枚举引用，或如果枚举设计不合理则重新设计。例如 `order.getStatus() == OrderStatusEnum.PENDING_PAYMENT.name()` 或使用 `@EnumValue` 注解与数据库映射。

---

## 九、🟡 CORS 配置冗余

`CorsConfig` 通过 `CorsFilter` Bean 配置了 CORS，但 `SecurityConfig` 中没有集成 CORS 配置。在 Spring Security 环境下，CORS 应在 SecurityFilterChain 中处理，否则可能出现 CORS 预检请求被 Security 拦截的问题。

**改进建议：** 删除 `CorsConfig`，在 `SecurityConfig.filterChain()` 中添加 `.cors(Customizer.withDefaults())` 并通过 `CorsConfigurationSource` Bean 配置 CORS。

---

## 十、🟡 不一致的表命名策略

Entity 的 `@TableName` 注解存在两种命名风格：

| 风格 | 表名示例 | Entity |
|------|------|------|
| 无前缀 | `users`, `products`, `orders`, `carts`, `payments`, `addresses`, `scenarios` | User, Product, Order, Cart, Payment, Address, Scenario |
| t_ 前缀 | `t_brand`, `t_category`, `t_sku`, `t_cart_item`, `t_scenario_product`, `t_scenario_tag` | Brand, Category, Sku, CartItem, ScenarioProduct, ScenarioTag |

**改进建议：** 统一表命名策略，建议全部使用无前缀的复数名词形式（如 `brands`, `categories`, `skus`），或全部使用 `t_` 前缀。

---

## 十一、🟡 其他代码质量问题

### 11.1 `UserServiceImpl` 中验证逻辑与 DTO 注解重复

`UserRegisterDTO` 已使用 `@NotBlank`、`@Size(min=3, max=20)` 等注解，但 `UserServiceImpl` 中又定义了 `validateUsername()` 和 `validatePassword()` 方法做相同校验。Controller 已加 `@Valid`，Service 层的校验冗余。

**改进建议：** 依赖 `@Valid` + DTO 注解完成校验，删除 Service 层的 `validateUsername()`/`validatePassword()` 方法。密码格式正则校验可通过自定义注解实现在 DTO 层。

### 11.2 `UserServiceImpl.register()` 手动设置时间戳

```java
user.setCreatedAt(java.time.LocalDateTime.now());
user.setUpdatedAt(java.time.LocalDateTime.now());
```

应使用 MyBatis-Plus 的 `MetaObjectHandler` 自动填充，或继承 `BaseEntity` 后由框架处理。

### 11.3 `UserServiceImpl.login()` 不应加 `@Transactional`

登录是纯读操作，不需要事务回滚支持。`@Transactional(rollbackFor = Exception.class)` 在此场景下是冗余的。

### 11.4 `OrderServiceImpl.createOrder()` 缺少商品校验

创建订单时没有校验：
- 商品是否存在
- 库存是否充足
- 价格是否与当前售价一致（前端传入的价格可能被篡改）

### 11.5 `OrderServiceImpl.generateOrderNo()` 并发不安全

`"ORD" + System.currentTimeMillis() + (long)(Math.random() * 10000)` 在高并发下可能生成重复订单号。建议使用雪花算法或 Redis 自增。

### 11.6 `ScenarioServiceImpl.listRecommendScenarios()` SQL 注入风险

```java
wrapper.last("LIMIT " + limit);
```

`limit` 参数来自前端请求，虽然当前是 Integer 类型不会产生注入，但使用 `wrapper.last()` 拼接 SQL 是不推荐的做法。建议使用 MyBatis-Plus 的分页功能替代。

### 11.7 `ScenarioServiceImpl.getScenarioProducts()` N+1 查询

```java
scenarioProducts.stream()
    .map(sp -> productMapper.selectById(sp.getProductId()))
    .collect(Collectors.toList());
```

每个商品单独查询一次数据库，存在 N+1 问题。应批量查询：先收集 productId 列表，再一次性 `selectBatchIds()`。

### 11.8 `PaymentServiceImpl.listMyPayments()` 查询逻辑错误

```java
wrapper.eq(Payment::getOrderId, 
    new LambdaQueryWrapper<Order>()
        .eq(Order::getUserId, userId)
        .select(Order::getId)
);
```

将 `LambdaQueryWrapper` 对象作为 `eq()` 的值传入是错误的，MyBatis-Plus 不会将其作为子查询处理。应改为先查询用户订单 ID 列表，再用 `in()` 查询支付记录。

### 11.9 `AuthInterceptor` 暴露了不必要的 getter

```java
public JwtUtil getJwtUtil() {
    return jwtUtil;
}
```

此 getter 仅为 SecurityConfig 的匿名 Filter 访问 JwtUtil 而存在，破坏了封装性。如果统一认证逻辑（建议一），此 getter 可删除。

---

## 十二、🟢 命名规范问题

| 位置 | 当前命名 | 建议命名 | 原因 |
|------|------|------|------|
| `OrderServiceImpl.listAll()` | `listAll()` | 与 IService.list() 功能完全相同，是冗余方法 | 直接调用 `list()` 即可 |
| `OrderServiceImpl.getOrderById()` | `getOrderById()` | 与 `getById()` 功能完全相同，是冗余方法 | 直接调用 `getById()` 即可 |
| `UserDTO` | `UserDTO` | `UserCreateDTO` | 明确表示用于创建操作 |
| `ProductDTO` | `ProductDTO` | `ProductCreateDTO` | 同上 |
| `ScenarioDTO` | `ScenarioDTO` | `ScenarioCreateDTO` | 同上 |
| `PaymentDTO` | `PaymentDTO` | `PaymentCreateDTO` | 同上 |

---

## 总结：改进优先级

| 优先级 | 改进项 | 影响范围 | 预期收益 |
|------|------|------|------|
| P0 | 统一认证逻辑（删除重复的 AuthInterceptor/SecurityConfig） | common/config | 消除3处重复，修复ThreadLocal泄漏风险 |
| P0 | 删除未使用的独立 Service 接口 | 所有模块 | 消除5个死代码文件 |
| P0 | 修复 PaymentServiceImpl.listMyPayments() 查询错误 | payment | 修复功能性 Bug |
| P1 | 统一使用 Assembler 替代手动转换 | 所有模块 | 消除5处重复转换代码 |
| P1 | 删除所有未使用的类（Entity/VO/DTO/Mapper/Constants） | 所有模块 | 减少约15个死代码文件 |
| P1 | Entity 继承 BaseEntity | 所有模块 | 消除8处重复字段定义 |
| P1 | 删除 OrderItemController | order | 消除不合理的 API 暴露 |
| P2 | 删除 Controller 中的冗余通用 CRUD 接口 | 多个模块 | 精简 API，提升可维护性 |
| P2 | 使用枚举替代硬编码字符串 | order, payment | 提升类型安全性 |
| P2 | 统一 CORS 配置方式 | common/config | 消除配置冗余 |
| P2 | 统一表命名策略 | 所有模块 | 提升一致性 |
| P3 | DTO 移除 id 字段 | 多个模块 | 规范化 API 设计 |
| P3 | 修复 N+1 查询和 SQL 注入风险 | scenario | 提升性能和安全性 |
| P3 | 补充订单创建的商品/库存校验 | order | 修复业务逻辑缺陷 |
