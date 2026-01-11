package com.oa.module.notification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_NOTIFICATION", autoResultMap = true)
public class OaNotification {
    @TableId(value = "NOTIF_ID", type = IdType.ASSIGN_ID)
    @TableField("NOTIF_ID")
    private String notifId;
    
    @TableField("USER_ID")
    private String userId;
    
    @TableField("TYPE")
    private String type; // system, workflow, mention
    
    @TableField("TITLE")
    private String title;
    
    @TableField("CONTENT")
    private String content;
    
    @TableField("IS_READ")
    private Integer isRead; // 0:? 1:?
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;
}