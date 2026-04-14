# 数据库 SQL 审查与补充计划

## 审查方法
逐表对比 `shop_v3_schema.sql` 与 Java Entity 类的字段，同时结合 Service 层业务逻辑检查索引、约束、缺漏。

---

## 发现的问题清单

### 一、🔴 SQL 缺表：3 张表完全缺失

| 缺失表 | 对应 Entity | 说明 |
|--------|------------|------|
| `brands` | `Brand.java` | 品牌表，Product 有 `brand_id` 外键但无对应表 |
| `categories` | `Category.java` | 分类表，Product 有 `category_id` 外键但无对应表 |
| `skus` | `Sku.java` | SKU 规格表，Cart/OrderItem 有 `sku_id` 外键但无对应表 |
| `scenario_products` | `ScenarioProduct.java` | 场景-商品关联表，ScenarioServiceImpl 中使用 |

### 二、🔴 字段缺失：SQL 表与 Entity 字段不一致

| 表名 | SQL 缺失字段 | Entity 字段 | 说明 |
|------|-------------|------------|------|
| `products` | `category_id` | `Product.categoryId` | 商品所属分类，SQL 中无此列 |
| `products` | `brand_id` | `Product.brandId` | 商品所属品牌，SQL 中无此列 |
| `carts` | `selected` | `Cart.selected` | 购物车选中状态，SQL 中无此列 |
| `orders` | `deleted` | `Order.deleted` + `@TableLogic` | 逻辑删除字段，SQL 中无此列 |

### 三、🟡 索引缺失：外键和查询字段无索引

| 表名 | 缺失索引 | 原因 |
|------|---------|------|
| `addresses` | `idx_user_id` | 按 userId 查地址列表 |
| `carts` | `idx_user_id` | 按 userId 查购物车 |
| `carts` | `uk_user_product_sku` | 同一用户同一 SKU 不应重复（CartServiceImpl.addToCart 依赖此逻辑） |
| `orders` | `idx_user_id` | 按 userId 查订单列表 |
| `orders` | `idx_order_no` | orderNo 已 UNIQUE 但需显式声明 |
| `order_items` | `idx_order_id` | 按 orderId 查订单明细 |
| `payments` | `idx_order_id` | 按 orderId 查支付记录 |
| `payments` | `idx_user_id` (间接) | 通过 orderId 关联查询 |
| `scenario_products` | `idx_scenario_id` | 按场景 ID 查关联商品 |
| `scenario_products` | `idx_product_id` | 按商品 ID 反查场景 |
| `products` | `idx_category_id` | 按分类筛选商品 |
| `products` | `idx_brand_id` | 按品牌筛选商品 |

### 四、🟡 数据类型/约束问题

| 表名 | 问题 | 修复 |
|------|------|------|
| `orders.status` | 默认值 `'pending'`，但代码中使用大写 `'PENDING_PAYMENT'` | 默认值改为 `'PENDING_PAYMENT'` |
| `payments.status` | 默认值 `'unpaid'`，但代码中使用 `'paid'`/`'refunded'` | 默认值改为 `'unpaid'`，添加 CHECK 约束 |
| `payments.payment_method` | 无约束，代码中使用 `'alipay'`/`'wechat'` 等 | 添加注释说明合法值 |
| `carts.quantity` | 无 CHECK 约束 | 添加 `CHECK (quantity > 0)` |
| `orders.total_amount` | 无 CHECK 约束 | 添加 `CHECK (total_amount >= 0)` |
| `products.price` | 无 CHECK 约束 | 添加 `CHECK (price >= 0)` |
| `products.stock` | 无 CHECK 约束 | 添加 `CHECK (stock >= 0)` |
| `addresses.is_default` | 使用 `TINYINT(1)` | 改为更标准的 `TINYINT UNSIGNED`，添加 `CHECK (is_default IN (0, 1))` |

### 五、🟡 逻辑设计缺漏

| 问题 | 说明 | 修复 |
|------|------|------|
| 订单状态无完整枚举约束 | 代码中存在 PENDING_PAYMENT/PAID/SHIPPED/DELIVERED/COMPLETED/CANCELLED/REFUNDING 7 种状态，SQL 注释未说明 | 添加完整 COMMENT |
| 支付状态无完整枚举约束 | 代码中存在 unpaid/paid/refunded，SQL 注释未说明 | 添加完整 COMMENT |
| 场景表缺少 type 字段 | `ScenarioTypeEnum` 定义了 5 种场景类型，但 `scenarios` 表无 `type` 列 | 添加 `type` 字段 |

---

## 实施步骤

1. 在 `sql/` 目录下创建 `shop_v3_schema_v2.sql`，包含完整的修正后建表语句
2. 内容包括：
   - 原有 8 张表的修正（补字段、补索引、补约束、修默认值）
   - 新增 4 张缺失表（brands、categories、skus、scenario_products）
   - 所有外键字段添加索引
   - 所有状态字段添加 COMMENT 说明合法值
   - 金额/数量字段添加 CHECK 约束
