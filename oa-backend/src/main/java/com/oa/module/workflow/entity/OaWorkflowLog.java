package com.oa.module.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_WORKFLOW_LOG", autoResultMap = true)
public class OaWorkflowLog {
    @TableId(value = "LOG_ID", type = IdType.ASSIGN_ID)
    @TableField("LOG_ID")
    private String logId;
    
    @TableField("FLOW_ID")
    private String flowId;
    
    @TableField("APPROVER_ID")
    private String approverId;
    
    @TableField("ACTION")
    private String action; // pass, reject
    
    @TableField("COMMENT")
    private String comment;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("ACTION_TIME")
    private Date actionTime;
}