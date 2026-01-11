package com.oa.module.attendance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "OA_ATTENDANCE", autoResultMap = true)
public class OaAttendance {
    @TableId(value = "ATTENDANCE_ID", type = IdType.ASSIGN_ID)
    @TableField("ATTENDANCE_ID")
    private Long attendanceId;
    
    @TableField("USER_ID")
    private Long userId;
    
    @TableField("WORK_DATE")
    private Date workDate;
    
    @TableField("CLOCK_IN_TIME")
    private Date clockInTime;
    
    @TableField("CLOCK_OUT_TIME")
    private Date clockOutTime;
    
    @TableField("CLOCK_IN_LOCATION")
    private String clockInLocation;
    
    @TableField("CLOCK_OUT_LOCATION")
    private String clockOutLocation;
    
    @TableField("STATUS")
    private String status; // NORMAL, LATE, EARLY_LEAVE, ABSENT
    
    @TableField("WORK_HOURS")
    private BigDecimal workHours;
    
    @TableField("IS_LATE")
    private Integer isLate; // 0-No, 1-Yes
    
    @TableField("IS_EARLY_LEAVE")
    private Integer isEarlyLeave; // 0-No, 1-Yes
    
    @TableField("REMARK")
    private String remark;
    
    @TableField("CREATE_TIME")
    private Date createTime;
    
    @TableField("UPDATE_TIME")
    private Date updateTime;
}