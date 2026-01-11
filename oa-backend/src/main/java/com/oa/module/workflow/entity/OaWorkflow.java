package com.oa.module.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_WORKFLOW", autoResultMap = true)
public class OaWorkflow {
    @TableId(value = "FLOW_ID", type = IdType.ASSIGN_UUID)
    @TableField("FLOW_ID")
    private String flowId;
    
    @TableField("FLOW_TYPE")
    private String flowType;
    
    @TableField("TITLE")
    private String title;
    
    @TableField("INITIATOR_ID")
    private String initiatorId;
    
    @TableField("CURRENT_APPROVER_ID")
    private String currentApproverId;
    
    @TableField("STATUS")
    private Integer status; // 0:? 1:? 2:?
    
    @TableField("PRIORITY")
    private String priority;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("UPDATE_TIME")
    private Date updateTime;
    
    @TableField("FLOW_DATA")
    private String flowData; // JSON?
    
    // ?
    @TableField(exist = false)
    private String initiatorName;
    
    @TableField(exist = false)
    private String initiatorAvatar;
}