-- ============================================================
-- 运营管理后台数据库脚本
-- 版本: v1.0
-- 日期: 2026-04-14
-- ============================================================

USE shop_v3;

-- ============================================================
-- 1. 管理员表 (admins)
-- ============================================================
CREATE TABLE IF NOT EXISTS `admins` (
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

-- ============================================================
-- 2. 角色表 (roles)
-- ============================================================
CREATE TABLE IF NOT EXISTS `roles` (
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    `name`        VARCHAR(50)  NOT NULL UNIQUE COMMENT '角色名称',
    `code`        VARCHAR(50)  NOT NULL UNIQUE COMMENT '角色代码',
    `description` VARCHAR(255) COMMENT '角色描述',
    `sort`        INT DEFAULT 0 COMMENT '排序权重',
    `status`      VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-正常 DISABLED-禁用',
    `is_system`   TINYINT UNSIGNED DEFAULT 0 COMMENT '系统内置：0-否 1-是（内置角色不可删除）',
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_name` (`name`),
    INDEX `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ============================================================
-- 3. 权限表 (permissions)
-- ============================================================
CREATE TABLE IF NOT EXISTS `permissions` (
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

-- ============================================================
-- 4. 管理员-角色关联表 (admin_roles)
-- ============================================================
CREATE TABLE IF NOT EXISTS `admin_roles` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `admin_id`   BIGINT NOT NULL COMMENT '管理员ID',
    `role_id`    BIGINT NOT NULL COMMENT '角色ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE INDEX `uk_admin_role` (`admin_id`, `role_id`),
    INDEX `idx_admin_id` (`admin_id`),
    INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员-角色关联表';

-- ============================================================
-- 5. 角色-权限关联表 (role_permissions)
-- ============================================================
CREATE TABLE IF NOT EXISTS `role_permissions` (
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `role_id`       BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `created_at`    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE INDEX `uk_role_permission` (`role_id`, `permission_id`),
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-权限关联表';

-- ============================================================
-- 6. 管理员登录日志表 (admin_login_logs)
-- ============================================================
CREATE TABLE IF NOT EXISTS `admin_login_logs` (
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

-- ============================================================
-- 7. 管理员操作日志表 (admin_operation_logs)
-- ============================================================
CREATE TABLE IF NOT EXISTS `admin_operation_logs` (
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

-- ============================================================
-- 8. 商品访问统计表 (product_stats)
-- ============================================================
CREATE TABLE IF NOT EXISTS `product_stats` (
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

-- ============================================================
-- 9. 每日统计汇总表 (daily_stats)
-- ============================================================
CREATE TABLE IF NOT EXISTS `daily_stats` (
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

-- ============================================================
-- 10. 修改现有表 - 商品表 (products)
-- ============================================================
ALTER TABLE `products` ADD COLUMN `status` VARCHAR(20) DEFAULT 'ON_SALE' COMMENT '商品状态：ON_SALE-上架 OFF_SALE-下架';
ALTER TABLE `products` ADD COLUMN `sales_count` INT DEFAULT 0 COMMENT '销量';
ALTER TABLE `products` ADD COLUMN `deleted` TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除';
ALTER TABLE `products` ADD INDEX `idx_status` (`status`);
ALTER TABLE `products` ADD INDEX `idx_deleted` (`deleted`);

-- ============================================================
-- 11. 修改现有表 - 用户表 (users)
-- ============================================================
ALTER TABLE `users` ADD COLUMN `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '用户状态：ACTIVE-正常 DISABLED-封禁';

-- ============================================================
-- 12. 初始化数据 - 超级管理员角色
-- ============================================================
INSERT INTO `roles` (`name`, `code`, `description`, `sort`, `status`, `is_system`)
VALUES ('超级管理员', 'SUPER_ADMIN', '系统内置超级管理员，拥有所有权限', 0, 'ACTIVE', 1);

-- ============================================================
-- 13. 初始化数据 - 权限表（树形结构）
-- ============================================================
-- 一级权限：系统管理
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES ('系统管理', 'SYSTEM', 'MENU', NULL, '/system', 'setting', 0);

-- 二级权限：系统管理下级
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES
('管理员管理', 'ADMIN', 'MENU', 1, '/system/admin', 'user', 1),
('角色管理', 'ROLE', 'MENU', 1, '/system/role', 'team', 2),
('权限管理', 'PERMISSION', 'MENU', 1, '/system/permission', 'lock', 3),
('操作日志', 'LOG', 'MENU', 1, '/system/log', 'file-text', 4);

-- 三级权限：管理员管理的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `sort`)
VALUES
('管理员查看', 'ADMIN:VIEW', 'BUTTON', 5, 1),
('管理员编辑', 'ADMIN:EDIT', 'BUTTON', 5, 2);

-- 三级权限：角色管理的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `sort`)
VALUES
('角色查看', 'ROLE:VIEW', 'BUTTON', 6, 1),
('角色编辑', 'ROLE:EDIT', 'BUTTON', 6, 2);

-- 三级权限：权限管理的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `sort`)
VALUES
('权限查看', 'PERMISSION:VIEW', 'BUTTON', 7, 1),
('权限编辑', 'PERMISSION:EDIT', 'BUTTON', 7, 2);

-- 三级权限：操作日志的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `sort`)
VALUES
('日志查看', 'LOG:VIEW', 'BUTTON', 8, 1);

-- 一级权限：商品管理
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES ('商品管理', 'PRODUCT', 'MENU', NULL, '/product', 'shopping-cart', 1);

-- 二级权限：商品管理下级
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES
('商品列表', 'PRODUCT:LIST', 'MENU', 10, '/product/list', 'box', 1),
('分类管理', 'CATEGORY', 'MENU', 10, '/product/category', 'folder', 2),
('品牌管理', 'BRAND', 'MENU', 10, '/product/brand', 'apartment', 3);

-- 三级权限：商品列表的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `sort`)
VALUES
('商品查看', 'PRODUCT:VIEW', 'BUTTON', 11, 1),
('商品编辑', 'PRODUCT:EDIT', 'BUTTON', 11, 2),
('商品上下架', 'PRODUCT:STATUS', 'BUTTON', 11, 3);

-- 三级权限：分类管理的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `sort`)
VALUES
('分类查看', 'CATEGORY:VIEW', 'BUTTON', 12, 1),
('分类编辑', 'CATEGORY:EDIT', 'BUTTON', 12, 2);

-- 三级权限：品牌管理的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `sort`)
VALUES
('品牌查看', 'BRAND:VIEW', 'BUTTON', 13, 1),
('品牌编辑', 'BRAND:EDIT', 'BUTTON', 13, 2);

-- 一级权限：订单管理
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES ('订单管理', 'ORDER', 'MENU', NULL, '/order', 'file-text', 2);

-- 二级权限：订单管理的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES
('订单列表', 'ORDER:LIST', 'MENU', 15, '/order/list', 'unordered-list', 1),
('订单发货', 'ORDER:SHIP', 'BUTTON', 15, NULL, NULL, 2);

-- 一级权限：用户管理
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES ('用户管理', 'USER', 'MENU', NULL, '/user', 'user', 3);

-- 二级权限：用户管理的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES
('用户列表', 'USER:LIST', 'MENU', 17, '/user/list', 'user', 1),
('用户禁用', 'USER:DISABLE', 'BUTTON', 17, NULL, NULL, 2),
('角色分配', 'USER:ROLE', 'BUTTON', 17, NULL, NULL, 3);

-- 一级权限：数据看板
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES ('数据看板', 'DASHBOARD', 'MENU', NULL, '/dashboard', 'dashboard', 4);

-- 二级权限：数据看板的按钮级权限
INSERT INTO `permissions` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES
('销售统计', 'DASHBOARD:SALES', 'MENU', 19, '/dashboard/sales', 'line-chart', 1),
('UV/PV统计', 'DASHBOARD:UVPV', 'MENU', 19, '/dashboard/uvpv', 'bar-chart', 2);

-- ============================================================
-- 14. 初始化数据 - 超级管理员角色分配所有权限
-- ============================================================
INSERT INTO `role_permissions` (`role_id`, `permission_id`)
SELECT 1, `id` FROM `permissions` WHERE `status` = 'ACTIVE';

-- ============================================================
-- 15. 初始化数据 - 默认超级管理员账号
-- 密码: admin123 (BCrypt加密后的值)
-- ============================================================
INSERT INTO `admins` (`username`, `password`, `nickname`, `email`, `status`, `created_by`)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '超级管理员', 'admin@example.com', 'ACTIVE', NULL);

-- ============================================================
-- 16. 为超级管理员分配角色
-- ============================================================
INSERT INTO `admin_roles` (`admin_id`, `role_id`)
VALUES (1, 1);

-- ============================================================
-- 17. 初始化数据 - 初始每日统计数据表（用于测试）
-- ============================================================
INSERT INTO `daily_stats` (`stat_date`, `new_users`, `new_orders`, `order_amount`, `total_uv`, `total_pv`)
VALUES
(CURDATE(), 10, 50, 25000.00, 1000, 5000),
(DATE_SUB(CURDATE(), INTERVAL 1 DAY), 8, 45, 22500.00, 950, 4750),
(DATE_SUB(CURDATE(), INTERVAL 2 DAY), 12, 60, 30000.00, 1200, 6000);


