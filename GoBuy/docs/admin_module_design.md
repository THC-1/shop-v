# 运营管理后台 API 与数据库设计文档

**项目**: GoBuy 商城系统
**版本**: v1.0
**日期**: 2026-04-14
**状态**: 待确认

***

## 一、概述

本文档设计运营管理后台（Admin）的 API 接口和数据库结构，包含以下模块：

| 模块    | 功能               |
| ----- | ---------------- |
| 管理员登录 | 管理员认证、Session 管理 |
| 权限管理  | 角色定义、权限分配        |
| 商品管理  | 商品 CRUD、上下架、批量操作 |
| 分类管理  | 树形分类 CRUD        |
| 品牌管理  | 品牌 CRUD          |
| 订单管理  | 订单查询、发货操作        |
| 用户管理  | 用户查询、封禁、角色分配     |
| 数据看板  | 销售统计、UV/PV 统计    |

***

## 二、API 接口设计

### 2.1 设计规范

必须遵照RESTful标准，严禁使用动词

- **基础路径**: `/api/v1/admin`
- **认证方式**: JWT Token（与现有系统一致）
- **通用请求头**: `Authorization: Bearer {token}`
- **分页规范**: 使用 `page` + `size` 参数，返回 `total` + `list`

### 2.2 通用响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

### 2.3 模块接口详情

#### 2.3.1 管理员登录模块

| 方法     | 路径        | 功能    | 说明      |
| ------ | --------- | ----- | ------- |
| POST   | /sessions | 管理员登录 | 创建管理员会话 |
| DELETE | /sessions | 管理员登出 | 删除会话    |

**POST /api/v1/admin/sessions - 管理员登录**

Request:

```json
{
  "username": "admin",
  "password": "xxxxxx"
}
```

Response:

```json
{
  "code": 200,
  "data": {
    "token": "eyJhbG...",
    "adminId": 1,
    "username": "admin",
    "roles": ["SUPER_ADMIN"]
  }
}
```

**DELETE /api/v1/admin/sessions - 管理员登出**

Response:

```json
{
  "code": 200,
  "message": "登出成功"
}
```

***

#### 2.3.2 权限管理模块

| 方法     | 路径                          | 功能     | 说明      |
| ------ | --------------------------- | ------ | ------- |
| GET    | /roles                      | 获取角色列表 | 分页查询    |
| POST   | /roles                      | 创建角色   | -       |
| GET    | /roles/{id}                 | 获取角色详情 | 包含权限列表  |
| PUT    | /roles/{id}                 | 更新角色   | -       |
| DELETE | /roles/{id}                 | 删除角色   | -       |
| GET    | /permissions                | 获取权限列表 | 树形结构    |
| POST   | /roles/{roleId}/permissions | 分配权限   | 绑定权限到角色 |

**GET /api/v1/admin/roles - 获取角色列表**

Query: `page`, `size`, `name`（可选）

Response:

```json
{
  "code": 200,
  "data": {
    "total": 5,
    "list": [
      { "id": 1, "name": "SUPER_ADMIN", "description": "超级管理员", "userCount": 2 },
      { "id": 2, "name": "PRODUCT_ADMIN", "description": "商品管理员", "userCount": 5 }
    ]
  }
}
```

**POST /api/v1/admin/roles - 创建角色**

Request:

```json
{
  "name": "ORDER_ADMIN",
  "description": "订单管理员",
  "permissionIds": [101, 102, 103]
}
```

**GET /api/v1/admin/permissions - 获取权限列表（树形）**

Response:

```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "商品管理",
      "code": "PRODUCT",
      "children": [
        { "id": 101, "name": "商品查看", "code": "PRODUCT:VIEW" },
        { "id": 102, "name": "商品编辑", "code": "PRODUCT:EDIT" }
      ]
    }
  ]
}
```

***

#### 2.3.3 商品管理模块

| 方法     | 路径                     | 功能     | 说明      |
| ------ | ---------------------- | ------ | ------- |
| GET    | /products              | 获取商品列表 | 分页、筛选   |
| GET    | /products/{id}         | 获取商品详情 | -       |
| POST   | /products              | 创建商品   | -       |
| PUT    | /products/{id}         | 更新商品   | -       |
| DELETE | /products/{id}         | 删除商品   | 软删除     |
| PATCH  | /products/{id}/status  | 批量更新状态 | 上架/下架   |
| POST   | /products/batch/status | 批量更新状态 | 批量上架/下架 |

**GET /api/v1/admin/products - 获取商品列表**

Query: `page`, `size`, `categoryId`, `brandId`, `status`, `name`

| 参数     | 类型     | 说明                   |
| ------ | ------ | -------------------- |
| status | string | ON\_SALE / OFF\_SALE |

Response:

```json
{
  "code": 200,
  "data": {
    "total": 100,
    "list": [
      {
        "id": 1,
        "name": "iPhone 15 Pro",
        "categoryName": "手机",
        "brandName": "Apple",
        "price": 8999.00,
        "stock": 50,
        "status": "ON_SALE",
        "createdAt": "2026-01-01 10:00:00"
      }
    ]
  }
}
```

**PATCH /api/v1/admin/products/{id}/status - 更新商品状态**

Request:

```json
{
  "status": "OFF_SALE"
}
```

**POST /api/v1/admin/products/batch/status - 批量更新商品状态**

Request:

```json
{
  "productIds": [1, 2, 3],
  "status": "ON_SALE"
}
```

***

#### 2.3.4 分类管理模块

| 方法     | 路径               | 功能     | 说明      |
| ------ | ---------------- | ------ | ------- |
| GET    | /categories      | 获取分类列表 | 树形结构    |
| GET    | /categories/{id} | 获取分类详情 | -       |
| POST   | /categories      | 创建分类   | -       |
| PUT    | /categories/{id} | 更新分类   | -       |
| DELETE | /categories/{id} | 删除分类   | 需确认无子分类 |

**GET /api/v1/admin/categories - 获取分类列表（树形）**

Response:

```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "手机",
      "parentId": null,
      "sort": 1,
      "icon": "https://xxx/phone.png",
      "children": [
        { "id": 11, "name": "智能手机", "parentId": 1, "sort": 1 }
      ]
    }
  ]
}
```

***

#### 2.3.5 品牌管理模块

| 方法     | 路径           | 功能     | 说明       |
| ------ | ------------ | ------ | -------- |
| GET    | /brands      | 获取品牌列表 | 分页       |
| GET    | /brands/{id} | 获取品牌详情 | -        |
| POST   | /brands      | 创建品牌   | -        |
| PUT    | /brands/{id} | 更新品牌   | -        |
| DELETE | /brands/{id} | 删除品牌   | 需确认无关联商品 |

***

#### 2.3.6 订单管理模块

| 方法    | 路径                | 功能     | 说明     |
| ----- | ----------------- | ------ | ------ |
| GET   | /orders           | 获取订单列表 | 分页、筛选  |
| GET   | /orders/{id}      | 获取订单详情 | 包含订单商品 |
| PATCH | /orders/{id}/ship | 订单发货   | 更新物流信息 |

**GET /api/v1/admin/orders - 获取订单列表**

Query: `page`, `size`, `status`, `orderNo`, `userId`, `startDate`, `endDate`

| 参数     | 类型     | 说明                                                                    |
| ------ | ------ | --------------------------------------------------------------------- |
| status | string | PENDING\_PAYMENT / PAID / SHIPPED / DELIVERED / COMPLETED / CANCELLED |

Response:

```json
{
  "code": 200,
  "data": {
    "total": 50,
    "list": [
      {
        "id": 1,
        "orderNo": "ORD202604140001",
        "userId": 100,
        "totalAmount": 8999.00,
        "status": "PAID",
        "createdAt": "2026-04-14 10:00:00"
      }
    ]
  }
}
```

**PATCH /api/v1/admin/orders/{id}/ship - 订单发货**

Request:

```json
{
  "expressCompany": "顺丰速运",
  "expressNo": "SF1234567890"
}
```

***

#### 2.3.7 用户管理模块

| 方法    | 路径                 | 功能     | 说明    |
| ----- | ------------------ | ------ | ----- |
| GET   | /users             | 获取用户列表 | 分页、筛选 |
| GET   | /users/{id}        | 获取用户详情 | 包含角色  |
| PATCH | /users/{id}/status | 更新用户状态 | 封禁/解封 |
| POST  | /users/{id}/roles  | 分配用户角色 | -     |

**PATCH /api/v1/admin/users/{id}/status - 更新用户状态**

Request:

```json
{
  "status": "DISABLED"
}
```

| status 值 | 说明 |
| -------- | -- |
| ACTIVE   | 正常 |
| DISABLED | 封禁 |

**POST /api/v1/admin/users/{id}/roles - 分配用户角色**

Request:

```json
{
  "roleIds": [2, 3]
}
```

***

#### 2.3.8 数据看板模块

| 方法  | 路径                       | 功能       | 说明    |
| --- | ------------------------ | -------- | ----- |
| GET | /dashboard/sales-summary | 销售汇总     | 按时段统计 |
| GET | /dashboard/sales-trend   | 销售趋势     | 图表数据  |
| GET | /dashboard/top-products  | 热销商品     | TOP N |
| GET | /dashboard/uv-pv         | UV/PV 统计 | -     |

**GET /api/v1/admin/dashboard/sales-summary - 销售汇总**

Query: `startDate`, `endDate`

Response:

```json
{
  "code": 200,
  "data": {
    "totalOrders": 1000,
    "totalAmount": 500000.00,
    "totalUsers": 500,
    "avgOrderAmount": 500.00
  }
}
```

**GET /api/v1/admin/dashboard/sales-trend - 销售趋势**

Query: `startDate`, `endDate`, `type` (day/week/month)

Response:

```json
{
  "code": 200,
  "data": [
    { "date": "2026-04-01", "orders": 100, "amount": 50000.00 },
    { "date": "2026-04-02", "orders": 120, "amount": 60000.00 }
  ]
}
```

**GET /api/v1/admin/dashboard/uv-pv - UV/PV 统计**

Query: `startDate`, `endDate`

Response:

```json
{
  "code": 200,
  "data": {
    "totalUv": 10000,
    "totalPv": 50000,
    "avgPvPerUv": 5.0,
    "dailyList": [
      { "date": "2026-04-14", "uv": 1000, "pv": 5000 }
    ]
  }
}
```

***

## 三、数据库设计

### 3.1 新增表清单

| 序号 | 表名                     | 说明             |
| -- | ---------------------- | -------------- |
| 1  | admins                 | 管理员表           |
| 2  | admin\_roles           | 管理员-角色关联表      |
| 3  | roles                  | 角色表            |
| 4  | permissions            | 权限表            |
| 5  | role\_permissions      | 角色-权限关联表       |
| 6  | admin\_login\_logs     | 管理员登录日志表       |
| 7  | admin\_operation\_logs | 管理员操作日志表       |
| 8  | product\_stats         | 商品访问统计表（UV/PV） |
| 9  | daily\_stats           | 每日汇总统计表        |

### 3.2 表结构详情

#### 3.2.1 管理员表 (admins)

```sql
CREATE TABLE `admins` (
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
    `username`        VARCHAR(50)  NOT NULL UNIQUE COMMENT '管理员用户名',
    `password`        VARCHAR(255) NOT NULL COMMENT '登录密码(BCrypt)',
    `nickname`        VARCHAR(50) COMMENT '昵称',
    `email`           VARCHAR(100) COMMENT '邮箱',
    `phone`           VARCHAR(20) COMMENT '手机号',
    `avatar`          VARCHAR(255) COMMENT '头像',
    `status`          VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-正常 DISABLED-禁用',
    `last_login_at`   DATETIME COMMENT '最后登录时间',
    `last_login_ip`   VARCHAR(50) COMMENT '最后登录IP',
    `created_at`      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by`      BIGINT COMMENT '创建人ID',
    INDEX `idx_username` (`username`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';
```

#### 3.2.2 角色表 (roles)

```sql
CREATE TABLE `roles` (
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    `name`        VARCHAR(50)  NOT NULL UNIQUE COMMENT '角色名称',
    `code`        VARCHAR(50)  NOT NULL UNIQUE COMMENT '角色代码',
    `description` VARCHAR(255) COMMENT '角色描述',
    `sort`        INT DEFAULT 0 COMMENT '排序权重',
    `status`      VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-正常 DISABLED-禁用',
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_name` (`name`),
    INDEX `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
```

#### 3.2.3 权限表 (permissions)

```sql
CREATE TABLE `permissions` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    `name`       VARCHAR(100) NOT NULL COMMENT '权限名称',
    `code`       VARCHAR(100) NOT NULL UNIQUE COMMENT '权限代码',
    `type`       VARCHAR(20) DEFAULT 'MENU' COMMENT '类型：MENU-菜单 BUTTON-按钮',
    `parent_id`  BIGINT DEFAULT NULL COMMENT '父权限ID',
    `path`       VARCHAR(255) COMMENT '路由路径',
    `icon`       VARCHAR(100) COMMENT '图标',
    `sort`       INT DEFAULT 0 COMMENT '排序权重',
    `status`     VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-正常 DISABLED-禁用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';
```

#### 3.2.4 管理员-角色关联表 (admin\_roles)

```sql
CREATE TABLE `admin_roles` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `admin_id`   BIGINT NOT NULL COMMENT '管理员ID',
    `role_id`    BIGINT NOT NULL COMMENT '角色ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE INDEX `uk_admin_role` (`admin_id`, `role_id`),
    INDEX `idx_admin_id` (`admin_id`),
    INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员-角色关联表';
```

#### 3.2.5 角色-权限关联表 (role\_permissions)

```sql
CREATE TABLE `role_permissions` (
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `role_id`       BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `created_at`    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE INDEX `uk_role_permission` (`role_id`, `permission_id`),
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-权限关联表';
```

#### 3.2.6 管理员登录日志表 (admin\_login\_logs)

```sql
CREATE TABLE `admin_login_logs` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `admin_id`   BIGINT COMMENT '管理员ID',
    `username`   VARCHAR(50) COMMENT '用户名',
    `ip`         VARCHAR(50) COMMENT '登录IP',
    `user_agent` VARCHAR(500) COMMENT '浏览器User-Agent',
    `status`     VARCHAR(20) COMMENT '登录状态：SUCCESS-成功 FAILED-失败',
    `fail_reason` VARCHAR(255) COMMENT '失败原因',
    `login_at`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    INDEX `idx_admin_id` (`admin_id`),
    INDEX `idx_login_at` (`login_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员登录日志表';
```

#### 3.2.7 管理员操作日志表 (admin\_operation\_logs)

```sql
CREATE TABLE `admin_operation_logs` (
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `admin_id`      BIGINT NOT NULL COMMENT '管理员ID',
    `admin_username` VARCHAR(50) COMMENT '管理员用户名',
    `module`        VARCHAR(50) COMMENT '操作模块',
    `action`        VARCHAR(50) COMMENT '操作动作',
    `target_type`   VARCHAR(50) COMMENT '操作对象类型',
    `target_id`     BIGINT COMMENT '操作对象ID',
    `detail`        JSON COMMENT '操作详情',
    `ip`            VARCHAR(50) COMMENT '操作IP',
    `created_at`    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX `idx_admin_id` (`admin_id`),
    INDEX `idx_module` (`module`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员操作日志表';
```

#### 3.2.8 商品访问统计表 (product\_stats)

```sql
CREATE TABLE `product_stats` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `stat_date`  DATE NOT NULL COMMENT '统计日期',
    `uv`         INT DEFAULT 0 COMMENT '独立访客数',
    `pv`         INT DEFAULT 0 COMMENT '页面浏览量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX `uk_product_date` (`product_id`, `stat_date`),
    INDEX `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品访问统计表';
```

#### 3.2.9 每日统计汇总表 (daily\_stats)

```sql
CREATE TABLE `daily_stats` (
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `stat_date`    DATE NOT NULL UNIQUE COMMENT '统计日期',
    `new_users`    INT DEFAULT 0 COMMENT '新增用户数',
    `new_orders`   INT DEFAULT 0 COMMENT '新增订单数',
    `order_amount` DECIMAL(12, 2) DEFAULT 0 COMMENT '订单总金额',
    `total_uv`     INT DEFAULT 0 COMMENT '总UV',
    `total_pv`     INT DEFAULT 0 COMMENT '总PV',
    `created_at`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日统计汇总表';
```

### 3.3 修改现有表

#### 3.3.1 商品表 (products) - 新增字段

```sql
ALTER TABLE `products` ADD COLUMN `status` VARCHAR(20) DEFAULT 'ON_SALE' COMMENT '商品状态：ON_SALE-上架 OFF_SALE-下架';
ALTER TABLE `products` ADD COLUMN `sales_count` INT DEFAULT 0 COMMENT '销量';
ALTER TABLE `products` ADD COLUMN `deleted` TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除';
ALTER TABLE `products` ADD INDEX `idx_status` (`status`);
```

#### 3.3.2 用户表 (users) - 新增字段

```sql
ALTER TABLE `users` ADD COLUMN `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '用户状态：ACTIVE-正常 DISABLED-封禁';
```

***

## 四、权限树结构（初始数据）

```
├── 系统管理
│   ├── 管理员管理 (ADMIN:VIEW, ADMIN:EDIT)
│   ├── 角色管理 (ROLE:VIEW, ROLE:EDIT)
│   ├── 权限管理 (PERMISSION:VIEW, PERMISSION:EDIT)
│   └── 操作日志 (LOG:VIEW)
├── 商品管理
│   ├── 商品列表 (PRODUCT:VIEW)
│   ├── 商品编辑 (PRODUCT:EDIT)
│   ├── 分类管理 (CATEGORY:VIEW, CATEGORY:EDIT)
│   └── 品牌管理 (BRAND:VIEW, BRAND:EDIT)
├── 订单管理
│   ├── 订单列表 (ORDER:VIEW)
│   └── 订单发货 (ORDER:SHIP)
├── 用户管理
│   ├── 用户列表 (USER:VIEW)
│   ├── 用户禁用 (USER:DISABLE)
│   └── 角色分配 (USER:ROLE)
└── 数据看板
    ├── 销售统计 (DASHBOARD:SALES)
    └── UV/PV统计 (DASHBOARD:UVPV)
```

***

## 五、接口依赖分析

### 5.1 与现有模块的冲突检测

| 现有接口                  | 冲突风险 | 解决方案                              |
| --------------------- | ---- | --------------------------------- |
| POST /api/v1/sessions | 无冲突  | Admin 模块使用 /api/v1/admin/sessions |
| GET /api/v1/products  | 无冲突  | Admin 模块使用 /api/v1/admin/products |

### 5.2 JWT Token 扩展

现有 JWT Token 只包含 `userId` 和 `username`，Admin 模块需要新增 `adminId` 和 `roles`。

**建议方案**：

1. 在 JWT Payload 中增加 `adminId` 字段（通过数值范围区分用户/管理员）
2. 或使用独立的 Admin Token

***

## 六、待确认问题

### 6.1 认证相关

| 序号 | 问题                      | 用户回答                          |
| -- | ----------------------- | ----------------------------- |
| 1  | 管理员和普通用户是否共用同一套 JWT 体系？ | 共用 JWT 结构，但区分 Payload 标识（更优选） |
| 2  | 管理员登录失败是否需要记录日志？        | 必须记录                          |
| 3  | 管理员 session 过期时间？       | 2小时                           |

### 6.2 权限相关

| 序号 | 问题               | 用户回答                                                |
| -- | ---------------- | --------------------------------------------------- |
| 4  | 权限是否需要精确到按钮级别？   | 需要                                                  |
| 5  | 超级管理员角色是否内置不可删除？ | 是                                                   |
| 6  | 角色删除时是否检查关联管理员？  | 检查并阻止，更稳妥的做法是强校验，提示前端：“该角色下存在关联的用户，请先移除这些用户后再尝试删除”。 |

### 6.3 商品管理

| 序号 | 问题             | 用户回答 |
| -- | -------------- | ---- |
| 7  | 批量操作的最大数量限制？   | 100  |
| 8  | 商品删除是软删除还是硬删除？ | 软删除  |
| 9  | 商品下架后是否保留已有订单？ | 正常保留 |

### 6.4 分类管理

| 序号 | 问题               | 用户回答                                                                           |
| -- | ---------------- | ------------------------------------------------------------------------------ |
| 10 | 删除分类时是否有子分类如何处理？ | 与角色删除同理，级联删除的风险极高。不仅要检查是否有子分类，还要检查该分类下**是否还有绑定的商品**。只有当分类下既没有子分类，也没有商品时，才允许删除。 |
| 11 | 分类是否支持多级嵌套？最大层级？ | 3级                                                                             |

### 6.5 数据统计

| 序号 | 问题                | 用户回答                |
| -- | ----------------- | ------------------- |
| 12 | UV/PV 统计是否需要实时更新？ | 实时查询，但可以加上Redis进行缓冲 |
| 13 | 销售统计的数据来源？        | 实时查询，但可以加上Redis进行缓冲 |

### 6.6 其他

| 序号 | 问题                   | 用户回答                              |
| -- | -------------------- | --------------------------------- |
| 14 | 是否需要操作日志记录所有变更？      | 核心操作记录                            |
| 15 | 订单发货是否需要物流公司基础数据？    | 订单发货功能暂时做一个较简单的，后台填写一个运单号，返回给用户即可 |
| 16 | 是否需要导出功能（Excel/CSV）？ | 暂不需要                              |

***

## 七、实现优先级建议

### 第一阶段（核心功能）

1. 管理员登录 / 登出
2. 角色管理（CRUD + 权限分配）
3. 商品管理（CRUD + 上下架）
4. 订单管理（查询 + 发货）

### 第二阶段（完善功能）

1. 分类管理（树形 CRUD）
2. 品牌管理（CRUD）
3. 用户管理（查询 + 封禁）
4. 数据看板（基础统计）

### 第三阶段（高级功能）

1. 操作日志
2. 登录日志
3. 高级统计
4. 导出功能

***

**文档版本**: v1.0
**待审阅人**: 开发团队 / 产品团队
**预计完成**: 待确认需求后评估
