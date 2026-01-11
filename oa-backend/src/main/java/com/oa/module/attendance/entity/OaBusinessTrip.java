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
@TableName(value = "OA_BUSINESS_TRIP", autoResultMap = true)
public class OaBusinessTrip {
    @TableId(value = "TRIP_ID", type = IdType.ASSIGN_ID)
    @TableField("TRIP_ID")
    private Long tripId;
    
    @TableField("USER_ID")
    private Long userId;
    
    @TableField("DESTINATION")
    private String destination;
    
    @TableField("START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    @TableField("END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    @TableField("DAYS")
    private BigDecimal days;
    
    @TableField("PURPOSE")
    private String purpose;
    
    @TableField("TRANSPORT_TYPE")
    private String transportType; // FLIGHT, TRAIN, CAR, OTHER
    
    @TableField("ESTIMATED_COST")
    private BigDecimal estimatedCost;
    
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