-- =======================================================
-- Online Message (Internal Email) Database Schema (MySQL)
-- =======================================================

-- Internal Message Table
CREATE TABLE OA_MESSAGE (
    MSG_ID                  VARCHAR(32) PRIMARY KEY,
    SENDER_ID               VARCHAR(32) NOT NULL,
    RECEIVER_ID             VARCHAR(32) NOT NULL,
    SUBJECT                 VARCHAR(200) NOT NULL,
    CONTENT                 TEXT,
    IS_READ                 INT DEFAULT 0,             -- 0:Unread, 1:Read
    IS_DELETED_BY_SENDER    INT DEFAULT 0,             -- 0:Active, 1:Deleted by sender
    IS_DELETED_BY_RECEIVER  INT DEFAULT 0,             -- 0:Active, 1:Deleted by receiver
    SEND_TIME               DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_MSG_SENDER FOREIGN KEY (SENDER_ID) REFERENCES SYS_EMPLOYEE(EMP_ID),
    CONSTRAINT FK_MSG_RECEIVER FOREIGN KEY (RECEIVER_ID) REFERENCES SYS_EMPLOYEE(EMP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create indexes for faster queries
CREATE INDEX IDX_MSG_SENDER ON OA_MESSAGE(SENDER_ID);
CREATE INDEX IDX_MSG_RECEIVER ON OA_MESSAGE(RECEIVER_ID);
CREATE INDEX IDX_MSG_READ ON OA_MESSAGE(IS_READ);
CREATE INDEX IDX_MSG_TIME ON OA_MESSAGE(SEND_TIME);

-- =======================================================
-- Sample Data
-- =======================================================

-- Sample messages (assuming E001 is admin, we'll create some test messages)
INSERT INTO OA_MESSAGE VALUES 
('MSG001', 'E001', 'E001', '欢迎使用内部邮件系统', '这是一条测试消息。您可以使用此系统与同事进行沟通。', 1, 0, 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
('MSG002', 'E001', 'E001', '会议通知', '请于明天上午10:00参加部门例会，地点：会议室A。', 0, 0, 0, DATE_SUB(NOW(), INTERVAL 1 DAY)),
('MSG003', 'E001', 'E001', '项目进度汇报', '本周项目进度：\n1. 需求分析已完成\n2. 数据库设计进行中\n3. 预计下周开始编码', 0, 0, 0, NOW());

COMMIT;
