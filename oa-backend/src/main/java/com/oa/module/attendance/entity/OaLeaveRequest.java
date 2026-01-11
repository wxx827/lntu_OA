package com.oa.module.attendance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "OA_LEAVE_REQUEST", autoResultMap = true)
public class OaLeaveRequest {
    @TableId(value = "REQUEST_ID", type = IdType.ASSIGN_ID)
    @TableField("REQUEST_ID")
    private Long requestId;
    
    @TableField("USER_ID")
    private Long userId;
    
    @TableField("LEAVE_TYPE")
    private String leaveType; // SICK, ANNUAL, PERSONAL, MARRIAGE, MATERNITY, PATERNITY, BEREAVEMENT
    
    @TableField("START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    @TableField("END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    @TableField("DAYS")
    private BigDecimal days;
    
    @TableField("REASON")
    private String reason;
    
    @TableField("STATUS")
    private String status; // PENDING, APPROVED, REJECTED, CANCELLED
    
    @TableField("APPROVER_ID")
    private Long approverId;
    
    @TableField("APPROVE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approveTime;
    
    @TableField("APPROVE_REMARK")
    private String approveRemark;
    
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}