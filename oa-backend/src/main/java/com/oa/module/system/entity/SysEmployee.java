package com.oa.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "SYS_EMPLOYEE", autoResultMap = true)
public class SysEmployee {
    @TableId(value = "EMP_ID", type = IdType.ASSIGN_ID)
    @TableField("EMP_ID")
    private String empId;
    
    @TableField("EMP_NAME")
    private String empName;
    
    @TableField("PASSWORD")
    private String password;

    @TableField("ROLE")
    private String role;
    
    @TableField("SEX")
    private String sex;
    
    @TableField("TEL")
    private String tel;
    
    @TableField("EMAIL")
    private String email;
    
    @TableField("DEP_ID")
    private String depId;
    
    @TableField("POS_ID")
    private String posId;
}