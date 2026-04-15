# 运营管理后台 API 接口文档

**项目**: GoBuy 商城系统
**版本**: v1.0
**日期**: 2026-04-14
**基础路径**: `/api/v1/admin`

---

## 一、接口概述

### 1.1 认证方式

- **认证方式**: JWT Token
- **请求头**: `Authorization: Bearer {token}`
- **Token 标识**: JWT Payload 中通过 `adminId` 字段标识管理员身份（与普通用户 `userId` 区分）

### 1.2 通用请求规范

| 规范 | 说明 |
|------|------|
| 基础路径 | `/api/v1/admin` |
| 分页参数 | `page`（页码，从 1 开始）、`size`（每页数量） |
| 日期格式 | ISO 8601：`YYYY-MM-DD` |
| 时间格式 | 24 小时制：`YYYY-MM-DD HH:mm:ss` |

### 1.3 通用响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（未登录或 Token 失效） |
| 403 | 禁止访问（无权限） |
| 404 | 资源不存在 |
| 409 | 资源冲突（如用户名已存在） |
| 422 | 业务校验失败（如角色下有关联用户） |
| 500 | 服务器内部错误 |

### 1.4 分页响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "list": [ ... ]
  }
}
```

---

## 二、认证模块

### 2.1 管理员登录

**POST** `/api/v1/admin/sessions`

创建管理员会话，返回 JWT Token。

**Request:**

```json
{
  "username": "admin",
  "password": "admin123"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | string | 是 | 管理员用户名 |
| password | string | 是 | 登录密码 |

**Response:**

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "adminId": 1,
    "username": "admin",
    "nickname": "超级管理员",
    "roles": ["SUPER_ADMIN"]
  }
}
```

**失败响应:**

```json
{
  "code": 401,
  "message": "用户名或密码错误"
}
```

---

### 2.2 管理员登出

**DELETE** `/api/v1/admin/sessions`

删除当前管理员会话。

**Request Header:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "登出成功"
}
```

---

## 三、角色管理模块

### 3.1 获取角色列表

**GET** `/api/v1/admin/roles`

分页获取角色列表。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 1 |
| size | int | 否 | 每页数量，默认 10 |
| name | string | 否 | 角色名称（模糊匹配） |
| status | string | 否 | 状态：ACTIVE / DISABLED |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 3,
    "list": [
      {
        "id": 1,
        "name": "超级管理员",
        "code": "SUPER_ADMIN",
        "description": "系统内置超级管理员，拥有所有权限",
        "isSystem": true,
        "status": "ACTIVE",
        "userCount": 1,
        "createdAt": "2026-04-14 10:00:00"
      },
      {
        "id": 2,
        "name": "商品管理员",
        "code": "PRODUCT_ADMIN",
        "description": "负责商品上下架和编辑",
        "isSystem": false,
        "status": "ACTIVE",
        "userCount": 2,
        "createdAt": "2026-04-14 11:00:00"
      }
    ]
  }
}
```

---

### 3.2 创建角色

**POST** `/api/v1/admin/roles`

创建新角色。

**Request:**

```json
{
  "name": "订单管理员",
  "code": "ORDER_ADMIN",
  "description": "负责订单发货和查询",
  "sort": 1,
  "permissionIds": [16, 17]
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 角色名称 |
| code | string | 是 | 角色代码（唯一） |
| description | string | 否 | 角色描述 |
| sort | int | 否 | 排序权重，默认 0 |
| permissionIds | array | 否 | 权限 ID 列表 |

**Response:**

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 3,
    "name": "订单管理员",
    "code": "ORDER_ADMIN"
  }
}
```

---

### 3.3 获取角色详情

**GET** `/api/v1/admin/roles/{id}`

获取指定角色的详细信息，包含关联的权限列表。

**Path Parameters:**

| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 角色 ID |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "超级管理员",
    "code": "SUPER_ADMIN",
    "description": "系统内置超级管理员，拥有所有权限",
    "isSystem": true,
    "status": "ACTIVE",
    "permissions": [
      { "id": 1, "name": "系统管理", "code": "SYSTEM" },
      { "id": 2, "name": "管理员管理", "code": "ADMIN" }
    ],
    "createdAt": "2026-04-14 10:00:00",
    "updatedAt": "2026-04-14 10:00:00"
  }
}
```

---

### 3.4 更新角色

**PUT** `/api/v1/admin/roles/{id}`

更新指定角色的信息。

**Path Parameters:**

| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 角色 ID |

**Request:**

```json
{
  "name": "订单管理员（更新）",
  "description": "负责订单相关操作",
  "sort": 2,
  "status": "ACTIVE",
  "permissionIds": [16, 17, 18]
}
```

**Response:**

```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 3.5 删除角色

**DELETE** `/api/v1/admin/roles/{id}`

删除指定角色。若角色下存在关联管理员，则返回错误。

**Path Parameters:**

| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 角色 ID |

**Response:**

```json
{
  "code": 200,
  "message": "删除成功"
}
```

**删除失败响应（存在关联用户）:**

```json
{
  "code": 422,
  "message": "该角色下存在关联的管理员，请先移除这些用户后再尝试删除"
}
```

---

### 3.6 获取权限列表

**GET** `/api/v1/admin/permissions`

获取权限树形结构列表。

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "code": "SYSTEM",
      "type": "MENU",
      "parentId": null,
      "path": "/system",
      "icon": "setting",
      "sort": 0,
      "children": [
        {
          "id": 5,
          "name": "管理员管理",
          "code": "ADMIN",
          "type": "MENU",
          "parentId": 1,
          "path": "/system/admin",
          "icon": "user",
          "sort": 1,
          "children": [
            { "id": 9, "name": "管理员查看", "code": "ADMIN:VIEW", "type": "BUTTON", "parentId": 5, "sort": 1 },
            { "id": 10, "name": "管理员编辑", "code": "ADMIN:EDIT", "type": "BUTTON", "parentId": 5, "sort": 2 }
          ]
        }
      ]
    }
  ]
}
```

---

### 3.7 分配权限

**POST** `/api/v1/admin/roles/{roleId}/permissions`

为指定角色分配权限。

**Path Parameters:**

| 参数 | 类型 | 说明 |
|------|------|------|
| roleId | long | 角色 ID |

**Request:**

```json
{
  "permissionIds": [1, 2, 3, 9, 10]
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| permissionIds | array | 是 | 权限 ID 列表（会替换原有的权限） |

**Response:**

```json
{
  "code": 200,
  "message": "分配成功"
}
```

---

## 四、管理员管理模块

### 4.1 获取管理员列表

**GET** `/api/v1/admin/admins`

分页获取管理员列表。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 1 |
| size | int | 否 | 每页数量，默认 10 |
| username | string | 否 | 用户名（模糊匹配） |
| status | string | 否 | 状态：ACTIVE / DISABLED |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 1,
    "list": [
      {
        "id": 1,
        "username": "admin",
        "nickname": "超级管理员",
        "email": "admin@example.com",
        "phone": "13800138000",
        "status": "ACTIVE",
        "roles": ["超级管理员"],
        "lastLoginAt": "2026-04-14 10:00:00",
        "createdAt": "2026-04-14 10:00:00"
      }
    ]
  }
}
```

---

### 4.2 创建管理员

**POST** `/api/v1/admin/admins`

创建新管理员。

**Request:**

```json
{
  "username": "product_admin",
  "password": "xxxxxx",
  "nickname": "商品管理员",
  "email": "product@example.com",
  "phone": "13800138001",
  "roleIds": [2]
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | string | 是 | 用户名（唯一） |
| password | string | 是 | 密码 |
| nickname | string | 否 | 昵称 |
| email | string | 否 | 邮箱 |
| phone | string | 否 | 手机号 |
| roleIds | array | 是 | 角色 ID 列表 |

**Response:**

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 2,
    "username": "product_admin"
  }
}
```

---

### 4.3 获取管理员详情

**GET** `/api/v1/admin/admins/{id}`

获取指定管理员的详细信息。

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "超级管理员",
    "email": "admin@example.com",
    "phone": "13800138000",
    "status": "ACTIVE",
    "roles": [
      { "id": 1, "name": "超级管理员", "code": "SUPER_ADMIN" }
    ],
    "lastLoginAt": "2026-04-14 10:00:00",
    "lastLoginIp": "127.0.0.1",
    "createdAt": "2026-04-14 10:00:00"
  }
}
```

---

### 4.4 更新管理员

**PUT** `/api/v1/admin/admins/{id}`

更新指定管理员的信息。

**Request:**

```json
{
  "nickname": "超级管理员（更新）",
  "email": "admin_new@example.com",
  "phone": "13800138999",
  "status": "ACTIVE",
  "roleIds": [1]
}
```

**Response:**

```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 4.5 删除管理员

**DELETE** `/api/v1/admin/admins/{id}`

删除指定管理员（软删除）。

**Response:**

```json
{
  "code": 200,
  "message": "删除成功"
}
```

---

## 五、商品管理模块

### 5.1 获取商品列表

**GET** `/api/v1/admin/products`

分页获取商品列表。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 1 |
| size | int | 否 | 每页数量，默认 10 |
| name | string | 否 | 商品名称（模糊匹配） |
| categoryId | long | 否 | 分类 ID |
| brandId | long | 否 | 品牌 ID |
| status | string | 否 | 状态：ON_SALE / OFF_SALE |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "list": [
      {
        "id": 1,
        "name": "iPhone 15 Pro",
        "categoryName": "手机",
        "brandName": "Apple",
        "price": 8999.00,
        "originalPrice": 9999.00,
        "stock": 50,
        "salesCount": 120,
        "status": "ON_SALE",
        "createdAt": "2026-01-01 10:00:00"
      }
    ]
  }
}
```

---

### 5.2 获取商品详情

**GET** `/api/v1/admin/products/{id}`

获取指定商品的详细信息。

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro",
    "description": "商品详细描述...",
    "images": ["https://xxx/1.jpg", "https://xxx/2.jpg"],
    "attributes": {"颜色": "深空黑色", "内存": "256GB"},
    "originalPrice": 9999.00,
    "price": 8999.00,
    "stock": 50,
    "salesCount": 120,
    "categoryId": 1,
    "categoryName": "手机",
    "brandId": 1,
    "brandName": "Apple",
    "status": "ON_SALE",
    "createdAt": "2026-01-01 10:00:00",
    "updatedAt": "2026-04-14 10:00:00"
  }
}
```

---

### 5.3 创建商品

**POST** `/api/v1/admin/products`

创建新商品。

**Request:**

```json
{
  "name": "iPhone 15 Pro",
  "description": "商品详细描述",
  "images": ["https://xxx/1.jpg", "https://xxx/2.jpg"],
  "attributes": {"颜色": "深空黑色", "内存": "256GB"},
  "originalPrice": 9999.00,
  "price": 8999.00,
  "stock": 100,
  "categoryId": 1,
  "brandId": 1
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 商品名称 |
| price | decimal | 是 | 售价 |
| categoryId | long | 是 | 分类 ID |
| brandId | long | 否 | 品牌 ID |
| description | string | 否 | 详细描述 |
| images | array | 否 | 轮播图列表 |
| attributes | object | 否 | 规格属性 |
| originalPrice | decimal | 否 | 原价 |
| stock | int | 否 | 库存，默认 0 |

**Response:**

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 100
  }
}
```

---

### 5.4 更新商品

**PUT** `/api/v1/admin/products/{id}`

更新指定商品的信息。

**Request:**

```json
{
  "name": "iPhone 15 Pro（更新）",
  "description": "更新后的描述",
  "price": 8799.00,
  "stock": 80,
  "categoryId": 1,
  "brandId": 1
}
```

**Response:**

```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 5.5 删除商品

**DELETE** `/api/v1/admin/products/{id}`

删除指定商品（软删除）。

**Response:**

```json
{
  "code": 200,
  "message": "删除成功"
}
```

---

### 5.6 更新商品状态

**PATCH** `/api/v1/admin/products/{id}/status`

更新单个商品的上下架状态。

**Request:**

```json
{
  "status": "OFF_SALE"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | string | 是 | ON_SALE（上架）/ OFF_SALE（下架） |

**Response:**

```json
{
  "code": 200,
  "message": "状态更新成功"
}
```

---

### 5.7 批量更新商品状态

**POST** `/api/v1/admin/products/batch/status`

批量更新商品状态。最多支持 100 个商品。

**Request:**

```json
{
  "productIds": [1, 2, 3],
  "status": "ON_SALE"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| productIds | array | 是 | 商品 ID 列表（最多 100 个） |
| status | string | 是 | ON_SALE / OFF_SALE |

**Response:**

```json
{
  "code": 200,
  "message": "批量更新成功",
  "data": {
    "successCount": 3,
    "failCount": 0
  }
}
```

**部分失败响应:**

```json
{
  "code": 200,
  "message": "批量更新完成，部分失败",
  "data": {
    "successCount": 98,
    "failCount": 2,
    "failIds": [5, 10]
  }
}
```

---

## 六、分类管理模块

### 6.1 获取分类列表

**GET** `/api/v1/admin/categories`

获取分类树形结构列表（支持 3 级嵌套）。

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "手机",
      "parentId": null,
      "sort": 1,
      "icon": "https://xxx/phone.png",
      "children": [
        {
          "id": 11,
          "name": "智能手机",
          "parentId": 1,
          "sort": 1,
          "icon": null,
          "children": [
            {
              "id": 111,
              "name": "5G 手机",
              "parentId": 11,
              "sort": 1,
              "icon": null,
              "children": []
            }
          ]
        }
      ]
    }
  ]
}
```

---

### 6.2 获取分类详情

**GET** `/api/v1/admin/categories/{id}`

获取指定分类的详细信息。

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "手机",
    "parentId": null,
    "sort": 1,
    "icon": "https://xxx/phone.png",
    "level": 1,
    "childrenCount": 3,
    "productCount": 50,
    "createdAt": "2026-01-01 10:00:00"
  }
}
```

---

### 6.3 创建分类

**POST** `/api/v1/admin/categories`

创建新分类。

**Request:**

```json
{
  "name": "平板电脑",
  "parentId": null,
  "sort": 2,
  "icon": "https://xxx/pad.png"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 分类名称 |
| parentId | long | 否 | 父分类 ID（顶级为 null） |
| sort | int | 否 | 排序权重，默认 0 |
| icon | string | 否 | 分类图标 |

**业务校验:**
- 若 `parentId` 不为 null，必须确保父分类存在
- 分类最大支持 3 级嵌套

**Response:**

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 5
  }
}
```

---

### 6.4 更新分类

**PUT** `/api/v1/admin/categories/{id}`

更新指定分类的信息。

**Request:**

```json
{
  "name": "手机（更新）",
  "parentId": null,
  "sort": 1,
  "icon": "https://xxx/phone_new.png"
}
```

**Response:**

```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 6.5 删除分类

**DELETE** `/api/v1/admin/categories/{id}`

删除指定分类。

**业务校验:**
- 分类下存在子分类时，禁止删除
- 分类下存在商品时，禁止删除

**成功响应:**

```json
{
  "code": 200,
  "message": "删除成功"
}
```

**删除失败响应（存在子分类）:**

```json
{
  "code": 422,
  "message": "该分类下存在子分类，请先删除子分类后再尝试删除"
}
```

**删除失败响应（存在商品）:**

```json
{
  "code": 422,
  "message": "该分类下存在关联商品，请先移除这些商品后再尝试删除"
}
```

---

## 七、品牌管理模块

### 7.1 获取品牌列表

**GET** `/api/v1/admin/brands`

分页获取品牌列表。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 1 |
| size | int | 否 | 每页数量，默认 10 |
| name | string | 否 | 品牌名称（模糊匹配） |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 20,
    "list": [
      {
        "id": 1,
        "name": "Apple",
        "logo": "https://xxx/apple.png",
        "description": "美国苹果公司",
        "productCount": 15,
        "createdAt": "2026-01-01 10:00:00"
      }
    ]
  }
}
```

---

### 7.2 获取品牌详情

**GET** `/api/v1/admin/brands/{id}`

获取指定品牌的详细信息。

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "Apple",
    "logo": "https://xxx/apple.png",
    "description": "美国苹果公司",
    "productCount": 15,
    "createdAt": "2026-01-01 10:00:00",
    "updatedAt": "2026-04-14 10:00:00"
  }
}
```

---

### 7.3 创建品牌

**POST** `/api/v1/admin/brands`

创建新品牌。

**Request:**

```json
{
  "name": "Samsung",
  "logo": "https://xxx/samsung.png",
  "description": "韩国三星集团"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 品牌名称（唯一） |
| logo | string | 否 | 品牌 Logo 链接 |
| description | string | 否 | 品牌描述 |

**Response:**

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 2
  }
}
```

---

### 7.4 更新品牌

**PUT** `/api/v1/admin/brands/{id}`

更新指定品牌的信息。

**Request:**

```json
{
  "name": "Samsung（更新）",
  "logo": "https://xxx/samsung_new.png",
  "description": "韩国三星集团（更新）"
}
```

**Response:**

```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 7.5 删除品牌

**DELETE** `/api/v1/admin/brands/{id}`

删除指定品牌。

**业务校验:**
- 品牌下存在关联商品时，禁止删除

**成功响应:**

```json
{
  "code": 200,
  "message": "删除成功"
}
```

**删除失败响应:**

```json
{
  "code": 422,
  "message": "该品牌下存在关联商品，请先移除这些商品后再尝试删除"
}
```

---

## 八、订单管理模块

### 8.1 获取订单列表

**GET** `/api/v1/admin/orders`

分页获取订单列表。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 1 |
| size | int | 否 | 每页数量，默认 10 |
| orderNo | string | 否 | 订单编号（精确匹配） |
| status | string | 否 | 订单状态 |
| userId | long | 否 | 用户 ID |
| startDate | string | 否 | 开始日期（YYYY-MM-DD） |
| endDate | string | 否 | 结束日期（YYYY-MM-DD） |

**订单状态枚举:**

| 状态 | 说明 |
|------|------|
| PENDING_PAYMENT | 待付款 |
| PAID | 已付款 |
| SHIPPED | 已发货 |
| DELIVERED | 已送达 |
| COMPLETED | 已完成 |
| CANCELLED | 已取消 |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 50,
    "list": [
      {
        "id": 1,
        "orderNo": "ORD202604140001",
        "userId": 100,
        "username": "user001",
        "totalAmount": 8999.00,
        "status": "PAID",
        "expressCompany": null,
        "expressNo": null,
        "createdAt": "2026-04-14 10:00:00"
      }
    ]
  }
}
```

---

### 8.2 获取订单详情

**GET** `/api/v1/admin/orders/{id}`

获取指定订单的详细信息，包含订单商品列表。

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderNo": "ORD202604140001",
    "userId": 100,
    "username": "user001",
    "receiverName": "张三",
    "phone": "13800138000",
    "province": "广东省",
    "city": "深圳市",
    "district": "南山区",
    "detailAddress": "科技园路 1 号",
    "totalAmount": 8999.00,
    "status": "PAID",
    "expressCompany": null,
    "expressNo": null,
    "note": "请尽快发货",
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "iPhone 15 Pro",
        "skuId": 1001,
        "skuName": "深空黑色 256GB",
        "price": 8999.00,
        "quantity": 1,
        "subtotal": 8999.00
      }
    ],
    "createdAt": "2026-04-14 10:00:00"
  }
}
```

---

### 8.3 订单发货

**PATCH** `/api/v1/admin/orders/{id}/ship`

为订单填写物流信息并发货。

**Request:**

```json
{
  "expressCompany": "顺丰速运",
  "expressNo": "SF1234567890"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| expressCompany | string | 是 | 物流公司名称 |
| expressNo | string | 是 | 运单号 |

**业务校验:**
- 仅 `PAID`（已付款）状态的订单可以发货

**Response:**

```json
{
  "code": 200,
  "message": "发货成功"
}
```

**发货失败响应:**

```json
{
  "code": 422,
  "message": "当前订单状态不允许发货"
}
```

---

## 九、用户管理模块

### 9.1 获取用户列表

**GET** `/api/v1/admin/users`

分页获取用户列表。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 1 |
| size | int | 否 | 每页数量，默认 10 |
| username | string | 否 | 用户名（模糊匹配） |
| status | string | 否 | 状态：ACTIVE / DISABLED |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 500,
    "list": [
      {
        "id": 1,
        "username": "user001",
        "nickname": "用户一",
        "email": "user001@example.com",
        "phone": "13800138000",
        "status": "ACTIVE",
        "orderCount": 10,
        "totalSpent": 50000.00,
        "createdAt": "2026-01-01 10:00:00"
      }
    ]
  }
}
```

---

### 9.2 获取用户详情

**GET** `/api/v1/admin/users/{id}`

获取指定用户的详细信息。

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "user001",
    "nickname": "用户一",
    "email": "user001@example.com",
    "phone": "13800138000",
    "avatar": "https://xxx/avatar.jpg",
    "status": "ACTIVE",
    "orderCount": 10,
    "totalSpent": 50000.00,
    "createdAt": "2026-01-01 10:00:00",
    "updatedAt": "2026-04-14 10:00:00"
  }
}
```

---

### 9.3 更新用户状态

**PATCH** `/api/v1/admin/users/{id}/status`

更新用户状态（封禁/解封）。

**Request:**

```json
{
  "status": "DISABLED"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | string | 是 | ACTIVE（正常）/ DISABLED（封禁） |

**Response:**

```json
{
  "code": 200,
  "message": "状态更新成功"
}
```

---

### 9.4 分配用户角色

**POST** `/api/v1/admin/users/{id}/roles`

为指定用户分配角色（注：此为平台用户角色，非管理员角色）。

**Request:**

```json
{
  "roleIds": [2, 3]
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| roleIds | array | 是 | 角色 ID 列表（会替换原有的角色） |

**Response:**

```json
{
  "code": 200,
  "message": "角色分配成功"
}
```

---

## 十、数据看板模块

### 10.1 销售汇总

**GET** `/api/v1/admin/dashboard/sales-summary`

获取销售汇总数据。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| startDate | string | 是 | 开始日期（YYYY-MM-DD） |
| endDate | string | 是 | 结束日期（YYYY-MM-DD） |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalOrders": 1000,
    "totalAmount": 500000.00,
    "totalUsers": 500,
    "avgOrderAmount": 500.00
  }
}
```

---

### 10.2 销售趋势

**GET** `/api/v1/admin/dashboard/sales-trend`

获取销售趋势数据（用于图表展示）。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| startDate | string | 是 | 开始日期（YYYY-MM-DD） |
| endDate | string | 是 | 结束日期（YYYY-MM-DD） |
| type | string | 否 | 统计维度：day（按日）/ week（按周）/ month（按月），默认 day |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    { "date": "2026-04-01", "orders": 100, "amount": 50000.00 },
    { "date": "2026-04-02", "orders": 120, "amount": 60000.00 },
    { "date": "2026-04-03", "orders": 80, "amount": 40000.00 }
  ]
}
```

---

### 10.3 热销商品

**GET** `/api/v1/admin/dashboard/top-products`

获取热销商品排行。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| startDate | string | 否 | 开始日期（YYYY-MM-DD） |
| endDate | string | 否 | 结束日期（YYYY-MM-DD） |
| limit | int | 否 | 返回数量，默认 10，最大 100 |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "rank": 1,
      "productId": 1,
      "productName": "iPhone 15 Pro",
      "brandName": "Apple",
      "salesCount": 500,
      "salesAmount": 4499500.00
    },
    {
      "rank": 2,
      "productId": 2,
      "productName": "MacBook Pro",
      "brandName": "Apple",
      "salesCount": 200,
      "salesAmount": 2999800.00
    }
  ]
}
```

---

### 10.4 UV/PV 统计

**GET** `/api/v1/admin/dashboard/uv-pv`

获取 UV/PV 统计数据。

**Request:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| startDate | string | 是 | 开始日期（YYYY-MM-DD） |
| endDate | string | 是 | 结束日期（YYYY-MM-DD） |

**Response:**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalUv": 10000,
    "totalPv": 50000,
    "avgPvPerUv": 5.0,
    "dailyList": [
      { "date": "2026-04-14", "uv": 1000, "pv": 5000 },
      { "date": "2026-04-13", "uv": 950, "pv": 4750 }
    ]
  }
}
```

---

## 十一、错误码对照表

| HTTP 状态码 | 业务码 | 说明 |
|-------------|--------|------|
| 200 | 200 | 操作成功 |
| 400 | 400 | 请求参数错误 |
| 401 | 401 | 未授权（Token 缺失或失效） |
| 403 | 403 | 无权限访问 |
| 404 | 404 | 资源不存在 |
| 409 | 409 | 资源冲突（如用户名已存在） |
| 422 | 422 | 业务校验失败 |
| 500 | 500 | 服务器内部错误 |

---

**文档版本**: v1.0
**最后更新**: 2026-04-14
