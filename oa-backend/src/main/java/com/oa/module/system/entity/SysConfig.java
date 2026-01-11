package com.oa.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置实体类
 */
@Data
@TableName(value = "SYS_CONFIG", autoResultMap = true)
public class SysConfig {
    
    @TableId("CONFIG_ID")
    private String configId;
    
    @TableField("CONFIG_KEY")
    private String configKey;
    
    @TableField("CONFIG_VALUE")
    private String configValue;
    
    @TableField("CONFIG_TYPE")
    private String configType;
    
    @TableField("CONFIG_GROUP")
    private String configGroup;
    
    @TableField("DESCRIPTION")
    private String description;
    
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
