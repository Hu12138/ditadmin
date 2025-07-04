-- 文件表
CREATE TABLE sys_files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL COMMENT '文件名',
    url VARCHAR(255) NOT NULL COMMENT '文件URL',
    size BIGINT COMMENT '文件大小(字节)',
    type VARCHAR(100) COMMENT '文件类型',
    user_id BIGINT COMMENT '上传用户ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT COMMENT '创建人ID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT COMMENT '修改人ID',
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- 初始化文件上传示例数据
INSERT INTO sys_files (name, url, size, type, user_id) VALUES
('example.jpg', '/uploads/example.jpg', 102400, 'image/jpeg', 1);
