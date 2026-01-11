-- =======================================================
-- Cloud File Management Database Schema (MySQL)
-- =======================================================

-- Cloud Disk File Table
CREATE TABLE OA_DISK_FILE (
    FILE_ID         VARCHAR(32) PRIMARY KEY,
    FILE_NAME       VARCHAR(200) NOT NULL,
    FILE_TYPE       VARCHAR(20) NOT NULL,      -- 'file' or 'folder'
    FILE_SIZE       BIGINT DEFAULT 0,          -- File size in bytes (0 for folders)
    PARENT_ID       VARCHAR(32),               -- NULL for root, 'ROOT' for top-level
    OWNER_ID        VARCHAR(32) NOT NULL,
    FILE_PATH       VARCHAR(500),              -- Full path for quick access
    MIME_TYPE       VARCHAR(100),              -- e.g., 'application/pdf', 'image/png'
    IS_DELETED      INT DEFAULT 0,             -- 0:Active, 1:Deleted (soft delete)
    CREATE_TIME     DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_DISK_OWNER FOREIGN KEY (OWNER_ID) REFERENCES SYS_EMPLOYEE(EMP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create index for faster queries
CREATE INDEX IDX_DISK_PARENT ON OA_DISK_FILE(PARENT_ID);
CREATE INDEX IDX_DISK_OWNER ON OA_DISK_FILE(OWNER_ID);
CREATE INDEX IDX_DISK_TYPE ON OA_DISK_FILE(FILE_TYPE);

-- =======================================================
-- Sample Data
-- =======================================================

-- Root folders for admin user
INSERT INTO OA_DISK_FILE VALUES 
('DF001', '我的文档', 'folder', 0, 'ROOT', 'E001', '/我的文档', NULL, 0, NOW(), NOW()),
('DF002', '工作资料', 'folder', 0, 'ROOT', 'E001', '/工作资料', NULL, 0, NOW(), NOW()),
('DF003', '共享文件', 'folder', 0, 'ROOT', 'E001', '/共享文件', NULL, 0, NOW(), NOW());

-- Sample files in folders
INSERT INTO OA_DISK_FILE VALUES 
('DF004', '2026年度计划.docx', 'file', 51200, 'DF001', 'E001', '/我的文档/2026年度计划.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 0, NOW(), NOW()),
('DF005', '会议记录.pdf', 'file', 102400, 'DF002', 'E001', '/工作资料/会议记录.pdf', 'application/pdf', 0, NOW(), NOW()),
('DF006', '项目文档', 'folder', 0, 'DF002', 'E001', '/工作资料/项目文档', NULL, 0, NOW(), NOW()),
('DF007', '需求分析.xlsx', 'file', 76800, 'DF006', 'E001', '/工作资料/项目文档/需求分析.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 0, NOW(), NOW());

COMMIT;
