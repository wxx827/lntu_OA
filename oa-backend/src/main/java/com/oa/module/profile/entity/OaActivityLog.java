package com.oa.module.profile.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_ACTIVITY_LOG", autoResultMap = true)
public class OaActivityLog {
    @TableId(value = "LOG_ID", type = IdType.ASSIGN_ID)
    @TableField("LOG_ID")
    private String logId;
    
    @TableField("USER_ID")
    private String userId;
    
    @TableField("ACTION")
    private String action;
    
    @TableField("DETAIL")
    private String detail;
    
    @TableField("IP_ADDRESS")
    private String ipAddress;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;
}