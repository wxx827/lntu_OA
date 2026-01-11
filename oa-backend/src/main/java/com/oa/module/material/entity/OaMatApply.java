package com.oa.module.material.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_MAT_APPLY", autoResultMap = true)
public class OaMatApply {
    @TableId(value = "APPLY_ID", type = IdType.ASSIGN_UUID)
    @TableField("APPLY_ID")
    private String applyId;
    
    @TableField("MAT_ID")
    private String matId;
    
    @TableField("EMP_ID")
    private String empId;
    
    @TableField("COUNT")
    private Integer count;
    
    @TableField("REASON")
    private String reason;
    
    @TableField("STATE")
    private Integer state; // 0: Pending, 1: Approved, 2: Rejected
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("APPLY_DATE")
    private Date applyDate;
    
    // 非数据库字段 - 用于前端显示
    @TableField(exist = false)
    private String matName;
    
    @TableField(exist = false)
    private String empName;
}