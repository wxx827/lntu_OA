-- =======================================================
-- OA System Configuration Table (MySQL)
-- System Configuration Table
-- =======================================================

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS SYS_CONFIG (
    CONFIG_ID     VARCHAR(32) PRIMARY KEY,
    CONFIG_KEY    VARCHAR(100) NOT NULL UNIQUE COMMENT 'Config Key',
    CONFIG_VALUE  VARCHAR(500) COMMENT 'Config Value',
    CONFIG_TYPE   VARCHAR(20) DEFAULT 'STRING' COMMENT 'Type: STRING, INTEGER, BOOLEAN',
    CONFIG_GROUP  VARCHAR(50) COMMENT 'Group: jwt, attendance, system',
    DESCRIPTION   VARCHAR(200) COMMENT 'Description',
    CREATE_TIME   DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX IDX_CONFIG_KEY (CONFIG_KEY),
    INDEX IDX_CONFIG_GROUP (CONFIG_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Configuration Table';

-- =======================================================
-- Initialize Configuration Data
-- =======================================================

-- JWT Configuration
INSERT INTO SYS_CONFIG (CONFIG_ID, CONFIG_KEY, CONFIG_VALUE, CONFIG_TYPE, CONFIG_GROUP, DESCRIPTION) VALUES
('CFG001', 'jwt.secret', 'oa_secure_jwt_secret_key_2026_random_string', 'STRING', 'jwt', 'JWT Signing Key'),
('CFG002', 'jwt.expiration', '86400000', 'INTEGER', 'jwt', 'JWT Expiration Time (ms), default 24 hours');

-- Attendance Configuration
INSERT INTO SYS_CONFIG (CONFIG_ID, CONFIG_KEY, CONFIG_VALUE, CONFIG_TYPE, CONFIG_GROUP, DESCRIPTION) VALUES
('CFG003', 'attendance.work.start', '09:00:00', 'STRING', 'attendance', 'Work Start Time'),
('CFG004', 'attendance.work.end', '18:00:00', 'STRING', 'attendance', 'Work End Time'),
('CFG005', 'attendance.late.threshold', '15', 'INTEGER', 'attendance', 'Late Threshold (minutes)'),
('CFG006', 'attendance.early.threshold', '30', 'INTEGER', 'attendance', 'Early Leave Threshold (minutes)');

-- System Configuration
INSERT INTO SYS_CONFIG (CONFIG_ID, CONFIG_KEY, CONFIG_VALUE, CONFIG_TYPE, CONFIG_GROUP, DESCRIPTION) VALUES
('CFG007', 'upload.path', 'uploads/documents/', 'STRING', 'system', 'File Upload Path'),
('CFG008', 'upload.max.size', '104857600', 'INTEGER', 'system', 'Max Upload Size (bytes), default 100MB'),
('CFG009', 'system.name', 'OA System', 'STRING', 'system', 'System Name'),
('CFG010', 'system.version', '1.0.0', 'STRING', 'system', 'System Version');

COMMIT;
