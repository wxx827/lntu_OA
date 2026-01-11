-- Initial Data for OA System V4.0

-- ---------------------------------------------------------------------
-- Insert Departments
-- ---------------------------------------------------------------------
INSERT INTO sys_department (dep_name, parent_id) VALUES ('总经办', NULL);
INSERT INTO sys_department (dep_name, parent_id) VALUES ('技术部', NULL);
INSERT INTO sys_department (dep_name, parent_id) VALUES ('市场部', NULL);
INSERT INTO sys_department (dep_name, parent_id) VALUES ('人力资源部', NULL);
INSERT INTO sys_department (dep_name, parent_id) VALUES ('研发组', 2);
INSERT INTO sys_department (dep_name, parent_id) VALUES ('测试组', 2);

-- ---------------------------------------------------------------------
-- Insert Positions
-- ---------------------------------------------------------------------
INSERT INTO sys_position (pos_name) VALUES ('总经理');
INSERT INTO sys_position (pos_name) VALUES ('部门经理');
INSERT INTO sys_position (pos_name) VALUES ('高级工程师');
INSERT INTO sys_position (pos_name) VALUES ('工程师');
INSERT INTO sys_position (pos_name) VALUES ('助理');

-- ---------------------------------------------------------------------
-- Insert Employees (Password: 123456 in MD5 = e10adc3949ba59abbe56e057f20f883e)
-- ---------------------------------------------------------------------
INSERT INTO sys_employee (emp_name, password, dep_id, pos_id, tel, email, sex, status) 
VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, '13800138000', 'admin@oa.com', 1, 1);

INSERT INTO sys_employee (emp_name, password, dep_id, pos_id, tel, email, sex, status) 
VALUES ('张三', 'e10adc3949ba59abbe56e057f20f883e', 2, 2, '13800138001', 'zhangsan@oa.com', 1, 1);

INSERT INTO sys_employee (emp_name, password, dep_id, pos_id, tel, email, sex, status) 
VALUES ('李四', 'e10adc3949ba59abbe56e057f20f883e', 2, 3, '13800138002', 'lisi@oa.com', 1, 1);

INSERT INTO sys_employee (emp_name, password, dep_id, pos_id, tel, email, sex, status) 
VALUES ('王五', 'e10adc3949ba59abbe56e057f20f883e', 3, 4, '13800138003', 'wangwu@oa.com', 0, 1);

-- ---------------------------------------------------------------------
-- Insert Meeting Rooms
-- ---------------------------------------------------------------------
INSERT INTO oa_meeting_room (room_name, capacity, location, equipment, status) 
VALUES ('301会议室', 12, '3楼东侧', '4K投影仪,白板,视频会议系统', 0);

INSERT INTO oa_meeting_room (room_name, capacity, location, equipment, status) 
VALUES ('205会议室', 8, '2楼西侧', '投影仪,白板', 0);

INSERT INTO oa_meeting_room (room_name, capacity, location, equipment, status) 
VALUES ('108会议室', 6, '1楼南侧', '电视,白板', 0);

INSERT INTO oa_meeting_room (room_name, capacity, location, equipment, status) 
VALUES ('大会议厅', 50, '1楼中央', '舞台,音响,投影,直播设备', 0);

-- ---------------------------------------------------------------------
-- Insert Materials
-- ---------------------------------------------------------------------
INSERT INTO oa_material (mat_name, spec, price, stock) 
VALUES ('A4打印纸', '500张/包', 25.00, 100);

INSERT INTO oa_material (mat_name, spec, price, stock) 
VALUES ('签字笔', '黑色0.5mm', 2.50, 200);

INSERT INTO oa_material (mat_name, spec, price, stock) 
VALUES ('笔记本', 'A5/80页', 8.00, 50);

INSERT INTO oa_material (mat_name, spec, price, stock) 
VALUES ('订书机', '标准型', 15.00, 30);

INSERT INTO oa_material (mat_name, spec, price, stock) 
VALUES ('文件夹', 'A4塑料', 3.50, 80);

-- ---------------------------------------------------------------------
-- Insert Sample Meeting Bookings
-- ---------------------------------------------------------------------
INSERT INTO oa_meeting_book (room_id, emp_id, subject, start_time, end_time, status, create_time) 
VALUES (1, 2, '技术部周会', '2026-01-06 14:00:00', '2026-01-06 16:00:00', 1, NOW());

INSERT INTO oa_meeting_book (room_id, emp_id, subject, start_time, end_time, status, create_time) 
VALUES (2, 3, '项目评审会', '2026-01-07 10:00:00', '2026-01-07 12:00:00', 0, NOW());

-- ---------------------------------------------------------------------
-- Insert Sample Material Applications
-- ---------------------------------------------------------------------
INSERT INTO oa_mat_apply (mat_id, emp_id, count, reason, state, apply_time) 
VALUES (1, 3, 5, '部门日常办公用', 1, NOW());

INSERT INTO oa_mat_apply (mat_id, emp_id, count, reason, state, apply_time) 
VALUES (2, 4, 10, '市场活动使用', 0, NOW());
