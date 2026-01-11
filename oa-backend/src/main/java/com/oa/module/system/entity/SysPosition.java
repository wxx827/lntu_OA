package com.oa.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "SYS_POSITION", autoResultMap = true)
public class SysPosition {
    @TableId(value = "POS_ID", type = IdType.ASSIGN_ID)
    @TableField("POS_ID")
    private String posId;
    
    @TableField("POS_NAME")
    private String posName;
    
    @TableField("POS_LEVEL")
    private Integer posLevel;
}