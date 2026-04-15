-- ============================================================
-- 修复数据库表结构以匹配后端实体类
-- 基于 complete.sql 导出的当前结构，只添加缺少的列
-- ============================================================

USE shop_v3;

-- ============================================================
-- 1. 修复 orders 表 - 添加物流相关字段
-- ============================================================
ALTER TABLE `orders`
    ADD COLUMN `express_company` VARCHAR(100) DEFAULT NULL COMMENT '物流公司' AFTER `note`,
    ADD COLUMN `express_no` VARCHAR(100) DEFAULT NULL COMMENT '运单号' AFTER `express_company`,
    ADD COLUMN `shipped_at` DATETIME DEFAULT NULL COMMENT '发货时间' AFTER `express_no`;

-- ============================================================
-- 3. 修复 categories 表 - 添加状态字段
-- ============================================================
ALTER TABLE `categories`
    ADD COLUMN `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-正常 DISABLED-禁用' AFTER `icon`;

-- ============================================================
-- 4. 修复 product_stats 表 - 添加订单和退款统计字段
-- ============================================================
ALTER TABLE `product_stats`
    ADD COLUMN `order_count` INT DEFAULT 0 COMMENT '下单次数' AFTER `cart_count`,
    ADD COLUMN `refund_count` INT DEFAULT 0 COMMENT '退款次数' AFTER `order_count`;
