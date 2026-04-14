CREATE DATABASE IF NOT EXISTS shop_v3
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE shop_v3;

-- ============================================================
-- 品牌表 (brands)
-- ============================================================
CREATE TABLE `brands` (
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '品牌ID',
    `name`        VARCHAR(100) NOT NULL UNIQUE COMMENT '品牌名称',
    `logo`        VARCHAR(255) COMMENT '品牌 Logo 链接',
    `description` TEXT COMMENT '品牌描述',
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌表';

-- ============================================================
-- 商品分类表 (categories)
-- ============================================================
CREATE TABLE `categories` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    `name`       VARCHAR(100) NOT NULL COMMENT '分类名称',
    `parent_id`  BIGINT DEFAULT NULL COMMENT '父分类ID，NULL 表示顶级分类',
    `sort`       INT DEFAULT 0 COMMENT '排序权重，越小越靠前',
    `icon`       VARCHAR(255) COMMENT '分类图标链接',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- ============================================================
-- 用户表 (users)
-- ============================================================
CREATE TABLE `users` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `username`   VARCHAR(50)  NOT NULL UNIQUE COMMENT '账户登录名',
    `password`   VARCHAR(255) NOT NULL COMMENT '登录密码(BCrypt)',
    `email`      VARCHAR(100) COMMENT '电子邮箱',
    `phone`      VARCHAR(20) COMMENT '联系手机',
    `nickname`   VARCHAR(50) COMMENT '页面展示昵称',
    `avatar`     VARCHAR(255) COMMENT '头像缩略图链接',
    `status`     VARCHAR(20) DEFAULT 'active' COMMENT '账户状态：active / disabled',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================================
-- 产品表 (products)
-- ============================================================
CREATE TABLE `products` (
    `id`             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '产品ID',
    `name`           VARCHAR(100) NOT NULL COMMENT '产品名称',
    `description`    TEXT COMMENT '详细描述/富文本',
    `images`         JSON COMMENT '轮播图列表 (URL数组)',
    `attributes`     JSON COMMENT '商品规格属性（如颜色、内存等）',
    `original_price` DECIMAL(10, 2) COMMENT '原价',
    `price`          DECIMAL(10, 2) NOT NULL COMMENT '售价',
    `stock`          INT DEFAULT 0 COMMENT '当前库存',
    `category_id`    BIGINT COMMENT '所属分类ID',
    `brand_id`       BIGINT COMMENT '所属品牌ID',
    `created_at`     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CHECK (`price` >= 0),
    CHECK (`stock` >= 0),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- ============================================================
-- SKU 规格表 (skus)
-- ============================================================
CREATE TABLE `skus` (
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'SKU ID',
    `product_id`  BIGINT NOT NULL COMMENT '所属商品ID',
    `name`        VARCHAR(200) NOT NULL COMMENT '规格名称（如"黑色 256GB"）',
    `spec_values` JSON COMMENT '规格键值对（如 {"颜色":"黑色","内存":"256GB"}）',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT 'SKU 售价',
    `stock`       INT DEFAULT 0 COMMENT 'SKU 库存',
    `image`       VARCHAR(255) COMMENT 'SKU 图片链接',
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CHECK (`price` >= 0),
    CHECK (`stock` >= 0),
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SKU 规格表';

-- ============================================================
-- 场景表 (scenarios)
-- ============================================================
CREATE TABLE `scenarios` (
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '场景ID',
    `name`        VARCHAR(100) NOT NULL COMMENT '场景名称',
    `type`        INT COMMENT '场景类型：1-电竞游戏 2-办公生产力 3-创意设计 4-学习编程 5-家庭娱乐',
    `description` TEXT COMMENT '场景详尽描述',
    `cover_url`   VARCHAR(255) COMMENT '场景封面图链接',
    `config_data` JSON COMMENT '场景配置参数或扩展元数据',
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景配置表';

-- ============================================================
-- 场景-商品关联表 (scenario_products)
-- ============================================================
CREATE TABLE `scenario_products` (
    `id`               BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
    `scenario_id`      BIGINT NOT NULL COMMENT '场景ID',
    `product_id`       BIGINT NOT NULL COMMENT '商品ID',
    `recommend_reason` VARCHAR(500) COMMENT '推荐理由',
    `sort`             INT DEFAULT 0 COMMENT '排序权重，越小越靠前',
    `created_at`       DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX `uk_scenario_product` (`scenario_id`, `product_id`),
    INDEX `idx_scenario_id` (`scenario_id`),
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景-商品关联表';

-- ============================================================
-- 收货地址表 (addresses)
-- ============================================================
CREATE TABLE `addresses` (
    `id`             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '地址ID',
    `user_id`        BIGINT NOT NULL COMMENT '关联的用户ID',
    `receiver_name`  VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `phone`          VARCHAR(20) NOT NULL COMMENT '联系电话',
    `province`       VARCHAR(50) NOT NULL COMMENT '省份',
    `city`           VARCHAR(50) NOT NULL COMMENT '城市',
    `district`       VARCHAR(50) NOT NULL COMMENT '区县',
    `detail_address` VARCHAR(255) NOT NULL COMMENT '详细地址',
    `is_default`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否为默认地址：0-否 1-是',
    `created_at`     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CHECK (`is_default` IN (0, 1)),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- ============================================================
-- 购物车表 (carts)
-- ============================================================
CREATE TABLE `carts` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车项ID',
    `user_id`    BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `sku_id`     BIGINT COMMENT '具体规格(SKU)ID',
    `quantity`   INT NOT NULL DEFAULT 1 COMMENT '购买数量',
    `selected`   TINYINT UNSIGNED DEFAULT 1 COMMENT '是否选中：0-否 1-是',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CHECK (`quantity` > 0),
    CHECK (`selected` IN (0, 1)),
    INDEX `idx_user_id` (`user_id`),
    UNIQUE INDEX `uk_user_product_sku` (`user_id`, `product_id`, `sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ============================================================
-- 订单表 (orders)
-- ============================================================
CREATE TABLE `orders` (
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    `order_no`     VARCHAR(64) NOT NULL UNIQUE COMMENT '订单编号',
    `user_id`      BIGINT NOT NULL COMMENT '买家ID',
    `address_id`   BIGINT NOT NULL COMMENT '收货地址ID',
    `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    `status`       VARCHAR(20) NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '订单状态：PENDING_PAYMENT-待付款 PAID-已付款 SHIPPED-已发货 DELIVERED-已送达 COMPLETED-已完成 CANCELLED-已取消 REFUNDING-退款中',
    `note`         VARCHAR(255) COMMENT '订单备注',
    `deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    `created_at`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CHECK (`total_amount` >= 0),
    CHECK (`deleted` IN (0, 1)),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ============================================================
-- 订单明细表 (order_items)
-- ============================================================
CREATE TABLE `order_items` (
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单明细ID',
    `order_id`     BIGINT NOT NULL COMMENT '所属订单ID',
    `product_id`   BIGINT NOT NULL COMMENT '商品ID',
    `sku_id`       BIGINT COMMENT '规格(SKU)ID',
    `product_name` VARCHAR(100) NOT NULL COMMENT '商品名称（下单时快照）',
    `price`        DECIMAL(10, 2) NOT NULL COMMENT '购买时单价（下单时快照）',
    `quantity`     INT NOT NULL COMMENT '购买数量',
    `created_at`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CHECK (`price` >= 0),
    CHECK (`quantity` > 0),
    INDEX `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- ============================================================
-- 支付流水表 (payments)
-- ============================================================
CREATE TABLE `payments` (
    `id`             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '支付ID',
    `order_id`       BIGINT NOT NULL COMMENT '关联的订单ID',
    `payment_method` VARCHAR(50) NOT NULL COMMENT '支付方式：alipay-支付宝 wechat-微信 bank_card-银行卡',
    `amount`         DECIMAL(10, 2) NOT NULL COMMENT '支付金额',
    `status`         VARCHAR(20) NOT NULL DEFAULT 'unpaid' COMMENT '支付状态：unpaid-未支付 paid-已支付 refunded-已退款',
    `created_at`     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CHECK (`amount` >= 0),
    INDEX `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';
