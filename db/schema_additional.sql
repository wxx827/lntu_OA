-- =======================================================
-- 补充模块数据库脚本 (MySQL)
-- =======================================================

-- 1. 消息通知表
CREATE TABLE OA_NOTIFICATION (
    NOTIF_ID    VARCHAR(32) PRIMARY KEY,
    USER_ID     VARCHAR(32) NOT NULL,
    TYPE        VARCHAR(20),            -- 'system', 'workflow', 'mention'
    TITLE       VARCHAR(200) NOT NULL,
    CONTENT     TEXT,
    IS_READ     INT DEFAULT 0,          -- 0:未读, 1:已读
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_NOTIF_USER FOREIGN KEY (USER_ID) REFERENCES SYS_EMPLOYEE(EMP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 操作日志表
CREATE TABLE OA_ACTIVITY_LOG (
    LOG_ID      VARCHAR(32) PRIMARY KEY,
    USER_ID     VARCHAR(32) NOT NULL,
    ACTION      VARCHAR(100),
    DETAIL      TEXT,
    IP_ADDRESS  VARCHAR(50),
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_LOG_USER FOREIGN KEY (USER_ID) REFERENCES SYS_EMPLOYEE(EMP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 角色表
CREATE TABLE OA_ROLE (
    ROLE_ID     VARCHAR(32) PRIMARY KEY,
    ROLE_NAME   VARCHAR(50) NOT NULL,
    ROLE_DESC   VARCHAR(200),
    PERMISSIONS TEXT,  -- JSON格式存储权限ID数组
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 用户角色关联表
CREATE TABLE OA_USER_ROLE (
    USER_ID VARCHAR(32),
    ROLE_ID VARCHAR(32),
    PRIMARY KEY (USER_ID, ROLE_ID),
    CONSTRAINT FK_UR_USER FOREIGN KEY (USER_ID) REFERENCES SYS_EMPLOYEE(EMP_ID),
    CONSTRAINT FK_UR_ROLE FOREIGN KEY (ROLE_ID) REFERENCES OA_ROLE(ROLE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 修改工作流表,添加流程数据字段
ALTER TABLE OA_WORKFLOW ADD COLUMN FLOW_DATA TEXT COMMENT 'JSON格式存储表单数据';

-- =======================================================
-- 测试数据
-- =======================================================

-- 通知测试数据
INSERT INTO OA_NOTIFICATION VALUES 
('N001', 'E001', 'system', '系统维护通知', '系统将于今晚 24:00 进行停机维护，预计耗时 2 小时。', 0, NOW()),
('N002', 'E001', 'workflow', '审批通过提醒', '您的"年假申请"已被审批通过。', 0, NOW()),
('N003', 'E001', 'workflow', '新的待办任务', '您收到一个新的"采购申请"待审批任务。', 0, NOW());

-- 操作日志测试数据
INSERT INTO OA_ACTIVITY_LOG VALUES 
('L001', 'E001', '登录系统', 'IP: 192.168.1.100', '192.168.1.100', NOW()),
('L002', 'E001', '审批通过', '流程: WF20260101', '192.168.1.100', NOW());

-- 角色测试数据
INSERT INTO OA_ROLE VALUES 
('R001', '超级管理员', '拥有系统所有权限', '[1,2,3,4,5,21,22,31,32,41,42]', NOW()),
('R002', '部门经理', '审批流核心与部门管理', '[2,3,31]', NOW()),
('R003', '普通员工', '基础办公功能使用', '[2,3]', NOW());

-- 用户角色关联
INSERT INTO OA_USER_ROLE VALUES ('E001', 'R001');

COMMIT;
