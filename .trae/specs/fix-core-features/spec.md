# GoBuy 电商系统完整修复方案

## Why
当前 GoBuy 电商系统存在严重的功能缺失问题，用户无法登录注册、商品查询功能不完善、购物车接口设计不合理、订单和支付流程不完整。这导致系统无法与前端正常对接，核心购物流程无法跑通。需要全面修复各模块的业务逻辑和 API 接口。

## What Changes

### P0 - 核心功能修复（必须实现）

#### 1. 用户模块 - 实现登录注册和当前用户管理
- **新增接口**:
  - `POST /api/v1/users/login` - 用户登录（生成 JWT token）
  - `POST /api/v1/users/register` - 用户注册（密码加密存储）
  - `GET /api/v1/users/me` - 获取当前用户信息
  - `PUT /api/v1/users/me` - 更新当前用户信息
  - `POST /api/v1/users/logout` - 用户登出（可选，因 JWT 无状态）
- **实现 UserService**: 完成 login, register, getUserInfo, updateUserInfo, logout 方法
- **密码加密**: 使用 BCrypt 加密存储密码

#### 2. 订单模块 - 实现用户订单查询和状态流转
- **新增接口**:
  - `GET /api/v1/orders/my` - 查询当前用户订单列表（分页 + 状态筛选）
  - `GET /api/v1/orders/{id}/detail` - 获取订单详情（含订单项和收货地址）
  - `PUT /api/v1/orders/{id}/confirm` - 确认收货
  - `PUT /api/v1/orders/{id}/refund` - 申请退款
  - `DELETE /api/v1/orders/{id}` - 删除订单（软删除）
- **实现 OrderService**: 完成 listOrders, getOrderDetail, confirmOrder, refundOrder 等方法
- **订单状态流转**: 完善订单状态管理（pending→paid→shipped→delivered→completed）

#### 3. 商品模块 - 实现搜索和库存管理
- **新增接口**:
  - `GET /api/v1/products/search` - 商品搜索（关键词、分类、价格区间、分页）
  - `GET /api/v1/products/category/{categoryId}` - 按分类查询商品
  - `GET /api/v1/products/{id}/stock` - 查询商品库存
  - `PUT /api/v1/products/{id}/stock` - 更新商品库存
- **使用 ProductQueryDTO**: 实现复杂的查询条件
- **库存管理**: 实现库存扣减和更新逻辑

### P1 - 重要功能完善（强烈建议）

#### 4. 购物车模块 - 优化接口设计
- **修复接口**:
  - `POST /api/v1/carts/items` - 使用 CartAddDTO 接收参数
  - `GET /api/v1/carts/items` - 获取当前用户购物车列表（使用 userId）
  - `PUT /api/v1/carts/items/{id}/select` - 选中/取消选中商品
- **移除旧接口**: 删除使用 CartDTO 的接口
- **优化查询**: 购物车列表接口必须使用 UserContextHolder 获取 userId

#### 5. 地址模块 - 实现用户关联
- **新增接口**:
  - `GET /api/v1/addresses/my` - 获取当前用户的地址列表
  - `PUT /api/v1/addresses/{id}/default` - 设置为默认地址
  - `GET /api/v1/addresses/default` - 获取默认地址
- **地址管理**: 实现默认地址逻辑，一个用户只能有一个默认地址

#### 6. 支付模块 - 完善支付流程（部分实现，跳过第三方对接）
- **新增接口**:
  - `POST /api/v1/payments/{id}/callback` - 支付回调处理（模拟）
  - `PUT /api/v1/payments/{id}/refund` - 退款处理
  - `GET /api/v1/payments/my` - 查询当前用户的支付记录
- **支付状态管理**: 实现支付状态更新逻辑

### P2 - 辅助功能增强（建议实现）

#### 7. 场景模块 - 实现推荐功能
- **新增接口**:
  - `GET /api/v1/scenarios/recommend` - 推荐场景列表
  - `GET /api/v1/scenarios/{id}/products` - 获取场景关联的商品列表
- **场景商品关联**: 实现场景与商品的关联查询

## Impact

### 受影响的 Service
- **UserServiceImpl**: 从空实现变为完整业务逻辑
- **OrderServiceImpl**: 实现订单查询和状态流转
- **ProductServiceImpl**: 实现商品搜索和库存管理
- **CartServiceImpl**: 优化购物车操作
- **AddressServiceImpl**: 实现地址管理
- **PaymentServiceImpl**: 实现支付流程

### 受影响的 Controller
- **UserController**: 新增 5 个接口
- **OrderController**: 新增 5 个接口，重构现有接口
- **ProductController**: 新增 4 个接口
- **CartController**: 修复现有接口，新增选中接口
- **AddressController**: 新增 3 个接口
- **PaymentController**: 新增 3 个接口
- **ScenarioController**: 新增 2 个接口

### 数据库影响
- **users 表**: 密码字段存储加密后的值
- **orders 表**: 新增订单状态字段（如已确认、已完成等）
- **addresses 表**: is_default 字段使用
- **products 表**: stock 字段的管理

## ADDED Requirements

### Requirement: 用户认证
系统 SHALL 提供用户注册、登录、获取当前用户信息、更新用户信息的完整功能

#### Scenario: 用户注册成功
- **WHEN** 用户提供用户名、密码、邮箱
- **THEN** 系统创建用户，密码加密存储，返回成功

#### Scenario: 用户登录成功
- **WHEN** 用户提供正确的用户名和密码
- **THEN** 系统验证通过，返回 JWT token

#### Scenario: 获取当前用户信息
- **WHEN** 用户携带有效 token 访问 `/api/v1/users/me`
- **THEN** 系统返回当前登录用户的详细信息

### Requirement: 订单管理
系统 SHALL 提供用户订单查询、详情查看、确认收货、申请退款等功能

#### Scenario: 查询我的订单
- **WHEN** 用户访问 `/api/v1/orders/my?pageNum=1&pageSize=10&status=1`
- **THEN** 系统返回当前用户的订单列表（分页，带状态筛选）

#### Scenario: 查看订单详情
- **WHEN** 用户访问 `/api/v1/orders/{id}/detail`
- **THEN** 系统返回订单详细信息，包含订单项列表和收货地址

### Requirement: 商品搜索
系统 SHALL 提供多条件商品搜索功能

#### Scenario: 搜索商品
- **WHEN** 用户访问 `/api/v1/products/search?keyword=手机&categoryId=1&minPrice=1000&maxPrice=5000&pageNum=1`
- **THEN** 系统返回符合条件的商品列表（分页）

### Requirement: 购物车管理
系统 SHALL 提供购物车项的添加、查询、更新、删除、选中/取消选中功能

#### Scenario: 添加商品到购物车
- **WHEN** 用户 POST `/api/v1/carts/items` 携带 productId, skuId, quantity
- **THEN** 系统将商品添加到当前用户的购物车

#### Scenario: 获取购物车列表
- **WHEN** 用户 GET `/api/v1/carts/items`
- **THEN** 系统返回当前用户的购物车项列表（包含选中状态）

### Requirement: 地址管理
系统 SHALL 提供用户地址的增删改查、设置默认地址功能

#### Scenario: 设置默认地址
- **WHEN** 用户 PUT `/api/v1/addresses/{id}/default`
- **THEN** 系统将该地址设为默认，并将其他地址设为非默认

## MODIFIED Requirements

### Requirement: Controller 层
Controller 层 SHALL 使用 DTO 接收请求参数，使用 VO 返回响应，严禁直接使用实体类

### Requirement: Service 层
Service 层 SHALL 实现完整的业务逻辑，包括：
- 数据校验
- 事务管理（@Transactional）
- 业务规则检查（如库存检查、价格计算）

### Requirement: 分页处理
所有列表查询接口 SHALL 支持分页，使用 IPage<T> 返回分页结果

## REMOVED Requirements

### Requirement: 购物车旧接口
**Reason**: CartDTO 包含 userId 字段，不符合 RESTful 设计（userId 应从 token 获取）
**Migration**: 使用 CartAddDTO 替代，userId 从 UserContextHolder 获取

### Requirement: 基础 CRUD 接口
**Reason**: 大部分模块的 list()、getById()、save()、update()、delete() 接口不符合业务场景
**Migration**: 替换为业务导向的接口，如 `/my`、`/detail`、`/search` 等
