package com.oa.module.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("OA_PROJECT")
public class OaProject {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String projectId;
    
    private String name;
    
    private String description;
    
    private Integer progress;
    
    private String status;
    
    private String color;
    
    private String icon;
    
    private Date dueDate;
    
    private String creatorId;
    
    private Date createTime;
    
    private Date updateTime;
}
