-- Views, Triggers and Stored Procedures for OA System V4.0

-- ---------------------------------------------------------------------
-- View: V_EMP_FULL_INFO (Employee Full Information)
-- ---------------------------------------------------------------------
CREATE OR REPLACE VIEW v_emp_full_info AS
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

-- ---------------------------------------------------------------------
-- Trigger: TRG_LOCK_ROOM (Auto Lock Meeting Room)
-- Requirement: When booking status becomes 1 (Approved), set room status to 1 (Occupied)
-- ---------------------------------------------------------------------
DELIMITER $$

CREATE TRIGGER trg_lock_room
AFTER UPDATE ON oa_meeting_book
FOR EACH ROW
BEGIN
    IF NEW.status = 1 AND OLD.status != 1 THEN
        UPDATE oa_meeting_room SET status = 1 WHERE room_id = NEW.room_id;
    END IF;
END$$

-- ---------------------------------------------------------------------
-- Trigger: TRG_DEDUCT_STOCK (Auto Deduct Stock)
-- Requirement: When apply state becomes 1 (Approved), deduct stock
-- Note: Logic also implemented in Java Service for reliability
-- ---------------------------------------------------------------------
CREATE TRIGGER trg_deduct_stock
AFTER UPDATE ON oa_mat_apply
FOR EACH ROW
BEGIN
    IF NEW.state = 1 AND OLD.state != 1 THEN
        UPDATE oa_material SET stock = stock - NEW.count WHERE mat_id = NEW.mat_id;
    END IF;
END$$

DELIMITER ;

-- ---------------------------------------------------------------------
-- Stored Procedure: SP_MONTHLY_STATS (Monthly Statistics)
-- Requirement: Monthly report (Placeholder implementation)
-- ---------------------------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_monthly_stats(IN report_month VARCHAR(7))
BEGIN
    -- Select count of new bookings in month
    SELECT COUNT(*) as booking_count FROM oa_meeting_book 
    WHERE DATE_FORMAT(create_time, '%Y-%m') = report_month;
    
    -- Select count of material applications
    SELECT COUNT(*) as mat_apply_count FROM oa_mat_apply 
    WHERE DATE_FORMAT(apply_time, '%Y-%m') = report_month;
END$$
DELIMITER ;
