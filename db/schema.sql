-- =======================================================
-- OA System Database Schema (Oracle)
-- Version: 4.0 FINAL
-- Author: Gemini Agent
-- =======================================================

-- 1. Cleanup (Optional: uncomment if re-running)
-- DROP TABLE OA_MAT_APPLY;
-- DROP TABLE OA_MATERIAL;
-- DROP TABLE OA_MEETING_BOOK;
-- DROP TABLE OA_MEETING_ROOM;
-- DROP TABLE SYS_EMPLOYEE;
-- DROP TABLE SYS_POSITION;
-- DROP TABLE SYS_DEPARTMENT;

-- =======================================================
-- 2. Core Tables
-- =======================================================

-- 2.1 Department Table
CREATE TABLE SYS_DEPARTMENT (
    DEP_ID      VARCHAR2(32) PRIMARY KEY,
    DEP_NAME    VARCHAR2(100) NOT NULL,
    DEP_DESC    VARCHAR2(200)
);

-- 2.2 Position Table
CREATE TABLE SYS_POSITION (
    POS_ID      VARCHAR2(32) PRIMARY KEY,
    POS_NAME    VARCHAR2(50) NOT NULL,
    POS_LEVEL   NUMBER(2)    -- Level for permission check
);

-- 2.3 Employee Table
CREATE TABLE SYS_EMPLOYEE (
    EMP_ID      VARCHAR2(32) PRIMARY KEY,
    EMP_NAME    VARCHAR2(50) NOT NULL,
    PASSWORD    VARCHAR2(100) NOT NULL, -- MD5 Encrypted
    SEX         VARCHAR2(10),
    TEL         VARCHAR2(20),
    EMAIL       VARCHAR2(50),
    DEP_ID      VARCHAR2(32),
    POS_ID      VARCHAR2(32),
    CONSTRAINT FK_EMP_DEP FOREIGN KEY (DEP_ID) REFERENCES SYS_DEPARTMENT(DEP_ID),
    CONSTRAINT FK_EMP_POS FOREIGN KEY (POS_ID) REFERENCES SYS_POSITION(POS_ID)
);

-- 2.4 Meeting Room Table
CREATE TABLE OA_MEETING_ROOM (
    ROOM_ID     VARCHAR2(32) PRIMARY KEY,
    ROOM_NAME   VARCHAR2(100) NOT NULL,
    LOCATION    VARCHAR2(100),
    CAPACITY    NUMBER(4),b
    HAS_MEDIA   NUMBER(1) DEFAULT 0, -- 0:No, 1:Yes
    STATUS      NUMBER(1) DEFAULT 0  -- 0:Free, 1:Occupied, 2:Maintenance
);

-- 2.5 Meeting Booking Table
CREATE TABLE OA_MEETING_BOOK (
    BOOK_ID     VARCHAR2(32) PRIMARY KEY,
    ROOM_ID     VARCHAR2(32) NOT NULL,
    EMP_ID      VARCHAR2(32) NOT NULL,
    START_TIME  DATE NOT NULL,
    END_TIME    DATE NOT NULL,
    TOPIC       VARCHAR2(200),
    STATUS      NUMBER(1) DEFAULT 0, -- 0:Pending, 1:Approved, 2:Rejected
    CREATE_TIME DATE DEFAULT SYSDATE,
    CONSTRAINT FK_BOOK_ROOM FOREIGN KEY (ROOM_ID) REFERENCES OA_MEETING_ROOM(ROOM_ID),
    CONSTRAINT FK_BOOK_EMP FOREIGN KEY (EMP_ID) REFERENCES SYS_EMPLOYEE(EMP_ID)
);

-- 2.6 Material Table
CREATE TABLE OA_MATERIAL (
    MAT_ID      VARCHAR2(32) PRIMARY KEY,
    MAT_NAME    VARCHAR2(100) NOT NULL,
    SPEC        VARCHAR2(50),
    PRICE       NUMBER(10, 2),
    STOCK       NUMBER(10) DEFAULT 0
);

-- 2.7 Material Apply Table
CREATE TABLE OA_MAT_APPLY (
    APPLY_ID    VARCHAR2(32) PRIMARY KEY,
    MAT_ID      VARCHAR2(32) NOT NULL,
    EMP_ID      VARCHAR2(32) NOT NULL,
    COUNT       NUMBER(5) NOT NULL,
    REASON      VARCHAR2(200),
    APPLY_DATE  DATE DEFAULT SYSDATE,
    STATE       NUMBER(1) DEFAULT 0, -- 0:Pending, 1:Approved/Distributed, 2:Rejected
    CONSTRAINT FK_APPLY_MAT FOREIGN KEY (MAT_ID) REFERENCES OA_MATERIAL(MAT_ID),
    CONSTRAINT FK_APPLY_EMP FOREIGN KEY (EMP_ID) REFERENCES SYS_EMPLOYEE(EMP_ID)
);

-- =======================================================
-- 3. Advanced Features (Course Requirements)
-- =======================================================

-- 3.1 View: Full Employee Info
-- Simplifies frontend query by joining Dept and Position
CREATE OR REPLACE VIEW V_EMP_FULL_INFO AS
SELECT 
    e.EMP_ID, 
    e.EMP_NAME, 
    e.SEX, 
    e.TEL, 
    d.DEP_ID, 
    d.DEP_NAME, 
    p.POS_ID, 
    p.POS_NAME
FROM SYS_EMPLOYEE e
LEFT JOIN SYS_DEPARTMENT d ON e.DEP_ID = d.DEP_ID
LEFT JOIN SYS_POSITION p ON e.POS_ID = p.POS_ID;

-- 3.2 Trigger 1: Auto Deduct Stock
-- Automatically reduces material stock when application is approved (STATE becomes 1)
CREATE OR REPLACE TRIGGER TRG_DEDUCT_STOCK
AFTER UPDATE OF STATE ON OA_MAT_APPLY
FOR EACH ROW
WHEN (NEW.STATE = 1)
BEGIN
    UPDATE OA_MATERIAL 
    SET STOCK = STOCK - :NEW.COUNT
    WHERE MAT_ID = :NEW.MAT_ID;
END;
/

-- 3.3 Trigger 2: Auto Lock Meeting Room
-- Automatically marks room as Occupied (1) if an approved meeting is currently happening
-- Note: This is usually handled by scheduled tasks in real-world, but using Trigger for demo/course requirement.
-- Here we trigger on Booking Approval to simplisticly mark room if it starts now-ish. 
-- A better logic for DB trigger might be simply logging or complex check. 
-- Let's stick to the V4.0 plan logic: Lock room if booking is Approved.
CREATE OR REPLACE TRIGGER TRG_LOCK_ROOM
AFTER UPDATE OF STATUS ON OA_MEETING_BOOK
FOR EACH ROW
WHEN (NEW.STATUS = 1)
BEGIN
    -- Simply logic: If approved, mark room as potentially busy (1). 
    -- Real world needs more complex logic, but this satisfies 'Trigger' requirement.
    UPDATE OA_MEETING_ROOM 
    SET STATUS = 1 
    WHERE ROOM_ID = :NEW.ROOM_ID;
END;
/

-- 3.4 Stored Procedure: Department Monthly Stats
-- Calculates total cost of materials used by a department in a given month (YYYY-MM)
CREATE OR REPLACE PROCEDURE SP_DEPT_STATS(
    p_month IN VARCHAR2, 
    p_cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_cursor FOR
    SELECT 
        d.DEP_NAME, 
        SUM(m.PRICE * a.COUNT) as TOTAL_COST
    FROM SYS_DEPARTMENT d
    JOIN SYS_EMPLOYEE e ON d.DEP_ID = e.DEP_ID
    JOIN OA_MAT_APPLY a ON e.EMP_ID = a.EMP_ID
    JOIN OA_MATERIAL m ON a.MAT_ID = m.MAT_ID
    WHERE TO_CHAR(a.APPLY_DATE, 'YYYY-MM') = p_month
      AND a.STATE = 1 -- Only calculated approved items
    GROUP BY d.DEP_NAME;
END;
/

-- =======================================================
-- 4. Sample Data (Optional)
-- =======================================================
INSERT INTO SYS_DEPARTMENT VALUES ('D01', 'Tech Dept', 'Technology R&D');
INSERT INTO SYS_DEPARTMENT VALUES ('D02', 'HR Dept', 'Human Resources');

INSERT INTO SYS_POSITION VALUES ('P01', 'Manager', 10);
INSERT INTO SYS_POSITION VALUES ('P02', 'Staff', 1);

INSERT INTO SYS_EMPLOYEE VALUES ('E001', 'Admin', 'e10adc3949ba59abbe56e057f20f883e', 'Male', '13800138000', 'admin@oa.com', 'D01', 'P01');

INSERT INTO OA_MEETING_ROOM VALUES ('R101', 'Meeting Room A', '1F', 10, 1, 0);
INSERT INTO OA_MEETING_ROOM VALUES ('R102', 'Meeting Room B', '2F', 20, 1, 0);

INSERT INTO OA_MATERIAL VALUES ('M001', 'A4 Paper', '500 pages/pack', 25.00, 100);
INSERT INTO OA_MATERIAL VALUES ('M002', 'Pen', 'Black', 2.00, 500);

COMMIT;
