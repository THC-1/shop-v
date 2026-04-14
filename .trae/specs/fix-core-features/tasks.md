# Tasks

- [ ] Task 1: 实现用户模块核心功能
  - [ ] SubTask 1.1: 实现 UserService 接口（login, register, getUserInfo, updateUserInfo, logout）
  - [ ] SubTask 1.2: 在 UserController 添加登录、注册、当前用户信息接口
  - [ ] SubTask 1.3: 实现密码加密（BCrypt）
  - [ ] SubTask 1.4: 添加接口权限控制（登录接口无需认证，其他需要认证）

- [ ] Task 2: 实现订单模块业务功能
  - [ ] SubTask 2.1: 实现 OrderService 的 listOrders 方法（分页 + 状态筛选）
  - [ ] SubTask 2.2: 实现 getOrderDetail 方法（包含订单项和收货地址）
  - [ ] SubTask 2.3: 实现 confirmOrder 方法（确认收货）
  - [ ] SubTask 2.4: 实现 refundOrder 方法（申请退款）
  - [ ] SubTask 2.5: 在 OrderController 添加对应接口

- [ ] Task 3: 实现商品搜索和库存管理
  - [ ] SubTask 3.1: 实现 ProductService 的 searchProducts 方法（多条件查询）
  - [ ] SubTask 3.2: 实现 getCategoryProducts 方法（按分类查询）
  - [ ] SubTask 3.3: 实现 getStock 和 updateStock 方法
  - [ ] SubTask 3.4: 在 ProductController 添加对应接口

- [ ] Task 4: 优化购物车模块
  - [ ] SubTask 4.1: 修改 CartController 使用 CartAddDTO 替代 CartDTO
  - [ ] SubTask 4.2: 修改购物车列表接口使用 UserContextHolder 获取 userId
  - [ ] SubTask 4.3: 实现购物车项选中/取消选中功能
  - [ ] SubTask 4.4: 在 CartItemVO 中添加 selected 字段的 getter/setter

- [ ] Task 5: 实现地址管理功能
  - [ ] SubTask 5.1: 实现 AddressService 的 listMyAddresses 方法
  - [ ] SubTask 5.2: 实现 setDefaultAddress 方法
  - [ ] SubTask 5.3: 实现 getDefaultAddress 方法
  - [ ] SubTask 5.4: 在 AddressController 添加对应接口

- [ ] Task 6: 完善支付模块（模拟实现）
  - [ ] SubTask 6.1: 实现 PaymentService 的 handleCallback 方法（模拟支付回调）
  - [ ] SubTask 6.2: 实现 refundPayment 方法
  - [ ] SubTask 6.3: 实现 listMyPayments 方法
  - [ ] SubTask 6.4: 在 PaymentController 添加对应接口

- [ ] Task 7: 实现场景推荐功能
  - [ ] SubTask 7.1: 实现 ScenarioService 的 listRecommendScenarios 方法
  - [ ] SubTask 7.2: 实现 getScenarioProducts 方法
  - [ ] SubTask 7.3: 在 ScenarioController 添加对应接口

- [ ] Task 8: 添加事务管理和异常处理
  - [ ] SubTask 8.1: 在关键业务方法上添加 @Transactional 注解
  - [ ] SubTask 8.2: 完善业务异常处理（库存不足、订单状态错误等）
  - [ ] SubTask 8.3: 添加数据校验逻辑

# Task Dependencies
- Task 1 是其他任务的基础（用户认证）
- Task 2 依赖于 Task 1（订单查询需要用户认证）
- Task 4 依赖于 Task 1（购物车操作需要用户认证）
- Task 5 依赖于 Task 1（地址管理需要用户认证）
- Task 6 依赖于 Task 2（支付基于订单）
