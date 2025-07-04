-- 部门表
CREATE TABLE sys_departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    parent_id BIGINT COMMENT '父部门ID',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '0-禁用 1-启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES sys_departments(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 系统菜单表
CREATE TABLE sys_system_menus (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    path VARCHAR(255) COMMENT '菜单路径',
    permission VARCHAR(100) COMMENT '权限标识',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '0-禁用 1-启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT COMMENT '创建人ID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT COMMENT '修改人ID',
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- 初始化部门数据
INSERT INTO sys_departments (name, parent_id, sort) VALUES 
('总公司', NULL, 1),
('技术部', 1, 2),
('市场部', 1, 3);

-- 权限表
CREATE TABLE sys_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '权限编码',
    name VARCHAR(50) NOT NULL COMMENT '权限名称',
    description VARCHAR(255) COMMENT '权限描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT COMMENT '创建人ID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT COMMENT '修改人ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关联表
CREATE TABLE sys_role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 初始化权限数据
INSERT INTO sys_permissions (code, name, description) VALUES
('system:user', '用户管理', '用户管理权限'),
('system:role', '角色管理', '角色管理权限'), 
('system:menu', '菜单管理', '菜单管理权限'),
('system:dept', '部门管理', '部门管理权限');

-- 初始化角色权限数据
INSERT INTO sys_role_permissions (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4);

-- 部门表添加路径字段
ALTER TABLE sys_departments ADD COLUMN dept_path VARCHAR(255) COMMENT '部门路径(如1.2.3)';

-- 更新部门路径数据
UPDATE sys_departments SET dept_path = id WHERE parent_id IS NULL;
UPDATE sys_departments d 
JOIN sys_departments p ON d.parent_id = p.id
SET d.dept_path = CONCAT(p.dept_path, '.', d.id);

-- 角色数据范围表
CREATE TABLE sys_role_data_scopes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL COMMENT '角色ID',
    scope_type ENUM('OWN_DEPT','OWN_DEPT_AND_SUB','CUSTOM_DEPTS','ALL') NOT NULL,
    scope_value VARCHAR(255) COMMENT '部门ID集合(逗号分隔，仅CUSTOM_DEPTS需要)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES sys_roles(id) ON DELETE CASCADE
) COMMENT='角色数据范围表';

-- 初始化数据范围规则
INSERT INTO sys_role_data_scopes (role_id, scope_type) VALUES
(1, 'ALL'); -- 管理员拥有全部数据权限

-- 初始化系统菜单数据
INSERT INTO sys_system_menus (name, path, permission, sort) VALUES
('用户管理', '/system/user', 'system:user', 1),
('角色管理', '/system/role', 'system:role', 2),
('菜单管理', '/system/menu', 'system:menu', 3),
('部门管理', '/system/dept', 'system:dept', 4),
('数据权限', '/system/data-scope', 'system:data-scope', 5);
