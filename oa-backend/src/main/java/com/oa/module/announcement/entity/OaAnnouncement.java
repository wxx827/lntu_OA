package com.oa.module.announcement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 公告实体类
 */
@Data
@TableName("OA_ANNOUNCEMENT")
public class OaAnnouncement {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 公告标题
     */
    private String title;
    
    /**
     * 公告内容
     */
    private String content;
    
    /**
     * 公告类型: important-重要, urgent-紧急, normal-普通
     */
    private String type;
    
    /**
     * 状态: 0-草稿, 1-已发布, 2-已撤回
     */
    private Integer status;
    
    /**
     * 发布人ID
     */
    private String publisherId;
    
    /**
     * 发布人姓名
     */
    private String publisherName;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 浏览次数
     */
    private Integer views;
    
    /**
     * 是否置顶: 0-否, 1-是
     */
    private Integer isTop;
}
