-- =====================================================
-- OA_PROJECT 项目/任务表 (用于工作台展示)
-- =====================================================
CREATE TABLE IF NOT EXISTS `OA_PROJECT` (
    `project_id` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '项目ID',
    `name` VARCHAR(100) NOT NULL COMMENT '项目名称',
    `description` VARCHAR(500) COMMENT '项目描述',
    `progress` INT DEFAULT 0 COMMENT '进度百分比(0-100)',
    `status` VARCHAR(20) DEFAULT '进行中' COMMENT '状态: 进行中/开发中/已计划/已完成',
    `color` VARCHAR(20) DEFAULT '#409eff' COMMENT '进度条颜色',
    `icon` VARCHAR(50) DEFAULT 'Platform' COMMENT '图标名称',
    `due_date` DATE COMMENT '截止日期',
    `creator_id` VARCHAR(32) COMMENT '创建人ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目任务表';

-- 初始数据
INSERT INTO `OA_PROJECT` (`project_id`, `name`, `description`, `progress`, `status`, `color`, `icon`, `due_date`) VALUES
('P001', '前端界面优化', 'Vue3 布局与样式调整', 85, '进行中', '#409eff', 'Platform', '2026-01-20'),
('P002', '后端接口开发', '服务清理与数据对接', 30, '开发中', '#e6a23c', 'Box', '2026-02-15'),
('P003', 'AI 功能试点', '模型集成测试', 10, '已计划', '#f56c6c', 'DataAnalysis', '2026-03-01');
