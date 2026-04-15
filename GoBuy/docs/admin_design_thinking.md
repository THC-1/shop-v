# 运营管理后台设计思路与待确认问题

**项目**: GoBuy 商城系统
**版本**: v1.0
**日期**: 2026-04-14

***

## 一、整体设计思路

### 1.1 模块划分

按照业务边界，将运营管理后台划分为 8 个核心模块：

| 模块       | 优先级 | 说明                 |
| -------- | --- | ------------------ |
| 管理员登录/登出 | P0  | 认证是所有功能的基础         |
| 角色与权限管理  | P0  | RBAC 权限模型，支持按钮级权限  |
| 商品管理     | P0  | 核心业务，包含上下架和批量操作    |
| 订单管理     | P0  | 核心业务，发货是运营最常用操作    |
| 分类管理     | P1  | 树形分类，支持 3 级嵌套      |
| 品牌管理     | P1  | 相对简单的 CRUD         |
| 用户管理     | P1  | 封禁、角色分配            |
| 数据看板     | P2  | 销售统计、UV/PV（可后期加缓存） |

### 1.2 技术架构

#### 1.2.1 认证方案

**JWT Token 扩展设计：**

```json
// 普通用户 Token Payload
{
  "sub": "123",
  "userId": 123,
  "username": "user001",
  "iat": 1713000000,
  "exp": 1713086400
}

// 管理员 Token Payload
{
  "sub": "1",
  "adminId": 1,
  "username": "admin",
  "roles": ["SUPER_ADMIN"],
  "iat": 1713000000,
  "exp": 1713014400  // 2小时过期
}
```

**区分方案：**

- 通过判断 Token Payload 中是否存在 `adminId` 字段来区分管理员和普通用户
- 管理员的 `userId` 为 null，普通用户的 `adminId` 为 null
- 登录接口使用不同的路径：`/api/v1/sessions`（用户）vs `/api/v1/admin/sessions`（管理员）

#### 1.2.2 项目目录结构建议

```
modules/
├── admin/
│   ├── controller/
│   │   ├── AdminAuthController.java      # 管理员登录/登出
│   │   ├── AdminController.java          # 管理员 CRUD
│   │   ├── RoleController.java           # 角色管理
│   │   ├── PermissionController.java     # 权限管理
│   │   └── DashboardController.java       # 数据看板
│   ├── service/
│   │   ├── AdminAuthService.java
│   │   ├── AdminService.java
│   │   ├── RoleService.java
│   │   ├── PermissionService.java
│   │   └── DashboardService.java
│   ├── entity/
│   │   ├── Admin.java
│   │   ├── AdminRole.java
│   │   ├── Role.java
│   │   ├── Permission.java
│   │   └── AdminLoginLog.java
│   ├── mapper/
│   │   ├── AdminMapper.java
│   │   ├── AdminRoleMapper.java
│   │   ├── RoleMapper.java
│   │   ├── PermissionMapper.java
│   │   └── AdminLoginLogMapper.java
│   ├── dto/
│   │   ├── AdminLoginDTO.java
│   │   ├── AdminCreateDTO.java
│   │   ├── RoleCreateDTO.java
│   │   └── RoleUpdateDTO.java
│   ├── vo/
│   │   ├── AdminVO.java
│   │   ├── RoleVO.java
│   │   └── PermissionTreeVO.java
│   └── assembler/
│       ├── AdminAssembler.java
│       └── RoleAssembler.java
├── product/
│   └── controller/
│       └── AdminProductController.java   # 后台商品管理
├── category/
│   └── controller/
│       └── AdminCategoryController.java   # 后台分类管理
├── brand/
│   └── controller/
│       └── AdminBrandController.java      # 后台品牌管理
├── order/
│   └── controller/
│       └── AdminOrderController.java      # 后台订单管理
└── user/
    └── controller/
        └── AdminUserController.java       # 后台用户管理
```

### 1.3 权限模型设计

采用 **RBAC（Role-Based Access Control）** 模型：

```
Admin ─── N:1 ─── AdminRole ─── N:1 ─── Role ─── N:1 ─── RolePermission ─── N:1 ─── Permission
                                                      │
                                                      └── N:1
                                                          Permission（自关联，树形）
```

**权限校验流程：**

```
请求 → JwtFilter → 解析 Token → 获取 adminId → 查询角色 → 查询权限 → 比对权限码 → 允许/拒绝
```

**权限码示例：**

- `PRODUCT:VIEW` - 查看商品
- `PRODUCT:EDIT` - 编辑商品
- `ORDER:SHIP` - 订单发货

***

## 二、关键技术方案

### 2.1 管理员登录流程

```
1. 前端调用 POST /api/v1/admin/sessions
2. 后端校验用户名密码
3. 查询管理员角色和权限
4. 生成 JWT Token（包含 adminId、username、roles）
5. 记录登录日志
6. 返回 token 给前端
```

### 2.2 权限校验方案

**方案 A：基于注解的权限校验（推荐）**

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    String value();
}

// 使用示例
@GetMapping("/products")
@RequirePermission("PRODUCT:VIEW")
public Result<?> listProducts() { ... }

// 拦截器/切面校验
@Aspect
@Component
public class PermissionAspect {
    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint point, RequirePermission requirePermission) {
        // 从 SecurityContext 获取当前管理员权限
        // 校验是否存在 requiredPermission.value()
        // 不存在则抛出 403 异常
    }
}
```

**方案 B：基于 SecurityConfig 的 URL 校验**

适用于菜单级权限，在 SecurityConfig 中配置路径规则。

### 2.3 分类树形结构处理

**数据库存储**：使用 `parent_id` 自关联

**查询方案**：

```sql
-- 一次性查询所有分类，在内存中构建树
SELECT * FROM categories ORDER BY sort ASC;
```

**Java 内存构建树**：

```java
public List<CategoryTreeVO> buildTree(List<Category> categories) {
    Map<Long, List<Category>> childrenMap = categories.stream()
        .filter(c -> c.getParentId() != null)
        .collect(Collectors.groupingBy(Category::getParentId));

    return categories.stream()
        .filter(c -> c.getParentId() == null)
        .map(c -> buildNode(c, childrenMap))
        .collect(Collectors.toList());
}
```

### 2.4 批量操作处理

商品批量上下架使用以下策略：

```java
public BatchStatusResult batchUpdateStatus(List<Long> productIds, String status) {
    // 1. 数量校验
    if (productIds.size() > 100) {
        throw new BusinessException(400, "批量操作最多支持100个商品");
    }

    // 2. 批量更新（使用 MyBatis-Plus 批量更新）
    // UPDATE products SET status = ? WHERE id IN (?, ?)

    // 3. 返回成功数和失败数
    return new BatchStatusResult(successCount, failCount, failIds);
}
```

### 2.5 数据统计方案

**实时查询 + Redis 缓存：**

```
请求 → 检查 Redis 缓存 → 有 → 返回
                    ↓ 无
              查询数据库 → 计算结果 → 写入 Redis（TTL=5分钟）→ 返回
```

**Redis Key 设计：**

```
dashboard:sales:summary:{startDate}:{endDate}
dashboard:sales:trend:{startDate}:{endDate}:{type}
dashboard:uvpv:{startDate}:{endDate}
```

**定时任务（每日凌晨）**：

- 汇总前一天的统计数据到 `daily_stats` 表
- 清空 Redis 缓存

### 2.6 UV/PV 统计方案

**前端埋点上报：**

```javascript
// 商品详情页访问时
fetch('/api/v1/admin/product-stats', {
  method: 'POST',
  body: JSON.stringify({ productId: 1, type: 'UV' }) // 或 'PV'
});
```

**后端处理：**

- 使用 Redis HyperLogLog 统计 UV（内存占用小）
- 使用 Redis Counter 统计 PV
- 每日凌晨将 Redis 数据持久化到 `product_stats` 表

***

## 三、待确认问题（新增）

### 3.1 认证相关

| 序号 | 问题                     | 说明                                | 回答                                                                                                                                                          |
| -- | ---------------------- | --------------------------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1  | 管理员密码忘记如何重置？           | 是否需要提供"忘记密码"功能，还是直接数据库修改？         | 直接去数据库修改即可                                                                                                                                                  |
| 2  | 同一浏览器同时登录普通用户和管理员会冲突吗？ | 如果冲突，是否允许？建议分开两个域名或使用不同的 Token 标识 | \*\*允许，前端分离存储。\*\*必然会冲突（如果你的前端把 Token 都叫 `token` 并存在同一个 `localStorage` 里）。解决办法极其简单：前台商城的 Token 存为 `mall-token`，后台管理系统的 Token 存为 `admin-token`。请求时分别带上，互不干扰。 |
| 3  | 登录失败次数是否需要限制？          | 如 5 分钟内连续失败 5 次后锁定账号              | 需要，利用 Spring Boot 配合 Redis，代码量极少。每次登录失败，在 Redis 中将 `admin:login_err:用户名` 的值加 1 并设置 5 分钟过期。登录接口前置校验这个 Key 的值是否大于 5 即可。                                       |

### 3.2 权限相关

| 序号 | 问题                | 说明                       | 回答                          |
| -- | ----------------- | ------------------------ | :-------------------------- |
| 4  | 超级管理员是否可以编辑自己的权限？ | 建议禁止，否则可能把自己禁了           | 禁止                          |
| 5  | 管理员被删除后，其角色如何处理？  | 是否需要同时删除 AdminRole 关联记录？ | **物理删除关联表记录**               |
| 6  | 权限树是否需要支持拖拽排序？    | 前端交互需求                   | 不需要，用数字序号 `sort` 字段代替，简单不出错 |

### 3.3 商品管理

| 序号 | 问题                      | 说明                                        | 回答                                                                                                                        |
| -- | ----------------------- | ----------------------------------------- | :------------------------------------------------------------------------------------------------------------------------ |
| 7  | 商品 SKU 如何管理？            | Admin 模块是否需要单独的 SKU 管理页面？                 | 商品编辑页内嵌（动态表单）千万不要做成单独的页面跳来跳去。在 Vue 3 发布商品时，选择“多规格”后，下方直接动态渲染出一个表格（包含颜色、尺寸组合出来的具体 SKU 以及对应价格、库存）。这部分前端逻辑略复杂，但做出来效果极具电商专业感。 |
| 8  | 商品上下架是否需要审核流程？          | 如果需要，则增加 status: PENDING\_AUDIT / AUDITED | 不需要审核                                                                                                                     |
| 9  | 商品删除时，如果 SKU 已生成订单如何处理？ | 建议只软删除，不真正物理删除                            | 软删除                                                                                                                       |
| 10 | 商品编辑后，是否需要记录操作日志？       | 核心操作建议记录                                  | <br />                                                                                                                    |

### 3.4 分类管理

| 序号 | 问题                 | 说明               | 回答             |
| -- | ------------------ | ---------------- | :------------- |
| 11 | 移动分类时，其子分类和商品如何处理？ | 建议子分类跟随移动，商品不变   | 子分类自动跟随，商品不受影响 |
| 12 | 分类是否需要启用/禁用状态？     | 如禁用则分类不显示，但不删除数据 | 需要             |

### 3.5 订单管理

| 序号 | 问题               | 说明           | 回答           |
| -- | ---------------- | ------------ | :----------- |
| 13 | 订单发货后是否可以修改物流信息？ | 建议允许，但记录修改日志 | 允许修改，但记录修改日志 |
| 14 | 是否需要批量发货功能？      | 一次选择多个订单发货   | 可以           |
| 15 | 订单导出是否需要支持？      | 暂不需要，但建议预留接口 | 可以预留接口       |

### 3.6 数据统计

| 序号 | 问题                | 说明              | 回答           |
| -- | ----------------- | --------------- | :----------- |
| 16 | UV/PV 统计需要精确到商品吗？ | 还是只需要总体统计？      | 不需要，只做总体统计   |
| 17 | 热销商品的统计周期？        | 默认近 7 天 / 30 天？ | 7天即可         |
| 18 | 是否需要数据大屏展示功能？     | 可能需要单独的页面       | 强烈建议作为独立页面呈现 |

***

## 四、实现计划建议

### 阶段一：基础框架（预计 1-2 天）

1. **数据库初始化**
   - 执行 `admin.sql` 脚本
   - 创建实体类 `Admin`, `Role`, `Permission`
   - 创建 Mapper 接口
2. **认证模块**
   - 实现管理员登录/登出
   - JWT Token 生成与解析
   - 登录日志记录
3. **权限框架**
   - 权限校验注解
   - 权限校验切面
   - SecurityConfig 改造

### 阶段二：核心业务（预计 2-3 天）

1. **角色管理**
   - 角色 CRUD
   - 权限分配
2. **管理员管理**
   - 管理员 CRUD
   - 角色分配
3. **商品管理**
   - 商品列表/详情
   - 商品创建/编辑/删除
   - 上下架/批量上下架
4. **订单管理**
   - 订单列表/详情
   - 订单发货

### 阶段三：完善功能（预计 1-2 天）

1. **分类管理**
   - 分类树形结构
   - CRUD 操作
2. **品牌管理**
   - 品牌 CRUD
3. **用户管理**
   - 用户列表/详情
   - 封禁/解封
   - 角色分配

### 阶段四：数据看板（预计 1-2 天）

1. **统计接口**
   - 销售汇总
   - 销售趋势
   - 热销商品
   - UV/PV 统计
2. **缓存优化**
   - Redis 缓存集成
   - 定时任务

### 阶段五：日志与监控（可选）

1. **操作日志**
2. **登录日志查询**

***

## 五、风险评估

| 风险点     | 影响      | 缓解措施                  |
| ------- | ------- | --------------------- |
| 权限模型过复杂 | 开发周期延长  | 第一阶段先做菜单级权限，按钮级权限后续迭代 |
| 批量操作性能  | 大数据量时超时 | 限制单次批量数量（100），使用异步处理  |
| 数据统计查询慢 | 报表加载时间长 | Redis 缓存 + 定时汇总       |
| 与现有系统冲突 | 需要改造    | Admin 模块完全独立，不影响现有业务  |

***

## 六、依赖关系图

```
                    ┌─────────────┐
                    │   Admin     │
                    │   Login     │
                    └──────┬──────┘
                           │
                           ▼
                    ┌─────────────┐
                    │   JWT       │
                    │   Filter    │
                    └──────┬──────┘
                           │
            ┌──────────────┼──────────────┐
            ▼              ▼              ▼
      ┌──────────┐   ┌──────────┐   ┌──────────┐
      │  Role    │   │ Product  │   │  Order   │
      │ Service  │   │ Service  │   │ Service  │
      └────┬─────┘   └────┬─────┘   └────┬─────┘
           │              │              │
           ▼              ▼              ▼
      ┌──────────┐   ┌──────────┐   ┌──────────┐
      │ Permission│  │Category  │   │   User   │
      │ Service  │   │ Service  │   │ Service  │
      └──────────┘   └──────────┘   └──────────┘
                            │
                            ▼
                      ┌──────────┐
                      │Dashboard │
                      │ Service  │
                      └──────────┘
```

***

**文档状态**: 待审阅
**问题总数**: 18 个
**建议优先级**: 先确认认证方案和权限模型，其他可后续迭代

***

## 七、补充问题与新想法

### 7.1 商品管理补充

| 序号 | 问题                        | 说明             | 回答                                      |
| -- | ------------------------- | -------------- | --------------------------------------- |
| 19 | 商品编辑后是否需要记录操作日志？          | 核心操作建议记录       | 需要                                      |
| 20 | 商品多规格（SKU）的价格库存如何校验？      | 防止负库存、超低价等异常   | 建议最低售价不低于原价的 10%，库存不得为负                 |
| 21 | 商品列表是否需要支持自定义排序？          | 如按销量、价格、创建时间排序 | 建议支持，前端已有排序交互                           |
| 22 | 商品搜索是否需要支持品牌+分类+价格区间组合筛选？ | 运营常用功能         | 建议支持，接口已有 categoryId + brandId + 价格区间参数 |

### 7.2 订单管理补充

| 序号 | 问题                  | 说明            | 回答                                                                                                                   |
| -- | ------------------- | ------------- | -------------------------------------------------------------------------------------------------------------------- |
| 23 | 批量发货的交互方式？          | 前端如何选择多个订单？   | 建议：勾选复选框 → 点击"批量发货"按钮 → 弹出模态框填写统一的物流信息                                                                               |
| 24 | 订单发货时是否需要库存校验？      | 商品库存不足时是否阻止发货 | **下单/支付时扣减库存（预扣减）。** 业界通用做法是：用户提交订单（或支付成功）的瞬间就扣减数据库库存。发货仅仅是一个**状态的流转**（从 `TO_BE_SHIPPED` 改为 `SHIPPED`），不需要且不能再次校验库存。 |
| 25 | 订单列表是否需要支持按收货人信息搜索？ | 手机号、收货人姓名     | 建议支持，运营常用地毯式搜索                                                                                                       |
| 26 | 取消订单后是否需要自动退库存？     | 涉及库存回滚        | 必须回滚库存                                                                                                               |

### 7.3 数据看板补充

| 序号 | 问题              | 说明                                              | 回答                                                                   |
| -- | --------------- | ----------------------------------------------- | -------------------------------------------------------------------- |
| 27 | 数据大屏需要展示哪些核心指标？ | 设计参考                                            | 建议：实时订单数、今日销售额、累计用户数、TOP 5 热销商品、24小时销售趋势图                            |
| 28 | 热销商品的销量数据来源？    | 从 order\_items 实时统计还是从 product.sales\_count 获取？ | 建议以 order\_items 实时统计为准，product.sales\_count 作为缓存加速                  |
| 29 | UV/PV 埋点如何实现？   | 需要修改前端商品详情页                                     | 前端在每次进入商品详情页时，调用 `POST /api/v1/admin/product-stats` 上报，需要与前端团队确认实现时机 |

### 7.4 分类管理补充

| 序号 | 问题                | 说明               | 回答                             |
| -- | ----------------- | ---------------- | ------------------------------ |
| 30 | 分类启用/禁用状态对子分类的影响？ | 父分类禁用后子分类是否自动隐藏？ | 建议：禁用父分类 → 所有子分类也视为禁用，前端递归处理   |
| 31 | 分类移动的交互方式？        | 前端如何选择目标父分类      | 建议：点击"移动"按钮 → 弹出树形选择器 → 选择目标分类 |

### 7.5 安全性补充

| 序号 | 问题                        | 说明                          | 回答                                                                                                                                                                                                      |
| -- | ------------------------- | --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 32 | 管理员操作是否需要记录操作来源 IP？       | 结合 admin\_operation\_logs 表 | 已设计日志表，需要在 Filter/Interceptor 中获取 IP 并注入，技术实现上，可以在 AOP 切面中通过 `RequestContextHolder.getRequestAttributes()` 拿到 `HttpServletRequest`。注意要处理 Nginx 代理的情况，优先获取 `X-Forwarded-For` 请求头，拿不到再用 `getRemoteAddr()` |
| 33 | 敏感操作（如删除商品、封禁用户）是否需要二次确认？ | 防止误操作                       | 建议前端二次确认弹窗，后端不做限制                                                                                                                                                                                       |
| 34 | 管理员是否可以查看其他管理员的信息？        | 涉及隐私                        | 建议只有 SUPER\_ADMIN 可以查看所有管理员，普通管理员只能查看自己                                                                                                                                                                 |

### 7.6 前端交互新想法

**Idea 1: 仪表盘首页快捷操作栏**

```
┌─────────────────────────────────────────────────────────┐
│  📦 待发货订单(12)  │  ⚠️ 库存不足(5)  │  👥 新注册用户(28)  │
└─────────────────────────────────────────────────────────┘
```

点击可直接跳转到对应列表页，运营一眼看到需要处理的事项。

**Idea 2: 商品列表快捷操作**

在商品列表每行末尾增加快捷操作按钮：

- 快速上下架切换（toggle）
- 快速编辑（弹窗编辑商品名、价格）
- 复制商品链接

**Idea 3: 订单发货页面增强**

```
┌─────────────────────────────────────────┐
│  已选择 3 个订单                        │
│  ┌─────────────────────────────────┐   │
│  │ 物流公司： [顺丰速运        ▼]   │   │
│  │ 运单号：   [SF123456789    ]    │   │
│  │           [批量填充运单号]       │   │
│  └─────────────────────────────────┘   │
│                                         │
│  订单预览：                              │
│  - ORD202604140001 - iPhone 15 Pro     │
│  - ORD202604140002 - MacBook Pro       │
│  - ORD202604140003 - AirPods Pro       │
└─────────────────────────────────────────┘
```

**Idea 4: 分类管理的拖拽排序（争议）**

虽然您回答不需要拖拽排序，但提供一个备选方案：**在分类列表页面，每个分类行有一个 ↑ ↓ 排序按钮**，点击后该分类与相邻的同级分类交换 sort 值。比拖拽实现简单，但体验也不错。

***

## 八、技术方案补充

### 8.1 登录失败限流实现

```java
// Redis Key: admin:login:fail:{username}
// Value: 失败次数
// TTL: 5分钟

public boolean isLoginAllowed(String username) {
    String key = "admin:login:fail:" + username;
    String count = redisTemplate.opsForValue().get(key);
    return count == null || Integer.parseInt(count) < 5;
}

public void recordLoginFail(String username) {
    String key = "admin:login:fail:" + username;
    redisTemplate.opsForValue().increment(key);
    redisTemplate.expire(key, 5, TimeUnit.MINUTES);
}

public void clearLoginFail(String username) {
    redisTemplate.delete("admin:login:fail:" + username);
}
```

### 8.2 分类树查询优化

考虑分类数据量不大（通常几百条），建议使用 MyBatis-Plus 的 `ServiceWrapper` 一次性查询所有分类：

```java
public List<CategoryTreeVO> getCategoryTree() {
    List<Category> allCategories = categoryService.list(
        new LambdaQueryWrapper<Category>()
            .eq(Category::getStatus, "ACTIVE")
            .orderByAsc(Category::getSort)
    );
    return buildTree(allCategories);
}
```

### 8.3 批量发货事务保证

```java
@Transactional(rollbackFor = Exception.class)
public void batchShip(List<Long> orderIds, String expressCompany, String expressNo) {
    // 1. 查询所有待发货订单
    List<Order> orders = orderService.list(
        new LambdaQueryWrapper<Order>()
            .in(Order::getId, orderIds)
            .eq(Order::getStatus, OrderStatusEnum.PAID.name())
    );

    // 2. 校验数量一致性
    if (orders.size() != orderIds.size()) {
        throw new BusinessException(422, "部分订单状态不允许发货");
    }

    // 3. 批量更新状态
    Order update = new Order();
    update.setExpressCompany(expressCompany);
    update.setExpressNo(expressNo);
    update.setStatus(OrderStatusEnum.SHIPPED.name());
    update.setShippedAt(LocalDateTime.now());

    orderService.update(update, new LambdaUpdateWrapper<Order>()
        .in(Order::getId, orderIds)
        .eq(Order::getStatus, OrderStatusEnum.PAID.name())
    );
}
```

***

## 九、接口调整记录

| 日期         | 变更内容              | 说明                          |
| ---------- | ----------------- | --------------------------- |
| 2026-04-14 | SKU 管理入口改为商品编辑页内嵌 | 用户明确：不需要单独页面，在商品编辑页内嵌动态规格表格 |
| 2026-04-14 | 数据大屏建议作为独立页面      | 用户明确：强烈建议，需要专业感             |
| 2026-04-14 | 热销商品统计周期改为 7 天    | 用户确认：7天即可                   |

***

**最后更新**: 2026-04-14
**新增问题数**: 16 个（累计 34 个）
