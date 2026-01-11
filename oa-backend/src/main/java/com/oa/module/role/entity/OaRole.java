package com.oa.module.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_ROLE", autoResultMap = true)
public class OaRole {
    @TableId(value = "ROLE_ID", type = IdType.ASSIGN_ID)
    @TableField("ROLE_ID")
    private String roleId;
    
    @TableField("ROLE_NAME")
    private String roleName;
    
    @TableField("ROLE_DESC")
    private String roleDesc;
    
    @TableField("PERMISSIONS")
    private String permissions; // JSON?
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;
}