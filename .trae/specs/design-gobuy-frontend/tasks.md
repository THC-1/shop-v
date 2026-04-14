# Tasks

- [x] Task 1: 项目基础设施搭建 - 安装依赖、配置 Tailwind CSS、配置路径别名、全局样式
  - [x] SubTask 1.1: 安装 axios、tailwindcss、@tailwindcss/vite 等必要依赖
  - [x] SubTask 1.2: 配置 Tailwind CSS（淘宝橙主题色 #ff5000、最大宽度 1190px）
  - [x] SubTask 1.3: 配置 vite.config.ts 代理后端 API（/api → localhost:8080）
  - [x] SubTask 1.4: 创建全局样式文件 src/styles/main.css（字体、基础样式、商品卡片 hover 效果）

- [x] Task 2: API 请求层封装 - 创建统一的 axios 实例和按模块封装的 API 调用
  - [x] SubTask 2.1: 创建 src/api/request.ts（axios 实例、请求/响应拦截器、token 注入、401 处理）
  - [x] SubTask 2.2: 创建 src/api/user.ts（login, register, getMe, updateMe, logout）
  - [x] SubTask 2.3: 创建 src/api/product.ts（list, getById, search, getByCategory, getStock）
  - [x] SubTask 2.4: 创建 src/api/cart.ts（addItem, list, updateQuantity, deleteItem, toggleSelect, clear）
  - [x] SubTask 2.5: 创建 src/api/order.ts（create, listMy, getDetail, confirm, refund, cancel, deleteOrder）
  - [x] SubTask 2.6: 创建 src/api/payment.ts（getByOrderId, listMy, handleCallback, refund）
  - [x] SubTask 2.7: 创建 src/api/scenario.ts（listRecommend, getById, getProducts）
  - [x] SubTask 2.8: 创建 src/api/address.ts（listMy, setDefault, getDefault, create, update, deleteAddress）

- [x] Task 3: Pinia 状态管理 - 创建用户和购物车 Store
  - [x] SubTask 3.1: 创建 src/stores/user.ts（用户信息、token 管理、登录/登出 action）
  - [x] SubTask 3.2: 创建 src/stores/cart.ts（购物车列表、商品数量、选中状态、加购/删除 action）

- [x] Task 4: 路由配置 - 配置全部页面路由和导航守卫
  - [x] SubTask 4.1: 配置路由表（/, /scenarios/:id, /products, /products/:id, /cart, /checkout, /orders, /login, /user）
  - [x] SubTask 4.2: 实现路由守卫（未登录访问需认证页面时跳转 /login）

- [x] Task 5: 通用布局组件 - 顶部导航栏和底部页脚
  - [x] SubTask 5.1: 创建 src/components/layout/AppHeader.vue（顶部导航条+搜索栏，购物车角标显示数量）
  - [x] SubTask 5.2: 创建 src/components/layout/AppFooter.vue（底部信息区）
  - [x] SubTask 5.3: 修改 App.vue 使用布局组件包裹 router-view

- [x] Task 6: 通用业务组件
  - [x] SubTask 6.1: 创建 src/components/ProductCard.vue（商品卡片：图片+名称+价格+标签，hover 效果）
  - [x] SubTask 6.2: 创建 src/components/Carousel.vue（轮播图：自动播放+左右箭头+圆点指示器）
  - [x] SubTask 6.3: 创建 src/components/Pagination.vue（分页器：页码+上下页）
  - [x] SubTask 6.4: 创建 src/components/CategorySidebar.vue（分类侧栏：hover 展开子分类）

- [ ] Task 7: 首页 - 淘宝风格首页
  - [ ] SubTask 7.1: 创建 src/views/HomeView.vue，实现三栏布局（分类侧栏+轮播图+用户面板）
  - [ ] SubTask 7.2: 实现场景推荐横幅区（调用 scenarios API）
  - [ ] SubTask 7.3: 实现猜你喜欢商品瀑布流（调用 products API，5列网格）
  - [ ] SubTask 7.4: 实现搜索功能（输入关键词跳转商品列表页）

- [ ] Task 8: 场景详情页 - 参考场景页.html 设计
  - [ ] SubTask 8.1: 创建 src/views/ScenarioView.vue，实现英雄海报区（封面图+渐变遮罩+场景信息）
  - [ ] SubTask 8.2: 实现左侧商品分区展示（4列网格，按分类分组）
  - [ ] SubTask 8.3: 实现右侧粘性购物清单面板（勾选列表+进度条+一键加购）

- [ ] Task 9: 商品列表页 - 搜索和筛选
  - [ ] SubTask 9.1: 创建 src/views/ProductListView.vue，实现搜索栏+筛选条件+排序选项
  - [ ] SubTask 9.2: 实现商品网格展示+分页器

- [ ] Task 10: 商品详情页
  - [ ] SubTask 10.1: 创建 src/views/ProductDetailView.vue，实现图片轮播+商品信息+规格选择+加购按钮

- [ ] Task 11: 购物车页
  - [ ] SubTask 11.1: 创建 src/views/CartView.vue，实现购物车列表（全选/单选+数量调整+删除+小计+总价+结算按钮）

- [ ] Task 12: 订单确认页
  - [ ] SubTask 12.1: 创建 src/views/CheckoutView.vue，实现地址选择+商品清单+备注+总价+提交订单

- [ ] Task 13: 订单列表页
  - [ ] SubTask 13.1: 创建 src/views/OrdersView.vue，实现状态 Tab 筛选+订单卡片列表+操作按钮（确认收货/取消/删除）

- [ ] Task 14: 登录/注册页
  - [ ] SubTask 14.1: 创建 src/views/LoginView.vue，实现登录/注册 Tab 切换表单

- [ ] Task 15: 用户中心页
  - [ ] SubTask 15.1: 创建 src/views/UserView.vue，实现用户信息展示/编辑+地址管理（增删改查+设置默认）

# Task Dependencies
- Task 1 → Task 2, 3, 4, 5（基础设施先行）
- Task 2 → Task 3（Store 依赖 API 层）
- Task 3 → Task 4（路由守卫依赖 user store）
- Task 5, 6 → Task 7, 8, 9, 10, 11, 12, 13, 14, 15（页面依赖通用组件）
- Task 7, 8 可并行开发
- Task 9, 10 可并行开发
- Task 11, 12, 13 可并行开发
- Task 14, 15 可并行开发
