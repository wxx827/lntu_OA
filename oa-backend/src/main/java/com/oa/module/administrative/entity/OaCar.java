package com.oa.module.administrative.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("OA_CAR")
public class OaCar {
    @TableId(value = "CAR_ID", type = IdType.ASSIGN_UUID)
    private String carId;
    
    @TableField("CAR_LICENCE")
    private String carLicence;
    
    @TableField("CAR_TYPE")
    private String carType;
    
    @TableField("BRAND")
    private String brand;
    
    @TableField("SEATS")
    private Integer seats;
    
    @TableField("STATE")
    private Integer state; // 0:空闲 1:使用中 2:维护中
    
    @TableField("MILEAGE")
    private Integer mileage; // 里程数
    
    @TableField("REMARK")
    private String remark;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;
}