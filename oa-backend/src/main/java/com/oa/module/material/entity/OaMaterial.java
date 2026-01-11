package com.oa.module.material.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName(value = "OA_MATERIAL", autoResultMap = true)
public class OaMaterial {
    @TableId(value = "MAT_ID", type = IdType.ASSIGN_ID)
    @TableField("MAT_ID")
    private String matId;
    
    @TableField("MAT_NAME")
    private String matName;
    
    @TableField("SPEC")
    private String spec;
    
    @TableField("PRICE")
    private BigDecimal price;
    
    @TableField("STOCK")
    private Integer stock;
}