-- 菜单表
CREATE TABLE sys_menus (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    path VARCHAR(255) NOT NULL COMMENT '菜单路径',
    name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    icon VARCHAR(100) COMMENT '菜单图标',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '0-禁用 1-启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT COMMENT '创建人ID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT COMMENT '修改人ID',
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 菜单层级关系表
CREATE TABLE sys_menu_relations (
    parent_id BIGINT NOT NULL,
    child_id BIGINT NOT NULL,
    sort INT DEFAULT 0 COMMENT '排序',
    PRIMARY KEY (parent_id, child_id),
    FOREIGN KEY (parent_id) REFERENCES sys_menus(id) ON DELETE CASCADE,
    FOREIGN KEY (child_id) REFERENCES sys_menus(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单层级关系表';

-- 初始化菜单数据
INSERT INTO sys_menus (path, name, icon, sort) VALUES 
('/dashboard', 'Dashboard', 'ion:grid-outline', 1),
('/dashboard/analysis', 'Analysis', NULL, 2);

INSERT INTO sys_menu_relations (parent_id, child_id, sort) VALUES (1, 2, 1);
