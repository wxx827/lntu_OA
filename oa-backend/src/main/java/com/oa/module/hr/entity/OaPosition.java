package com.oa.module.hr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_POSITION", autoResultMap = true)
public class OaPosition {
    @TableId(value = "POSITION_ID", type = IdType.ASSIGN_ID)
    @TableField("POSITION_ID")
    private Long positionId;
    
    @TableField("POSITION_NAME")
    private String positionName;
    
    @TableField("DEPT_ID")
    private Long deptId;
    
    @TableField("POSITION_LEVEL")
    private String positionLevel; // JUNIOR, MIDDLE, SENIOR, EXPERT
    
    @TableField("DESCRIPTION")
    private String description;
    
    @TableField("RESPONSIBILITIES")
    private String responsibilities;
    
    @TableField("REQUIREMENTS")
    private String requirements;
    
    @TableField("STATUS")
    private String status; // ACTIVE, INACTIVE
    
    @TableField("CREATE_TIME")
    private Date createTime;
    
    @TableField("UPDATE_TIME")
    private Date updateTime;
    
    // ?
    @TableField(exist = false)
    private String deptName; // ?
}