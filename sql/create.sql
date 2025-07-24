-- ================================
-- RBAC数据权限管理系统初始化脚本
-- ================================

-- ================================
-- 1. 部门表 sys_depts
-- ================================
CREATE TABLE sys_depts (
                           id BIGINT PRIMARY KEY COMMENT '主键',
                           parent_id BIGINT COMMENT '部门上级id',
                           dept_name VARCHAR(255) COMMENT '部门名称',
                           ancestors TEXT COMMENT '祖级列表',
                           order_num VARCHAR(255) COMMENT '排序',
                           status VARCHAR(10) COMMENT '状态 0：禁用 1：正常',
                           leader BIGINT COMMENT '负责人',
                           created_at DATETIME COMMENT '创建时间',
                           created_by BIGINT COMMENT '创建者',
                           updated_at DATETIME COMMENT '更新时间',
                           updated_by BIGINT COMMENT '更新者',
                           tenant_id BIGINT COMMENT '租户ID'
) COMMENT = '部门表';

-- ================================
-- 2. 菜单表 sys_menus
-- ================================
CREATE TABLE sys_menus (
                           id BIGINT PRIMARY KEY COMMENT '主键',
                           parent_id BIGINT COMMENT '菜单ID',
                           menu_name VARCHAR(255) COMMENT '菜单名称',
                           order_num INT COMMENT '显示顺序',
                           path VARCHAR(255) COMMENT '路由地址',
                           component VARCHAR(255) COMMENT '组件路径',
                           query_param VARCHAR(255) COMMENT '请求参数',
                           is_frame VARCHAR(10) COMMENT '是否为外链（0是 1否）',
                           icon VARCHAR(255) COMMENT '图标',
                           menu_type INT COMMENT '菜单类型（M目录 C菜单 F按钮）',
                           visible VARCHAR(10) COMMENT '显示状态（0显示 1隐藏）',
                           status VARCHAR(10) COMMENT '菜单状态（0正常 1停用）',
                           perms VARCHAR(255) COMMENT '权限标识',
                           remark TEXT COMMENT '备注',
                           created_at DATETIME COMMENT '创建时间',
                           created_by BIGINT COMMENT '创建者',
                           updated_at DATETIME COMMENT '更新时间',
                           updated_by BIGINT COMMENT '更新者',
                           tenant_id BIGINT COMMENT '租户ID'
) COMMENT = '菜单表';

-- ================================
-- 3. 角色部门关联表 sys_role_depts
-- ================================
CREATE TABLE sys_role_depts (
                                role_id BIGINT NOT NULL,
                                dep_id BIGINT NOT NULL,
                                PRIMARY KEY (role_id, dep_id)
) COMMENT = '角色部门关联表';

-- ================================
-- 4. 角色菜单关联表 sys_role_menus
-- ================================
CREATE TABLE sys_role_menus (
                                role_id BIGINT NOT NULL,
                                menu_id BIGINT NOT NULL,
                                PRIMARY KEY (role_id, menu_id)
) COMMENT = '角色菜单关联表';

-- ================================
-- 5. 角色表 sys_roles
-- ================================
CREATE TABLE sys_roles (
                           id BIGINT PRIMARY KEY COMMENT '主键',
                           role_name VARCHAR(255) COMMENT '角色名称',
                           role_code VARCHAR(255) COMMENT '角色编码',
                           role_desc TEXT COMMENT '角色描述',
                           role_order INT COMMENT '角色排序',
                           data_scope VARCHAR(10) COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限）',
                           status INT COMMENT '状态 0：禁用 1：正常',
                           created_at DATETIME COMMENT '创建时间',
                           created_by BIGINT COMMENT '创建者',
                           updated_at DATETIME COMMENT '更新时间',
                           updated_by BIGINT COMMENT '更新者',
                           tenant_id BIGINT COMMENT '租户ID'
) COMMENT = '角色表';

-- ================================
-- 6. 租户表 sys_tenant
-- ================================
CREATE TABLE sys_tenant (
                            id BIGINT PRIMARY KEY COMMENT '主键',
                            contact_user_name VARCHAR(255) COMMENT '联系人',
                            contact_phone VARCHAR(255) COMMENT '联系人电话',
                            company_name VARCHAR(255) COMMENT '公司名称',
                            license_number VARCHAR(255) COMMENT '统一社会信用代码',
                            address VARCHAR(255) COMMENT '地址',
                            intro TEXT COMMENT '企业简介',
                            domain VARCHAR(255) COMMENT '域名',
                            remark TEXT COMMENT '备注',
                            status VARCHAR(10) COMMENT '状态 0：禁用 1：正常',
                            created_at DATETIME COMMENT '创建时间',
                            created_by BIGINT COMMENT '创建者',
                            updated_at DATETIME COMMENT '更新时间',
                            updated_by BIGINT COMMENT '更新者',
                            tenant_id BIGINT COMMENT '租户ID'
) COMMENT = '租户表';

-- ================================
-- 7. 用户部门关联表 sys_user_depts
-- ================================
CREATE TABLE sys_user_depts (
                                user_id BIGINT NOT NULL,
                                dept_id BIGINT NOT NULL,
                                PRIMARY KEY (user_id, dept_id)
) COMMENT = '用户部门关联表';

-- ================================
-- 8. 用户角色关联表 sys_user_roles
-- ================================
CREATE TABLE sys_user_roles (
                                user_id BIGINT NOT NULL,
                                role_id BIGINT NOT NULL,
                                PRIMARY KEY (user_id, role_id)
) COMMENT = '用户角色关联表';

-- ================================
-- 9. 用户表 sys_users
-- ================================
CREATE TABLE sys_users (
                           id BIGINT PRIMARY KEY COMMENT '主键',
                           username VARCHAR(255) COMMENT '用户名',
                           password VARCHAR(255) COMMENT '密码',
                           nickname VARCHAR(255) COMMENT '昵称',
                           avatar VARCHAR(255) COMMENT '头像',
                           email VARCHAR(255) COMMENT '邮箱',
                           phone VARCHAR(255) COMMENT '手机号',
                           sex INT COMMENT '性别 0：女 1：男',
                           status INT COMMENT '状态 0：禁用 1：正常',
                           created_at DATETIME COMMENT '创建时间',
                           created_by BIGINT COMMENT '创建者',
                           updated_at DATETIME COMMENT '更新时间',
                           updated_by BIGINT COMMENT '更新者',
                           tenant_id BIGINT COMMENT '租户ID'
) COMMENT = '用户表';

-- ================================
-- 10. 初始化默认租户
-- ================================
INSERT INTO sys_tenant (id, contact_user_name, contact_phone, company_name, license_number, address, intro, domain, remark, status, created_at, created_by, updated_at, updated_by, tenant_id)
VALUES (1, '系统管理员', '13800138000', '系统默认租户', '910000000000000000', '北京市朝阳区', '系统初始化默认租户', 'localhost', '系统默认租户', '1', NOW(), 1, NOW(), 1, 1);

-- ================================
-- 11. 初始化系统管理员角色
-- ================================
INSERT INTO sys_roles (id, role_name, role_code, role_desc, role_order, data_scope, status, created_at, created_by, updated_at, updated_by, tenant_id)
VALUES (1, '系统管理员', 'admin', '系统最高权限角色', 1, '1', 1, NOW(), 1, NOW(), 1, 1);

-- ================================
-- 12. 初始化系统管理员用户
-- ================================
INSERT INTO sys_users (id, username, password, nickname, avatar, email, phone, sex, status, created_at, created_by, updated_at, updated_by, tenant_id)
VALUES (1, 'admin', '$2a$10$xyz', '系统管理员', '', 'admin@example.com', '13800138000', 1, 1, NOW(), 1, NOW(), 1, 1);

-- ================================
-- 13. 关联管理员用户和角色
-- ================================
INSERT INTO sys_user_roles (user_id, role_id) VALUES (1, 1);

-- ================================
-- 14. 初始化系统菜单
-- ================================
INSERT INTO sys_menus (id, parent_id, menu_name, order_num, path, component, query_param, is_frame, icon, menu_type, visible, status, perms, remark, created_at, created_by, updated_at, updated_by, tenant_id) VALUES
                                                                                                                                                                                                                    (1, 0, '系统管理', 1, 'system', 'Layout', '', '1', 'system', 1, '0', '0', '', '系统管理目录', NOW(), 1, NOW(), 1, 1),
                                                                                                                                                                                                                    (2, 1, '用户管理', 1, 'user', 'system/user/index', '', '1', 'user', 2, '0', '0', 'system:user:list', '用户管理菜单', NOW(), 1, NOW(), 1, 1),
                                                                                                                                                                                                                    (3, 1, '角色管理', 2, 'role', 'system/role/index', '', '1', 'peoples', 2, '0', '0', 'system:role:list', '角色管理菜单', NOW(), 1, NOW(), 1, 1),
                                                                                                                                                                                                                    (4, 1, '菜单管理', 3, 'menu', 'system/menu/index', '', '1', 'tree-table', 2, '0', '0', 'system:menu:list', '菜单管理菜单', NOW(), 1, NOW(), 1, 1),
                                                                                                                                                                                                                    (5, 1, '部门管理', 4, 'dept', 'system/dept/index', '', '1', 'tree', 2, '0', '0', 'system:dept:list', '部门管理菜单', NOW(), 1, NOW(), 1, 1),
                                                                                                                                                                                                                    (6, 1, '租户管理', 5, 'tenant', 'system/tenant/index', '', '1', 'guide', 2, '0', '0', 'system:tenant:list', '租户管理菜单', NOW(), 1, NOW(), 1, 1);

-- ================================
-- 15. 初始化系统管理员角色菜单权限
-- ================================
INSERT INTO sys_role_menus (role_id, menu_id) VALUES
                                                  (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6);

-- ================================
-- 16. 初始化默认部门
-- ================================
INSERT INTO sys_depts (id, parent_id, dept_name, ancestors, order_num, status, leader, created_at, created_by, updated_at, updated_by, tenant_id)
VALUES (1, 0, '总部', '0', '1', '1', 1, NOW(), 1, NOW(), 1, 1);