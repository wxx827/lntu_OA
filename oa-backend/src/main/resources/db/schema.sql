-- Database Schema for OA System V4.0

-- ----------------------------
-- Table structure for SYS_DEPARTMENT
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `dep_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dep_name` varchar(100) NOT NULL COMMENT 'Department Name',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'Parent Department ID',
  PRIMARY KEY (`dep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Department Table';

-- ----------------------------
-- Table structure for SYS_POSITION
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position` (
  `pos_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pos_name` varchar(100) NOT NULL COMMENT 'Position Name',
  PRIMARY KEY (`pos_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Position Table';

-- ----------------------------
-- Table structure for SYS_EMPLOYEE
-- ----------------------------
DROP TABLE IF EXISTS `sys_employee`;
CREATE TABLE `sys_employee` (
  `emp_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `emp_name` varchar(50) NOT NULL COMMENT 'Employee Name',
  `password` varchar(100) NOT NULL COMMENT 'Password (MD5)',
  `dep_id` bigint(20) DEFAULT NULL,
  `pos_id` bigint(20) DEFAULT NULL,
  `tel` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `sex` int(1) DEFAULT NULL COMMENT '0: Female, 1: Male',
  `status` int(1) DEFAULT 1 COMMENT '1: Normal, 0: Disabled',
  PRIMARY KEY (`emp_id`),
  FOREIGN KEY (`dep_id`) REFERENCES `sys_department` (`dep_id`),
  FOREIGN KEY (`pos_id`) REFERENCES `sys_position` (`pos_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Employee Table';

-- ----------------------------
-- Table structure for OA_MEETING_ROOM
-- ----------------------------
DROP TABLE IF EXISTS `oa_meeting_room`;
CREATE TABLE `oa_meeting_room` (
  `room_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_name` varchar(100) NOT NULL,
  `capacity` int(11) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL COMMENT 'Floor/Location',
  `equipment` varchar(500) DEFAULT NULL COMMENT 'JSON or comma separated equipment list',
  `status` int(1) DEFAULT 0 COMMENT '0: Free, 1: Occupied',
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Meeting Room Table';

-- ----------------------------
-- Table structure for OA_MEETING_BOOK
-- ----------------------------
DROP TABLE IF EXISTS `oa_meeting_book`;
CREATE TABLE `oa_meeting_book` (
  `book_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_id` bigint(20) NOT NULL,
  `emp_id` bigint(20) NOT NULL,
  `subject` varchar(200) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `status` int(1) DEFAULT 0 COMMENT '0: Pending, 1: Approved, 2: Rejected',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`book_id`),
  FOREIGN KEY (`room_id`) REFERENCES `oa_meeting_room` (`room_id`),
  FOREIGN KEY (`emp_id`) REFERENCES `sys_employee` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Meeting Booking Table';

-- ----------------------------
-- Table structure for OA_MATERIAL
-- ----------------------------
DROP TABLE IF EXISTS `oa_material`;
CREATE TABLE `oa_material` (
  `mat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mat_name` varchar(100) NOT NULL,
  `spec` varchar(100) DEFAULT NULL COMMENT 'Specification',
  `price` decimal(10,2) DEFAULT NULL,
  `stock` int(11) DEFAULT 0,
  PRIMARY KEY (`mat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Material Table';

-- ----------------------------
-- Table structure for OA_MAT_APPLY
-- ----------------------------
DROP TABLE IF EXISTS `oa_mat_apply`;
CREATE TABLE `oa_mat_apply` (
  `apply_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mat_id` bigint(20) NOT NULL,
  `emp_id` bigint(20) NOT NULL,
  `count` int(11) NOT NULL,
  `reason` varchar(500) DEFAULT NULL,
  `state` int(1) DEFAULT 0 COMMENT '0: Pending, 1: Approved (Received), 2: Rejected',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`apply_id`),
  FOREIGN KEY (`mat_id`) REFERENCES `oa_material` (`mat_id`),
  FOREIGN KEY (`emp_id`) REFERENCES `sys_employee` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Material Apply Table';

-- ----------------------------
-- View structure for V_EMP_FULL_INFO
-- ----------------------------
CREATE OR REPLACE VIEW `v_emp_full_info` AS
SELECT
    e.emp_id,
    e.emp_name,
    d.dep_name,
    p.pos_name,
    e.tel,
    e.email,
    e.status
FROM
    sys_employee e
    LEFT JOIN sys_department d ON e.dep_id = d.dep_id
    LEFT JOIN sys_position p ON e.pos_id = p.pos_id;
