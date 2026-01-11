package com.oa.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "SYS_DEPARTMENT", autoResultMap = true)
public class SysDepartment {
    @TableId(value = "DEP_ID", type = IdType.ASSIGN_ID)
    @TableField("DEP_ID")
    private String depId;
    
    @TableField("DEP_NAME")
    private String depName;
    
    @TableField("DEP_DESC")
    private String depDesc;
    
    @TableField("PARENT_ID")
    private String parentId;
    
    @TableField(exist = false)
    private List<SysDepartment> children;
}