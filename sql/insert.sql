-- =============================
-- 插入默认部门（技术部）
-- =============================
INSERT INTO `sys_departments` (
    `id`, `department_name`, `parent_id`, `ancestors`, `order_num`, `status`, `leader`,
    `created_at`, `created_by`, `updated_at`, `updated_by`
) VALUES (
             1001, '顶层组织', 0, '', '1', '1', NULL,
             NOW(), 0, NOW(), 0
         );

-- =============================
-- 插入超级管理员角色
-- =============================
INSERT INTO `sys_roles` (
    `id`, `role_name`, `role_code`, `role_desc`, `role_order`, `status`,
    `created_at`, `created_by`, `updated_at`, `updated_by`
) VALUES (
             2001, '超级管理员', 'admin', '系统内置超级管理员角色', 1, 1,
             NOW(), 0, NOW(), 0
         );

-- =============================
-- 插入超级管理员用户 admin
-- 密码：你可以替换为加密后的密码
-- =============================
INSERT INTO `sys_users` (
    `id`, `username`, `password`, `nickname`, `avatar`, `email`, `phone`, `sex`, `status`,
    `created_at`, `created_by`, `updated_at`, `updated_by`
) VALUES (
             3001, 'admin', '$2a$10$abcdef1234567890fakehashforadmin', -- 示例密码，请替换为实际加密值
             '系统管理员', NULL, 'admin@ahzx.site', '13800000001', 1, 1,
             NOW(), 0, NOW(), 0
         );

-- =============================
-- 绑定 admin 与 超级管理员角色
-- =============================
INSERT INTO `sys_user_roles` (`user_id`, `role_id`)
VALUES (3001, 2001);

-- =============================
-- 绑定 admin 与 部门
-- =============================
INSERT INTO `sys_user_departments` (`user_id`, `department_id`)
VALUES (3001, 1001);