-- 示例数据表
CREATE TABLE sys_sample_table (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    age INT COMMENT '年龄',
    address VARCHAR(255) COMMENT '地址',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT COMMENT '创建人ID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT COMMENT '修改人ID',
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='示例数据表';

-- 初始化示例数据
INSERT INTO sys_sample_table (name, age, address) VALUES
('John Brown', 32, 'New York No. 1 Lake Park'),
('Jim Green', 42, 'London No. 1 Lake Park'),
('Joe Black', 28, 'Sidney No. 1 Lake Park');
