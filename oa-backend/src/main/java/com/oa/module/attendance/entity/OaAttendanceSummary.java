package com.oa.module.attendance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "OA_ATTENDANCE_SUMMARY", autoResultMap = true)
public class OaAttendanceSummary {
    @TableId(value = "SUMMARY_ID", type = IdType.ASSIGN_ID)
    @TableField("SUMMARY_ID")
    private Long summaryId;
    
    @TableField("USER_ID")
    private Long userId;
    
    @TableField("YEAR_MONTH")
    private String yearMonth; // Format: 2026-01
    
    @TableField("WORK_DAYS")
    private Integer workDays;
    
    @TableField("ACTUAL_DAYS")
    private Integer actualDays;
    
    @TableField("LATE_TIMES")
    private Integer lateTimes;
    
    @TableField("EARLY_LEAVE_TIMES")
    private Integer earlyLeaveTimes;
    
    @TableField("ABSENT_DAYS")
    private Integer absentDays;
    
    @TableField("LEAVE_DAYS")
    private BigDecimal leaveDays;
    
    @TableField("OVERTIME_HOURS")
    private BigDecimal overtimeHours;
    
    @TableField("TRIP_DAYS")
    private BigDecimal tripDays;
    
    @TableField("ATTENDANCE_RATE")
    private BigDecimal attendanceRate;
    
    @TableField("CREATE_TIME")
    private Date createTime;
    
    @TableField("UPDATE_TIME")
    private Date updateTime;
}