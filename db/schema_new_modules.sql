-- =======================================================
-- 新增模块数据库脚本 (MySQL)
-- =======================================================

-- 1. 文档管理表
CREATE TABLE OA_DOCUMENT (
    DOC_ID      VARCHAR(32) PRIMARY KEY,
    DOC_NAME    VARCHAR(200) NOT NULL,
    DOC_TYPE    VARCHAR(20),  -- 'folder', 'doc', 'image', 'video'
    FILE_SIZE   BIGINT,       -- 文件大小(字节)
    FILE_PATH   VARCHAR(500), -- 存储路径
    PARENT_ID   VARCHAR(32),  -- 父文件夹ID
    UPLOADER_ID VARCHAR(32),
    UPLOAD_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_DOC_UPLOADER FOREIGN KEY (UPLOADER_ID) REFERENCES SYS_EMPLOYEE(EMP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 工作流表
CREATE TABLE OA_WORKFLOW (
    FLOW_ID     VARCHAR(32) PRIMARY KEY,
    FLOW_TYPE   VARCHAR(50) NOT NULL,  -- '采购流程', '请假流程', '报销流程'
    TITLE       VARCHAR(200) NOT NULL,
    INITIATOR_ID VARCHAR(32) NOT NULL,
    CURRENT_APPROVER_ID VARCHAR(32),   -- 当前审批人
    STATUS      INT DEFAULT 0,          -- 0:待审批, 1:已通过, 2:已驳回
    PRIORITY    VARCHAR(10),            -- '高', '中', '低'
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_FLOW_INITIATOR FOREIGN KEY (INITIATOR_ID) REFERENCES SYS_EMPLOYEE(EMP_ID),
    CONSTRAINT FK_FLOW_APPROVER FOREIGN KEY (CURRENT_APPROVER_ID) REFERENCES SYS_EMPLOYEE(EMP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 工作流审批日志表
CREATE TABLE OA_WORKFLOW_LOG (
    LOG_ID      VARCHAR(32) PRIMARY KEY,
    FLOW_ID     VARCHAR(32) NOT NULL,
    APPROVER_ID VARCHAR(32) NOT NULL,
    ACTION      VARCHAR(20),  -- 'pass', 'reject'
    COMMENT     TEXT,
    ACTION_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_LOG_FLOW FOREIGN KEY (FLOW_ID) REFERENCES OA_WORKFLOW(FLOW_ID),
    CONSTRAINT FK_LOG_APPROVER FOREIGN KEY (APPROVER_ID) REFERENCES SYS_EMPLOYEE(EMP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================
-- 测试数据
-- =======================================================

-- 文档测试数据
INSERT INTO OA_DOCUMENT VALUES 
('DOC001', '后端接口文档V4.0.docx', 'doc', 2516582, '/uploads/docs/api-v4.docx', NULL, 'E001', NOW(), NOW()),
('DOC002', '前端UI切图资源', 'folder', NULL, NULL, NULL, 'E001', NOW(), NOW()),
('DOC003', '项目演示视频.mp4', 'video', 134217728, '/uploads/videos/demo.mp4', NULL, 'E001', NOW(), NOW()),
('DOC004', 'logo-brand.png', 'image', 55296, '/uploads/images/logo.png', NULL, 'E001', NOW(), NOW());

-- 工作流测试数据
INSERT INTO OA_WORKFLOW VALUES 
('WF20260101', '采购流程', 'IT设备采购申请_张三', 'E001', 'E001', 0, '高', NOW(), NOW()),
('WF20260102', '请假流程', '年假申请_李四_2026春节', 'E001', 'E001', 0, '中', NOW(), NOW()),
('WF20260103', '报销流程', '部门经费报销_Q4团建', 'E001', 'E001', 0, '低', NOW(), NOW());

COMMIT;
