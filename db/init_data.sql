-- =======================================================
-- Initial Data for OA System (MySQL 8.0)
-- =======================================================

-- Departments
INSERT INTO SYS_DEPARTMENT (DEP_ID, DEP_NAME, DEP_DESC) VALUES
('D01', 'General', 'Default Department'),
('D02', 'Operations', 'Operations Department');

-- Positions
INSERT INTO SYS_POSITION (POS_ID, POS_NAME, POS_LEVEL) VALUES
('P01', 'Manager', 10),
('P02', 'Staff', 1);

-- Employees (password: 123456, MD5)
INSERT INTO SYS_EMPLOYEE (EMP_ID, EMP_NAME, PASSWORD, ROLE, SEX, TEL, EMAIL, DEP_ID, POS_ID) VALUES
('E001', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'ADMIN', 'Male', '13800138000', 'admin@oa.com', 'D01', 'P01'),
('E002', 'manager', 'e10adc3949ba59abbe56e057f20f883e', 'MANAGER', 'Male', '13800138001', 'manager@oa.com', 'D01', 'P01'),
('E003', 'staff01', 'e10adc3949ba59abbe56e057f20f883e', 'EMPLOYEE', 'Female', '13800138002', 'staff01@oa.com', 'D02', 'P02');

-- Meeting Rooms
INSERT INTO OA_MEETING_ROOM (ROOM_ID, ROOM_NAME, LOCATION, CAPACITY, HAS_MEDIA, STATUS) VALUES
('R301', 'Main Meeting Room 301', '3F', 20, 1, 0),
('R202', 'Small Meeting Room 202', '2F', 8, 1, 0),
('R405', 'Training Room 405', '4F', 30, 1, 0);

-- Materials
INSERT INTO OA_MATERIAL (MAT_ID, MAT_NAME, SPEC, PRICE, STOCK) VALUES
('M001', 'A4 Paper', '500 pages/pack', 25.00, 100),
('M002', 'Black Pen', '0.5mm', 2.50, 120),
('M003', 'Stapler', 'Standard', 18.00, 60),
('M004', 'Notebook', 'A5', 6.50, 80),
('M005', 'Folder', 'A4', 4.00, 150);

-- Vehicles
INSERT INTO OA_CAR (CAR_ID, CAR_LICENCE, CAR_TYPE, BRAND, SEATS, STATE, REMARK, CREATE_TIME) VALUES
('C001', 'A88888', 'Sedan', 'Audi A6', 5, 0, '', NOW()),
('C002', 'A66666', 'MPV', 'Buick GL8', 7, 0, '', NOW());

COMMIT;
