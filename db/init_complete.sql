-- =======================================================
-- OA System Complete Database Initialization Script
-- MySQL 8.0 Compatible
-- Version: 5.0 FINAL
-- =======================================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS oa_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE oa_system;

-- =======================================================
-- 1. 核心表 - 部门、职位、员工
-- =======================================================

CREATE TABLE IF NOT EXISTS SYS_DEPARTMENT (
    DEP_ID      VARCHAR(32) PRIMARY KEY,
    DEP_NAME    VARCHAR(100) NOT NULL,
    DEP_DESC    VARCHAR(200)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS SYS_POSITION (
    POS_ID      VARCHAR(32) PRIMARY KEY,
    POS_NAME    VARCHAR(50) NOT NULL,
    POS_LEVEL   INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS SYS_EMPLOYEE (
    EMP_ID      VARCHAR(32) PRIMARY KEY,
    EMP_NAME    VARCHAR(50) NOT NULL,
    PASSWORD    VARCHAR(100) NOT NULL,
    ROLE        VARCHAR(20) DEFAULT 'EMPLOYEE',
    SEX         VARCHAR(10),
    TEL         VARCHAR(20),
    EMAIL       VARCHAR(50),
    DEP_ID      VARCHAR(32),
    POS_ID      VARCHAR(32)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 2. 会议管理
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_MEETING_ROOM (
    ROOM_ID     VARCHAR(32) PRIMARY KEY,
    ROOM_NAME   VARCHAR(100) NOT NULL,
    LOCATION    VARCHAR(100),
    CAPACITY    INT,
    HAS_MEDIA   INT DEFAULT 0,
    STATUS      INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS OA_MEETING_BOOK (
    BOOK_ID     VARCHAR(32) PRIMARY KEY,
    ROOM_ID     VARCHAR(32) NOT NULL,
    EMP_ID      VARCHAR(32) NOT NULL,
    START_TIME  DATETIME NOT NULL,
    END_TIME    DATETIME NOT NULL,
    TOPIC       VARCHAR(200),
    STATUS      INT DEFAULT 0,
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 3. 物资管理
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_MATERIAL (
    MAT_ID      VARCHAR(32) PRIMARY KEY,
    MAT_NAME    VARCHAR(100) NOT NULL,
    SPEC        VARCHAR(50),
    PRICE       DECIMAL(10, 2),
    STOCK       INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS OA_MAT_APPLY (
    APPLY_ID    VARCHAR(32) PRIMARY KEY,
    MAT_ID      VARCHAR(32) NOT NULL,
    EMP_ID      VARCHAR(32) NOT NULL,
    COUNT       INT NOT NULL,
    REASON      VARCHAR(200),
    APPLY_DATE  DATETIME DEFAULT CURRENT_TIMESTAMP,
    STATE       INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 4. 车辆管理
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_CAR (
    CAR_ID          VARCHAR(32) PRIMARY KEY,
    CAR_LICENCE     VARCHAR(20),
    CAR_TYPE        VARCHAR(50),
    BRAND           VARCHAR(50),
    SEATS           INT DEFAULT 5,
    STATE           INT DEFAULT 0,
    MILEAGE         INT DEFAULT 0,
    REMARK          VARCHAR(200),
    CREATE_TIME     DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 5. 流程审批
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_WORKFLOW (
    FLOW_ID             VARCHAR(32) PRIMARY KEY,
    FLOW_TYPE           VARCHAR(50),
    TITLE               VARCHAR(200),
    INITIATOR_ID        VARCHAR(32),
    CURRENT_APPROVER_ID VARCHAR(32),
    STATUS              INT DEFAULT 0,
    PRIORITY            VARCHAR(20),
    CREATE_TIME         DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FLOW_DATA           TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS OA_WORKFLOW_LOG (
    LOG_ID      VARCHAR(32) PRIMARY KEY,
    FLOW_ID     VARCHAR(32),
    APPROVER_ID VARCHAR(32),
    ACTION      VARCHAR(20),
    COMMENT     VARCHAR(500),
    ACTION_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 6. 通知公告
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_NOTIFICATION (
    NOTIF_ID    VARCHAR(32) PRIMARY KEY,
    USER_ID     VARCHAR(32) NOT NULL,
    TYPE        VARCHAR(20),
    TITLE       VARCHAR(200) NOT NULL,
    CONTENT     TEXT,
    IS_READ     INT DEFAULT 0,
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS OA_ANNOUNCEMENT (
    ID              VARCHAR(32) PRIMARY KEY,
    TITLE           VARCHAR(200) NOT NULL,
    CONTENT         TEXT,
    TYPE            VARCHAR(20) DEFAULT 'normal',
    PUBLISHER_ID    VARCHAR(32),
    PUBLISHER_NAME  VARCHAR(50),
    PUBLISH_TIME    DATETIME DEFAULT CURRENT_TIMESTAMP,
    STATUS          INT DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 7. 项目管理
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_PROJECT (
    PROJECT_ID  VARCHAR(32) PRIMARY KEY,
    NAME        VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(500),
    PROGRESS    INT DEFAULT 0,
    STATUS      VARCHAR(20) DEFAULT '进行中',
    COLOR       VARCHAR(20) DEFAULT '#409eff',
    ICON        VARCHAR(50) DEFAULT 'Platform',
    DUE_DATE    DATE,
    CREATOR_ID  VARCHAR(32),
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 8. 角色权限
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_ROLE (
    ROLE_ID     VARCHAR(32) PRIMARY KEY,
    ROLE_NAME   VARCHAR(50) NOT NULL,
    ROLE_DESC   VARCHAR(200),
    PERMISSIONS TEXT,
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 9. 云盘文件
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_DISK_FILE (
    ID          VARCHAR(32) PRIMARY KEY,
    NAME        VARCHAR(200) NOT NULL,
    IS_DIR      TINYINT DEFAULT 0,
    PARENT_ID   VARCHAR(32) DEFAULT 'root',
    USER_ID     VARCHAR(32),
    SIZE        BIGINT DEFAULT 0,
    FILE_PATH   VARCHAR(500),
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 10. 站内信
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_MESSAGE (
    ID          VARCHAR(32) PRIMARY KEY,
    SENDER_ID   VARCHAR(32),
    RECEIVER_ID VARCHAR(32),
    SUBJECT     VARCHAR(200),
    CONTENT     TEXT,
    IS_READ     TINYINT DEFAULT 0,
    BOX_TYPE    VARCHAR(20) DEFAULT 'inbox',
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 11. 费用报销
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_EXPENSE_TYPE (
    TYPE_ID     INT PRIMARY KEY AUTO_INCREMENT,
    TYPE_NAME   VARCHAR(50) NOT NULL,
    DESCRIPTION VARCHAR(200),
    STATUS      INT DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS OA_EXPENSE_CLAIM (
    CLAIM_ID        VARCHAR(32) PRIMARY KEY,
    USER_ID         VARCHAR(32) NOT NULL,
    EXPENSE_TYPE_ID INT,
    TOTAL_AMOUNT    DECIMAL(12, 2) NOT NULL,
    DESCRIPTION     VARCHAR(500),
    PROJECT_ID      VARCHAR(32),
    STATUS          INT DEFAULT 0,
    CREATE_TIME     DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS OA_EXPENSE_INVOICE (
    INVOICE_ID      VARCHAR(32) PRIMARY KEY,
    CLAIM_ID        VARCHAR(32) NOT NULL,
    INVOICE_NO      VARCHAR(50),
    INVOICE_TYPE    VARCHAR(20),
    AMOUNT          DECIMAL(12, 2),
    INVOICE_DATE    DATE,
    COMPANY_NAME    VARCHAR(200),
    CREATE_TIME     DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 12. 动态消息
-- =======================================================

CREATE TABLE IF NOT EXISTS OA_ACTIVITY (
    ID          VARCHAR(32) PRIMARY KEY,
    TITLE       VARCHAR(200) NOT NULL,
    DESCRIPTION TEXT,
    TYPE        VARCHAR(20) DEFAULT 'primary',
    USER_ID     VARCHAR(32),
    USER_NAME   VARCHAR(50),
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 初始数据
-- =======================================================

-- 部门
INSERT IGNORE INTO SYS_DEPARTMENT VALUES ('D01', '技术部', '技术研发中心');
INSERT IGNORE INTO SYS_DEPARTMENT VALUES ('D02', '人事部', '人力资源管理');
INSERT IGNORE INTO SYS_DEPARTMENT VALUES ('D03', '财务部', '财务与审计');
INSERT IGNORE INTO SYS_DEPARTMENT VALUES ('D04', '市场部', '市场营销推广');

-- 职位
INSERT IGNORE INTO SYS_POSITION VALUES ('P01', '经理', 10);
INSERT IGNORE INTO SYS_POSITION VALUES ('P02', '主管', 5);
INSERT IGNORE INTO SYS_POSITION VALUES ('P03', '员工', 1);

-- 员工 (密码: 123456 的MD5)
INSERT IGNORE INTO SYS_EMPLOYEE VALUES ('E001', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'ADMIN', '男', '13800138000', 'admin@oa.com', 'D01', 'P01');
INSERT IGNORE INTO SYS_EMPLOYEE VALUES ('E002', 'manager', 'e10adc3949ba59abbe56e057f20f883e', 'MANAGER', '男', '13800138001', 'manager@oa.com', 'D01', 'P01');
INSERT IGNORE INTO SYS_EMPLOYEE VALUES ('E003', 'staff01', 'e10adc3949ba59abbe56e057f20f883e', 'EMPLOYEE', '女', '13800138002', 'staff01@oa.com', 'D02', 'P03');

-- 会议室
INSERT IGNORE INTO OA_MEETING_ROOM VALUES ('R301', '大会议室301', '3楼', 20, 1, 0);
INSERT IGNORE INTO OA_MEETING_ROOM VALUES ('R202', '小会议室202', '2楼', 8, 1, 0);
INSERT IGNORE INTO OA_MEETING_ROOM VALUES ('R405', '培训室405', '4楼', 30, 1, 0);

-- 物资
INSERT IGNORE INTO OA_MATERIAL VALUES ('M001', 'A4打印纸', '500张/包', 25.00, 100);
INSERT IGNORE INTO OA_MATERIAL VALUES ('M002', '黑色签字笔', '0.5mm', 2.50, 200);
INSERT IGNORE INTO OA_MATERIAL VALUES ('M003', '订书机', '标准型', 18.00, 50);
INSERT IGNORE INTO OA_MATERIAL VALUES ('M004', '笔记本', 'A5', 6.50, 80);
INSERT IGNORE INTO OA_MATERIAL VALUES ('M005', '文件夹', 'A4', 4.00, 150);

-- 车辆
INSERT IGNORE INTO OA_CAR VALUES ('C001', '京A88888', '轿车', '奥迪A6', 5, 0, 50000, '公务用车', NOW());
INSERT IGNORE INTO OA_CAR VALUES ('C002', '京A66666', '商务车', '别克GL8', 7, 0, 30000, '接待用车', NOW());

-- 项目
INSERT IGNORE INTO OA_PROJECT VALUES ('P001', '前端界面优化', 'Vue3布局与样式调整', 85, '进行中', '#409eff', 'Platform', '2026-01-20', 'E001', NOW(), NOW());
INSERT IGNORE INTO OA_PROJECT VALUES ('P002', '后端接口开发', '服务清理与数据对接', 30, '开发中', '#e6a23c', 'Box', '2026-02-15', 'E001', NOW(), NOW());
INSERT IGNORE INTO OA_PROJECT VALUES ('P003', 'AI功能试点', '模型集成测试', 10, '已计划', '#f56c6c', 'DataAnalysis', '2026-03-01', 'E001', NOW(), NOW());

-- 公告
INSERT IGNORE INTO OA_ANNOUNCEMENT VALUES ('A001', '系统上线通知', '企业OA系统正式上线，欢迎使用！', 'important', 'E001', 'admin', NOW(), 1);
INSERT IGNORE INTO OA_ANNOUNCEMENT VALUES ('A002', '春节放假通知', '2026年春节假期安排：1月28日至2月4日放假。', 'normal', 'E001', 'admin', NOW(), 1);

-- 角色
INSERT IGNORE INTO OA_ROLE VALUES ('R001', '超级管理员', '拥有系统所有权限', '[1,2,3,4,5,21,22,31,32,41,42]', NOW());
INSERT IGNORE INTO OA_ROLE VALUES ('R002', '部门经理', '审批流核心与部门管理', '[2,3,31]', NOW());
INSERT IGNORE INTO OA_ROLE VALUES ('R003', '普通员工', '基础办公功能使用', '[2,3]', NOW());

-- 通知
INSERT IGNORE INTO OA_NOTIFICATION VALUES ('N001', 'E001', 'system', '欢迎使用OA系统', '您的账号已创建成功，祝您工作愉快！', 0, NOW());

-- 费用类型
INSERT IGNORE INTO OA_EXPENSE_TYPE (TYPE_ID, TYPE_NAME, DESCRIPTION) VALUES (1, '差旅费', '出差交通、住宿、餐饮等费用');
INSERT IGNORE INTO OA_EXPENSE_TYPE (TYPE_ID, TYPE_NAME, DESCRIPTION) VALUES (2, '招待费', '业务招待、客户接待等费用');
INSERT IGNORE INTO OA_EXPENSE_TYPE (TYPE_ID, TYPE_NAME, DESCRIPTION) VALUES (3, '办公费', '办公用品、耗材等费用');
INSERT IGNORE INTO OA_EXPENSE_TYPE (TYPE_ID, TYPE_NAME, DESCRIPTION) VALUES (4, '通讯费', '电话、网络等通讯费用');
INSERT IGNORE INTO OA_EXPENSE_TYPE (TYPE_ID, TYPE_NAME, DESCRIPTION) VALUES (5, '培训费', '员工培训、学习等费用');
INSERT IGNORE INTO OA_EXPENSE_TYPE (TYPE_ID, TYPE_NAME, DESCRIPTION) VALUES (6, '其他', '其他类型费用');

-- 动态消息
INSERT IGNORE INTO OA_ACTIVITY VALUES ('ACT001', '系统更新', '已升级至新版企业主题，界面更加美观', 'primary', 'E001', 'admin', NOW());
INSERT IGNORE INTO OA_ACTIVITY VALUES ('ACT002', '会议通知', '产品评审会将于今日14:00在301会议室召开', 'success', 'E001', 'admin', DATE_SUB(NOW(), INTERVAL 1 DAY));
INSERT IGNORE INTO OA_ACTIVITY VALUES ('ACT003', '新员工入职', '欢迎张三加入技术部，请大家多多关照', 'warning', 'E002', 'manager', DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 流程审批测试数据
INSERT IGNORE INTO OA_WORKFLOW VALUES ('WF001', 'leave', '年假申请-春节假期', 'E001', 'E002', 0, 'medium', DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), '{"days":5,"reason":"春节回家探亲"}');
INSERT IGNORE INTO OA_WORKFLOW VALUES ('WF002', 'expense', '差旅报销-北京出差', 'E001', 'E002', 1, 'high', DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), '{"amount":3500,"project":"客户拜访"}');
INSERT IGNORE INTO OA_WORKFLOW VALUES ('WF003', 'purchase', '办公用品采购申请', 'E003', 'E002', 0, 'low', DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), '{"items":"打印纸、签字笔","budget":500}');
INSERT IGNORE INTO OA_WORKFLOW VALUES ('WF004', 'leave', '病假申请', 'E003', 'E002', 2, 'high', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), '{"days":2,"reason":"感冒发烧"}');

-- 物资申请测试数据
INSERT IGNORE INTO OA_MAT_APPLY VALUES ('MA001', 'M001', 'E001', 5, '部门打印需要', NOW(), 1);
INSERT IGNORE INTO OA_MAT_APPLY VALUES ('MA002', 'M002', 'E001', 10, '新员工入职配发', DATE_SUB(NOW(), INTERVAL 2 DAY), 0);
INSERT IGNORE INTO OA_MAT_APPLY VALUES ('MA003', 'M004', 'E003', 3, '会议记录使用', DATE_SUB(NOW(), INTERVAL 1 DAY), 0);

COMMIT;

-- =======================================================
-- 完成提示
-- =======================================================
SELECT '数据库初始化完成！' AS Message;
SELECT CONCAT('员工数量: ', COUNT(*)) AS Info FROM SYS_EMPLOYEE;
SELECT CONCAT('会议室数量: ', COUNT(*)) AS Info FROM OA_MEETING_ROOM;
SELECT CONCAT('物资种类: ', COUNT(*)) AS Info FROM OA_MATERIAL;
