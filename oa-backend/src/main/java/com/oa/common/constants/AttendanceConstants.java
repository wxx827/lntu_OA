package com.oa.common.constants;

import com.oa.module.system.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 考勤相关常量
 * 工作时间和阈值配置从数据库读取，状态常量保持不变
 */
@Component
public class AttendanceConstants {
    
    private static ConfigService configService;
    
    @Autowired
    public void setConfigService(ConfigService service) {
        AttendanceConstants.configService = service;
    }
    
    // ========== 状态常量（不变） ==========
    public static final String STATUS_NORMAL = "NORMAL";
    public static final String STATUS_LATE = "LATE";
    public static final String STATUS_EARLY_LEAVE = "EARLY_LEAVE";
    public static final String STATUS_ABSENT = "ABSENT";
    
    // ========== 审批状态常量（不变） ==========
    public static final String APPROVAL_PENDING = "PENDING";
    public static final String APPROVAL_APPROVED = "APPROVED";
    public static final String APPROVAL_REJECTED = "REJECTED";
    public static final String APPROVAL_CANCELLED = "CANCELLED";
    
    // ========== 请假类型常量（不变） ==========
    public static final String LEAVE_TYPE_SICK = "SICK";
    public static final String LEAVE_TYPE_ANNUAL = "ANNUAL";
    public static final String LEAVE_TYPE_PERSONAL = "PERSONAL";
    public static final String LEAVE_TYPE_MARRIAGE = "MARRIAGE";
    public static final String LEAVE_TYPE_MATERNITY = "MATERNITY";
    public static final String LEAVE_TYPE_PATERNITY = "PATERNITY";
    public static final String LEAVE_TYPE_BEREAVEMENT = "BEREAVEMENT";
    
    // ========== 默认值（数据库不可用时备用） ==========
    private static final String DEFAULT_WORK_START_TIME = "09:00:00";
    private static final String DEFAULT_WORK_END_TIME = "18:00:00";
    private static final int DEFAULT_LATE_THRESHOLD_MINUTES = 15;
    private static final int DEFAULT_EARLY_LEAVE_THRESHOLD_MINUTES = 30;
    
    // ========== 动态配置读取方法 ==========
    
    /**
     * 获取上班时间
     */
    public static String getWorkStartTime() {
        if (configService != null) {
            return configService.getValue("attendance.work.start", DEFAULT_WORK_START_TIME);
        }
        return DEFAULT_WORK_START_TIME;
    }
    
    /**
     * 获取下班时间
     */
    public static String getWorkEndTime() {
        if (configService != null) {
            return configService.getValue("attendance.work.end", DEFAULT_WORK_END_TIME);
        }
        return DEFAULT_WORK_END_TIME;
    }
    
    /**
     * 获取迟到阈值（分钟）
     */
    public static int getLateThresholdMinutes() {
        if (configService != null) {
            return configService.getIntValue("attendance.late.threshold", DEFAULT_LATE_THRESHOLD_MINUTES);
        }
        return DEFAULT_LATE_THRESHOLD_MINUTES;
    }
    
    /**
     * 获取早退阈值（分钟）
     */
    public static int getEarlyLeaveThresholdMinutes() {
        if (configService != null) {
            return configService.getIntValue("attendance.early.threshold", DEFAULT_EARLY_LEAVE_THRESHOLD_MINUTES);
        }
        return DEFAULT_EARLY_LEAVE_THRESHOLD_MINUTES;
    }
    
    // ========== 兼容性：保留原有常量名（已标记为过时） ==========
    @Deprecated
    public static final String WORK_START_TIME = DEFAULT_WORK_START_TIME;
    @Deprecated
    public static final String WORK_END_TIME = DEFAULT_WORK_END_TIME;
    @Deprecated
    public static final int LATE_THRESHOLD_MINUTES = DEFAULT_LATE_THRESHOLD_MINUTES;
    @Deprecated
    public static final int EARLY_LEAVE_THRESHOLD_MINUTES = DEFAULT_EARLY_LEAVE_THRESHOLD_MINUTES;
}