package com.oa.module.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_EXPENSE_TYPE", autoResultMap = true)
public class OaExpenseType {
    @TableId(value = "TYPE_ID", type = IdType.ASSIGN_ID)
    @TableField("TYPE_ID")
    private Long typeId;
    
    @TableField("TYPE_NAME")
    private String typeName;
    
    @TableField("DESCRIPTION")
    private String description;
    
    @TableField("STATUS")
    private String status; // ACTIVE, INACTIVE
    
    @TableField("CREATE_TIME")
    private Date createTime;
    
    @TableField("UPDATE_TIME")
    private Date updateTime;
}