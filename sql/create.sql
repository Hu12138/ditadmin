-- 通用父类字段
-- 可封装为公共片段用于拼接
-- id 不是 auto_increment，依赖 FlexId 算法生成

-- ================================
-- 1. 部门表 sys_departments
-- ================================
CREATE TABLE `sys_departments` (
                                   `id` BIGINT PRIMARY KEY COMMENT '主键',
                                   `created_at` DATETIME COMMENT '创建时间',
                                   `created_by` BIGINT COMMENT '创建者',
                                   `updated_at` DATETIME COMMENT '更新时间',
                                   `updated_by` BIGINT COMMENT '更新者',
                                   `parent_id` BIGINT COMMENT '部门上级ID',
                                   `department_name` VARCHAR(255) COMMENT '部门名称',
                                   `ancestors` VARCHAR(255) COMMENT '祖级列表',
                                   `order_num` VARCHAR(50) COMMENT '排序',
                                   `status` VARCHAR(10) COMMENT '状态 0：禁用 1：正常',
                                   `leader` BIGINT COMMENT '负责人'
) COMMENT='部门信息表';

-- ================================
-- 2. 菜单表 sys_menus
-- ================================
CREATE TABLE `sys_menus` (
                             `id` BIGINT PRIMARY KEY COMMENT '主键',
                             `created_at` DATETIME COMMENT '创建时间',
                             `created_by` BIGINT COMMENT '创建者',
                             `updated_at` DATETIME COMMENT '更新时间',
                             `updated_by` BIGINT COMMENT '更新者',
                             `parent_id` BIGINT COMMENT '菜单父级ID',
                             `menu_name` VARCHAR(255) COMMENT '菜单名称',
                             `order_num` INT COMMENT '显示顺序',
                             `path` VARCHAR(255) COMMENT '路由地址',
                             `component` VARCHAR(255) COMMENT '组件路径',
                             `query_param` VARCHAR(255) COMMENT '请求参数',
                             `is_frame` VARCHAR(10) COMMENT '是否为外链（0是 1否）',
                             `icon` VARCHAR(100) COMMENT '图标',
                             `menu_type` INT COMMENT '菜单类型（M目录 C菜单 F按钮）',
                             `visible` VARCHAR(10) COMMENT '显示状态（0显示 1隐藏）',
                             `status` VARCHAR(10) COMMENT '菜单状态（0正常 1停用）',
                             `perms` VARCHAR(255) COMMENT '权限标识',
                             `remark` VARCHAR(500) COMMENT '备注'
) COMMENT='菜单信息表';

-- ================================
-- 3. 角色菜单关联表 sys_role_menus
-- ================================
CREATE TABLE `sys_role_menus` (
                                  `role_id` BIGINT COMMENT '角色ID',
                                  `menu_id` BIGINT COMMENT '菜单ID'
) COMMENT='角色-菜单关联表';

-- ================================
-- 4. 角色表 sys_roles
-- ================================
CREATE TABLE `sys_roles` (
                             `id` BIGINT PRIMARY KEY COMMENT '主键',
                             `created_at` DATETIME COMMENT '创建时间',
                             `created_by` BIGINT COMMENT '创建者',
                             `updated_at` DATETIME COMMENT '更新时间',
                             `updated_by` BIGINT COMMENT '更新者',
                             `role_name` VARCHAR(100) COMMENT '角色名称（可中文）',
                             `role_code` VARCHAR(100) COMMENT '角色编码（英文）',
                             `dataScope` VARCHAR(100) COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限，5:仅本人）',
                             `role_desc` VARCHAR(255) COMMENT '角色描述',
                             `role_order` INT COMMENT '角色排序',
                             `status` INT COMMENT '状态 0：禁用 1：正常'
) COMMENT='角色信息表';

-- ================================
-- 5. 用户部门关联表 sys_user_departments
-- ================================
CREATE TABLE `sys_user_departments` (
                                        `user_id` BIGINT COMMENT '用户ID',
                                        `department_id` BIGINT COMMENT '部门ID'
) COMMENT='用户-部门关联表';

-- ================================
-- 6. 用户角色关联表 sys_user_roles
-- ================================
CREATE TABLE `sys_user_roles` (
                                  `user_id` BIGINT COMMENT '用户ID',
                                  `role_id` BIGINT COMMENT '角色ID'
) COMMENT='用户-角色关联表';

-- ================================
-- 7. 用户表 sys_users
-- ================================
CREATE TABLE `sys_users` (
                             `id` BIGINT PRIMARY KEY COMMENT '主键',
                             `created_at` DATETIME COMMENT '创建时间',
                             `created_by` BIGINT COMMENT '创建者',
                             `updated_at` DATETIME COMMENT '更新时间',
                             `updated_by` BIGINT COMMENT '更新者',
                             `username` VARCHAR(100) NOT NULL COMMENT '用户名',
                             `password` VARCHAR(255) NOT NULL COMMENT '密码',
                             `nickname` VARCHAR(100) COMMENT '昵称',
                             `avatar` VARCHAR(255) COMMENT '头像',
                             `email` VARCHAR(100) COMMENT '邮箱',
                             `phone` VARCHAR(20) COMMENT '手机号',
                             `sex` INT COMMENT '性别 0：女 1：男',
                             `status` INT COMMENT '状态 0：禁用 1：正常'
) COMMENT='用户信息表';

-- ================================
-- 8. 角色部门关联表 sys_role_departments
-- ================================
CREATE TABLE `sys_role_departments` (
                                       `role_id` BIGINT COMMENT '角色ID',
                                       `department_id` BIGINT COMMENT '部门ID'
) COMMENT='角色-部门关联表';