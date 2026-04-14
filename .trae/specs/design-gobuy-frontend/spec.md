# GoBuy 前端设计规格

## Why
GoBuy 电商系统后端已提供完整的 38 个 RESTful API 接口（用户、商品、购物车、订单、支付、场景、地址共 7 大模块），但前端项目 GoBuy-fronted 目前仅有 Vue 3 + TypeScript + Vite 的空壳，无任何页面、路由或组件。需要基于后端接口设计并实现一套模仿淘宝风格的完整电商前端，参考已有的场景页.html 和首页.html 的设计风格。

## What Changes
- 新增项目基础设施：API 请求封装（axios）、路由配置、状态管理（Pinia stores）、全局样式
- 新增 9 个核心页面：首页、场景页、商品列表页、商品详情页、购物车页、订单确认页、订单列表页、登录/注册页、用户中心页
- 新增通用组件：顶部导航栏、底部页脚、商品卡片、轮播图、分页器、地址选择器等
- 新增 API 层：按模块封装全部 38 个后端接口调用

## Impact
- Affected code: GoBuy-fronted 整个项目（当前为空壳）
- 依赖后端 API: `/api/v1/users`, `/api/v1/products`, `/api/v1/carts/items`, `/api/v1/orders`, `/api/v1/payments`, `/api/v1/scenarios`, `/api/v1/addresses`

## 设计方向

### 美学风格
- **淘宝橙** (#ff5000) 为主色调，搭配暖灰色背景 (#f4f4f4)
- 字体：'Helvetica Neue', 'Microsoft Yahei', sans-serif
- 卡片式布局，圆角设计，悬浮阴影动效
- 商品卡片 hover 时边框变橙、微上浮、阴影加深
- 最大内容宽度 1190px，居中布局

### 页面结构总览

| 页面 | 路由 | 对应后端 API | 参考设计 |
|------|------|-------------|---------|
| 首页 | `/` | products, scenarios | 首页.html |
| 场景详情页 | `/scenarios/:id` | scenarios, products | 场景页.html |
| 商品列表页 | `/products` | products/search | 首页.html 猜你喜欢区 |
| 商品详情页 | `/products/:id` | products/{id}, carts | - |
| 购物车页 | `/cart` | carts/items | - |
| 订单确认页 | `/checkout` | orders, addresses | - |
| 订单列表页 | `/orders` | orders/my | - |
| 登录/注册页 | `/login` | users/login, register | - |
| 用户中心页 | `/user` | users/me, addresses | - |

---

## ADDED Requirements

### Requirement: 项目基础设施
系统 SHALL 搭建前端项目基础设施，包括 API 请求层、路由、状态管理和全局样式

#### Scenario: API 请求封装
- **WHEN** 前端需要调用后端接口
- **THEN** 通过统一的 axios 实例发送请求，自动携带 JWT token，统一处理错误响应

#### Scenario: 路由守卫
- **WHEN** 用户访问需要认证的页面（购物车、订单等）
- **THEN** 系统检查 localStorage 中的 token，未登录则跳转登录页

---

### Requirement: 首页
系统 SHALL 提供淘宝风格的首页，包含顶部导航、搜索栏、分类侧栏、轮播图、场景推荐和商品推荐

#### Scenario: 首页加载
- **WHEN** 用户访问首页 `/`
- **THEN** 页面展示：顶部导航条 → 搜索区域 → 分类+轮播+用户面板 → 场景推荐横幅 → 猜你喜欢商品瀑布流

#### Scenario: 场景推荐展示
- **WHEN** 首页加载完成
- **THEN** 调用 `GET /api/v1/scenarios` 获取推荐场景列表，以横幅卡片形式展示（封面图+名称+描述）

#### Scenario: 商品推荐展示
- **WHEN** 首页加载完成
- **THEN** 调用 `GET /api/v1/products` 获取商品列表，以 5 列网格卡片展示（图片+名称+价格+销量标签）

#### Scenario: 搜索商品
- **WHEN** 用户在搜索框输入关键词并点击搜索
- **THEN** 跳转到 `/products?keyword=xxx` 商品列表页

---

### Requirement: 场景详情页
系统 SHALL 提供场景详情页，展示场景英雄海报、分类商品推荐和右侧悬浮购物清单

#### Scenario: 场景页加载
- **WHEN** 用户访问 `/scenarios/:id`
- **THEN** 调用 `GET /api/v1/scenarios/{id}` 获取场景信息，调用 `GET /api/v1/scenarios/{id}/products` 获取推荐商品

#### Scenario: 场景英雄海报
- **WHEN** 场景数据加载完成
- **THEN** 顶部展示大尺寸封面图（coverUrl），叠加渐变遮罩，显示场景名称和描述

#### Scenario: 商品分区展示
- **WHEN** 场景推荐商品加载完成
- **THEN** 左侧按分类分区展示商品卡片（4列网格），每区有标题和标签

#### Scenario: 右侧购物清单
- **WHEN** 场景页加载完成
- **THEN** 右侧显示粘性定位的购物清单面板，包含进度条、商品勾选列表和一键加购按钮

#### Scenario: 一键加购
- **WHEN** 用户在购物清单中勾选商品并点击一键加购
- **THEN** 逐个调用 `POST /api/v1/carts/items` 将选中商品加入购物车

---

### Requirement: 商品列表页
系统 SHALL 提供商品搜索和筛选功能

#### Scenario: 关键词搜索
- **WHEN** 用户访问 `/products?keyword=手机`
- **THEN** 调用 `GET /api/v1/products/search?keyword=手机` 返回搜索结果

#### Scenario: 分类筛选
- **WHEN** 用户选择某个分类
- **THEN** 调用 `GET /api/v1/products/category/{categoryId}` 返回该分类商品

#### Scenario: 价格排序
- **WHEN** 用户选择价格排序
- **THEN** 调用 `GET /api/v1/products/search?sortField=price&sortOrder=asc` 返回排序结果

#### Scenario: 分页加载
- **WHEN** 用户滚动到底部或点击分页
- **THEN** 调用接口传入 pageNum 和 pageSize 获取下一页数据

---

### Requirement: 商品详情页
系统 SHALL 提供商品详情展示和加入购物车功能

#### Scenario: 商品详情加载
- **WHEN** 用户访问 `/products/:id`
- **THEN** 调用 `GET /api/v1/products/{id}` 获取商品详情，展示轮播图、名称、价格、描述、规格属性

#### Scenario: 加入购物车
- **WHEN** 用户选择 SKU 和数量后点击加入购物车
- **THEN** 调用 `POST /api/v1/carts/items` 添加商品到购物车，显示成功提示

---

### Requirement: 购物车页
系统 SHALL 提供购物车管理功能

#### Scenario: 购物车列表
- **WHEN** 用户访问 `/cart`
- **THEN** 调用 `GET /api/v1/carts/items` 获取购物车列表，展示商品图片、名称、规格、单价、数量、小计

#### Scenario: 修改数量
- **WHEN** 用户点击 +/- 按钮修改商品数量
- **THEN** 调用 `PUT /api/v1/carts/items/{id}` 更新数量，实时刷新小计和总价

#### Scenario: 选中/取消选中
- **WHEN** 用户勾选/取消勾选商品
- **THEN** 调用 `PUT /api/v1/carts/items/{id}/select` 切换选中状态

#### Scenario: 删除商品
- **WHEN** 用户点击删除按钮
- **THEN** 调用 `DELETE /api/v1/carts/items/{id}` 删除该购物车项

#### Scenario: 结算
- **WHEN** 用户点击结算按钮
- **THEN** 跳转到订单确认页 `/checkout`，携带选中的商品信息

---

### Requirement: 订单确认页
系统 SHALL 提供订单确认和提交功能

#### Scenario: 订单确认页加载
- **WHEN** 用户访问 `/checkout`
- **THEN** 展示收货地址选择、商品清单、订单备注输入框、总价和提交按钮

#### Scenario: 地址管理
- **WHEN** 页面加载
- **THEN** 调用 `GET /api/v1/addresses/my` 获取地址列表，默认选中默认地址

#### Scenario: 提交订单
- **WHEN** 用户点击提交订单
- **THEN** 调用 `POST /api/v1/orders` 创建订单，成功后跳转到支付或订单详情

---

### Requirement: 订单列表页
系统 SHALL 提供订单列表和状态筛选功能

#### Scenario: 订单列表加载
- **WHEN** 用户访问 `/orders`
- **THEN** 调用 `GET /api/v1/orders/my` 获取订单列表，按状态 Tab 分类展示

#### Scenario: 状态筛选
- **WHEN** 用户点击状态 Tab（待付款/已付款/已发货/已完成）
- **THEN** 调用 `GET /api/v1/orders/my?status=x` 筛选对应状态订单

#### Scenario: 订单操作
- **WHEN** 用户对订单执行确认收货/取消/删除操作
- **THEN** 调用对应 API 并刷新列表

---

### Requirement: 登录/注册页
系统 SHALL 提供用户登录和注册功能

#### Scenario: 用户登录
- **WHEN** 用户输入用户名和密码点击登录
- **THEN** 调用 `POST /api/v1/users/login`，成功后将 token 存入 localStorage，跳转首页

#### Scenario: 用户注册
- **WHEN** 用户填写注册信息点击注册
- **THEN** 调用 `POST /api/v1/users/register`，成功后跳转登录页

---

### Requirement: 用户中心页
系统 SHALL 提供用户信息管理和地址管理功能

#### Scenario: 用户信息展示
- **WHEN** 用户访问 `/user`
- **THEN** 调用 `GET /api/v1/users/me` 获取并展示用户信息

#### Scenario: 修改用户信息
- **WHEN** 用户编辑昵称/手机/邮箱后保存
- **THEN** 调用 `PUT /api/v1/users/me` 更新用户信息

#### Scenario: 地址管理
- **WHEN** 用户在地址管理区域操作
- **THEN** 调用地址相关 API（增删改查、设置默认地址）

---

### Requirement: 通用组件
系统 SHALL 提供以下通用组件

#### Scenario: 顶部导航栏
- **WHEN** 页面加载
- **THEN** 展示淘宝风格顶部导航：左侧登录/注册链接，右侧我的淘宝/购物车/收藏夹链接，购物车图标旁显示商品数量角标

#### Scenario: 商品卡片组件
- **WHEN** 商品列表渲染
- **THEN** 每个商品以卡片形式展示：商品图片、名称（2行截断）、价格（橙色加粗）、标签（促销/热卖等），hover 时边框变橙+上浮+阴影

#### Scenario: 轮播图组件
- **WHEN** 首页加载
- **THEN** 自动轮播展示 Banner 图片，支持左右箭头和底部圆点切换，鼠标悬停暂停

#### Scenario: 分页器组件
- **WHEN** 列表数据超过一页
- **THEN** 底部展示分页器，支持页码点击和上/下一页

---

## MODIFIED Requirements

### Requirement: 前端技术栈
前端 SHALL 使用 Vue 3 + TypeScript + Vite + Pinia + Vue Router + Tailwind CSS，所有组件使用 `<script setup lang="ts">` 语法

### Requirement: API 调用规范
前端 SHALL 通过统一的 axios 实例调用后端 API，基础 URL 为 `/api/v1`，请求头自动携带 `Authorization: Bearer {token}`，统一处理 401 跳转登录

### Requirement: 数据流规范
前端 SHALL 严格区分数据层次：API 层返回原始数据 → Store 层管理状态 → 组件层消费数据，禁止组件直接调用 axios

## REMOVED Requirements

无移除需求（项目为全新前端构建）
