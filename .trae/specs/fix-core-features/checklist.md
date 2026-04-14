# Checklist

## 用户模块
- [x] POST /api/v1/users/login 接口实现，返回 JWT token
- [x] POST /api/v1/users/register 接口实现，密码加密存储
- [x] GET /api/v1/users/me 接口实现，返回当前用户信息
- [x] PUT /api/v1/users/me 接口实现，更新当前用户信息
- [x] POST /api/v1/users/logout 接口实现（可选）
- [x] UserService 的 login 方法实现（用户名密码验证，生成 token）
- [x] UserService 的 register 方法实现（密码加密，用户创建）
- [x] UserService 的 getUserInfo 方法实现
- [x] UserService 的 updateUserInfo 方法实现
- [x] UserService 的 logout 方法实现

## 订单模块
- [x] GET /api/v1/orders/my 接口实现（分页 + 状态筛选）
- [x] GET /api/v1/orders/{id}/detail 接口实现（包含订单项）
- [x] PUT /api/v1/orders/{id}/confirm 接口实现（确认收货）
- [x] PUT /api/v1/orders/{id}/refund 接口实现（申请退款）
- [x] DELETE /api/v1/orders/{id} 接口实现（软删除）
- [x] OrderService 的 listOrders 方法实现（IPage 分页）
- [x] OrderService 的 getOrderDetail 方法实现（包含 items 列表）
- [x] OrderService 的 confirmOrder 方法实现
- [x] OrderService 的 refundOrder 方法实现

## 商品模块
- [x] GET /api/v1/products/search 接口实现（多条件查询）
- [x] GET /api/v1/products/category/{categoryId} 接口实现
- [x] GET /api/v1/products/{id}/stock 接口实现
- [x] PUT /api/v1/products/{id}/stock 接口实现
- [x] ProductService 的 searchProducts 方法实现（ProductQueryDTO）
- [x] ProductService 的 getCategoryProducts 方法实现
- [x] ProductService 的 getStock 方法实现
- [x] ProductService 的 updateStock 方法实现

## 购物车模块
- [x] POST /api/v1/carts/items 接口使用 CartAddDTO
- [x] GET /api/v1/carts/items 接口使用 UserContextHolder 获取 userId
- [x] PUT /api/v1/carts/items/{id}/select 接口实现（选中/取消选中）
- [x] CartService 的 addCartItem 方法实现
- [x] CartService 的 listCartItems 方法实现（返回 selected 字段）
- [x] CartController 的旧接口移除（使用 CartDTO 的接口）

## 地址模块
- [x] GET /api/v1/addresses/my 接口实现
- [x] PUT /api/v1/addresses/{id}/default 接口实现
- [x] GET /api/v1/addresses/default 接口实现
- [x] AddressService 的 listMyAddresses 方法实现
- [x] AddressService 的 setDefaultAddress 方法实现（处理唯一默认地址）
- [x] AddressService 的 getDefaultAddress 方法实现

## 支付模块
- [x] POST /api/v1/payments/{id}/callback 接口实现（模拟回调）
- [x] PUT /api/v1/payments/{id}/refund 接口实现
- [x] GET /api/v1/payments/my 接口实现
- [x] PaymentService 的 handleCallback 方法实现
- [x] PaymentService 的 refundPayment 方法实现
- [x] PaymentService 的 listMyPayments 方法实现

## 场景模块
- [x] GET /api/v1/scenarios/recommend 接口实现
- [x] GET /api/v1/scenarios/{id}/products 接口实现
- [x] ScenarioService 的 listRecommendScenarios 方法实现
- [x] ScenarioService 的 getScenarioProducts 方法实现

## 代码质量
- [x] 所有业务方法添加 @Transactional 注解
- [x] 所有接口添加适当的 Swagger 文档注释
- [x] 所有接口添加权限控制（需要认证的接口使用拦截器）
- [x] 异常处理完善（BusinessException 抛出）
- [x] 数据校验完善（@Valid 注解）
- [x] 代码符合命名规范（方法名 camelCase，类名 PascalCase）
- [x] 无重复代码（DRY 原则）
- [x] 单一职责原则（方法不超过 60 行）

## 测试验证
- [x] 用户注册流程测试
- [x] 用户登录流程测试
- [x] 获取/更新当前用户信息测试
- [x] 订单创建流程测试
- [x] 订单查询流程测试
- [x] 商品搜索功能测试
- [x] 购物车添加/查询测试
- [x] 地址管理功能测试
- [x] 支付回调流程测试（模拟）
