-- ========================================
-- 公告管理表 (Announcement Management)
-- ========================================

CREATE TABLE IF NOT EXISTS OA_ANNOUNCEMENT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '公告ID',
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    type VARCHAR(20) DEFAULT 'normal' COMMENT '公告类型: important-重要, urgent-紧急, normal-普通',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-草稿, 1-已发布, 2-已撤回',
    publisher_id VARCHAR(32) NOT NULL COMMENT '发布人ID',
    publisher_name VARCHAR(50) COMMENT '发布人姓名',
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    views INT DEFAULT 0 COMMENT '浏览次数',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶: 0-否, 1-是',
    INDEX idx_publisher (publisher_id),
    INDEX idx_status (status),
    INDEX idx_type (type),
    INDEX idx_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 初始化示例数据
INSERT INTO OA_ANNOUNCEMENT (title, content, type, status, publisher_id, publisher_name, is_top) VALUES
('欢迎使用OA协同办公系统', '尊敬的各位同事,欢迎使用全新升级的OA协同办公系统。本系统集成了考勤、审批、会议、物资等多项功能,旨在提升办公效率。如有任何问题,请联系IT部门。', 'important', 1, 'E001', 'Admin', 1),
('系统维护通知', '定于本周六晚上22:00-24:00进行系统维护升级,届时系统将暂停服务,请各位提前做好工作安排。给您带来不便,敬请谅解。', 'urgent', 1, 'E001', 'Admin', 0),
('节假日安排通知', '根据国家法定节假日安排,春节假期为1月28日至2月3日,共7天。请各部门合理安排工作,做好值班安排。', 'normal', 1, 'E001', 'Admin', 0);
