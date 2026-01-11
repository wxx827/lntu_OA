package com.oa.module.hr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName(value = "OA_DEPARTMENT", autoResultMap = true)
public class OaDepartment {
    @TableId(value = "DEPT_ID", type = IdType.ASSIGN_ID)
    @TableField("DEPT_ID")
    private Long deptId;
    
    @TableField("DEPT_NAME")
    private String deptName;
    
    @TableField("PARENT_ID")
    private Long parentId;
    
    @TableField("DEPT_LEVEL")
    private Integer deptLevel;
    
    @TableField("MANAGER_ID")
    private Long managerId;
    
    @TableField("DESCRIPTION")
    private String description;
    
    @TableField("STATUS")
    private String status; // ACTIVE, INACTIVE
    
    @TableField("SORT_ORDER")
    private Integer sortOrder;
    
    @TableField("CREATE_TIME")
    private Date createTime;
    
    @TableField("UPDATE_TIME")
    private Date updateTime;
    
    // ?
    @TableField(exist = false)
    private String managerName; // ?
    
    @TableField(exist = false)
    private Integer employeeCount; // 
    
    @TableField(exist = false)
    private List<OaDepartment> children; // ?
}