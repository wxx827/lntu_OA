-- =====================================================
-- 考勤相关表 (请假、加班、出差)
-- =====================================================

-- 请假申请表
CREATE TABLE IF NOT EXISTS `OA_LEAVE_REQUEST` (
    `leave_id` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '请假ID',
    `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
    `leave_type` VARCHAR(20) NOT NULL COMMENT '请假类型: SICK/ANNUAL/PERSONAL/MARRIAGE/MATERNITY/PATERNITY/BEREAVEMENT',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `days` DECIMAL(4,1) NOT NULL COMMENT '请假天数',
    `reason` VARCHAR(500) NOT NULL COMMENT '请假原因',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING/APPROVED/REJECTED/CANCELLED',
    `approver_id` VARCHAR(32) COMMENT '审批人ID',
    `approve_time` DATETIME COMMENT '审批时间',
    `approve_comment` VARCHAR(500) COMMENT '审批意见',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';

-- 加班申请表
CREATE TABLE IF NOT EXISTS `OA_OVERTIME_REQUEST` (
    `overtime_id` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '加班ID',
    `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
    `overtime_date` DATE NOT NULL COMMENT '加班日期',
    `start_time` TIME NOT NULL COMMENT '开始时间',
    `end_time` TIME NOT NULL COMMENT '结束时间',
    `hours` DECIMAL(4,1) NOT NULL COMMENT '加班小时数',
    `reason` VARCHAR(500) NOT NULL COMMENT '加班原因',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING/APPROVED/REJECTED',
    `approver_id` VARCHAR(32) COMMENT '审批人ID',
    `approve_time` DATETIME COMMENT '审批时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='加班申请表';

-- 出差申请表
CREATE TABLE IF NOT EXISTS `OA_BUSINESS_TRIP` (
    `trip_id` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '出差ID',
    `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
    `destination` VARCHAR(200) NOT NULL COMMENT '目的地',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `end_date` DATE NOT NULL COMMENT '结束日期',
    `days` INT NOT NULL COMMENT '出差天数',
    `purpose` VARCHAR(500) NOT NULL COMMENT '出差目的',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING/APPROVED/REJECTED',
    `approver_id` VARCHAR(32) COMMENT '审批人ID',
    `approve_time` DATETIME COMMENT '审批时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出差申请表';
