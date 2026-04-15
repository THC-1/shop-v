-- 1. 修复 daily_stats 表 - 添加缺少的列并重命名
ALTER TABLE `daily_stats`
    ADD COLUMN `total_orders` INT DEFAULT 0 COMMENT '总订单数' AFTER `stat_date`,
    ADD COLUMN `total_users` INT DEFAULT 0 COMMENT '总用户数' AFTER `total_orders`,
    ADD COLUMN `total_products` INT DEFAULT 0 COMMENT '总商品数' AFTER `total_users`,
    ADD COLUMN `uv` INT DEFAULT 0 COMMENT '独立访客数' AFTER `total_products`,
    ADD COLUMN `pv` INT DEFAULT 0 COMMENT '页面浏览量' AFTER `uv`;

-- 重命名 daily_stats 的列以匹配后端实体类
ALTER TABLE `daily_stats`
    CHANGE `new_orders` `total_orders` INT DEFAULT 0 COMMENT '总订单数',
    CHANGE `new_users` `total_users` INT DEFAULT 0 COMMENT '总用户数',
    CHANGE `total_uv` `uv` INT DEFAULT 0 COMMENT '独立访客数',
    CHANGE `total_pv` `pv` INT DEFAULT 0 COMMENT '页面浏览量';

-- 添加缺少的 total_products 列
ALTER TABLE `daily_stats`
    ADD COLUMN `total_products` INT DEFAULT 0 COMMENT '总商品数' AFTER `total_users`;

-- 删除不再需要的列
ALTER TABLE `daily_stats`
    DROP COLUMN `order_amount`;

-- 修复 product_stats 表（如果 cart_count 列不存在才执行）
ALTER TABLE `product_stats`
    ADD COLUMN `cart_count` INT DEFAULT 0 COMMENT '加购次数' AFTER `pv`,
    ADD COLUMN `order_count` INT DEFAULT 0 COMMENT '下单次数' AFTER `cart_count`,
    ADD COLUMN `refund_count` INT DEFAULT 0 COMMENT '退款次数' AFTER `order_count`;
-- 2. 修复 product_stats 表 - 添加缺少的列
ALTER TABLE `product_stats`
    ADD COLUMN `cart_count` INT DEFAULT 0 COMMENT '加购次数' AFTER `pv`,
    ADD COLUMN `order_count` INT DEFAULT 0 COMMENT '下单次数' AFTER `cart_count`,
    ADD COLUMN `refund_count` INT DEFAULT 0 COMMENT '退款次数' AFTER `order_count`;