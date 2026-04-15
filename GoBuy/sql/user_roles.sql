-- 用户-角色关联表 (user_roles)
CREATE TABLE IF NOT EXISTS `user_roles` (
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `user_id`    BIGINT NOT NULL COMMENT '用户ID',
    `role_id`    BIGINT NOT NULL COMMENT '角色ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE INDEX `uk_user_role` (`user_id`, `role_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';