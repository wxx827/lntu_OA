-- =======================================================
-- 车辆管理模块数据库脚本
-- =======================================================

CREATE TABLE OA_CAR (
    CAR_ID      VARCHAR(32) PRIMARY KEY,
    CAR_LICENCE VARCHAR(20) NOT NULL COMMENT '车牌号',
    CAR_TYPE    VARCHAR(50) COMMENT '车辆类型',
    BRAND       VARCHAR(50) COMMENT '品牌',
    SEATS       INT COMMENT '座位数',
    STATE       INT DEFAULT 0 COMMENT '0:空闲 1:使用中 2:维修中',
    REMARK      VARCHAR(200) COMMENT '备注',
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 测试数据
INSERT INTO OA_CAR (CAR_ID, CAR_LICENCE, CAR_TYPE, BRAND, SEATS, STATE, CREATE_TIME) VALUES 
('C001', '京A88888', '轿车', '奥迪A6', 5, 0, NOW()),
('C002', '京A66666', '商务车', '别克GL8', 7, 1, NOW()),
('C003', '京A12345', '大巴', '宇通', 45, 0, NOW());

COMMIT;
