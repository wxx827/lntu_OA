package com.oa.module.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 内部消息实体类
 */
@Data
@TableName("OA_MESSAGE")
public class OaMessage {
    
    @TableId(value = "MSG_ID", type = IdType.ASSIGN_ID)
    private String msgId;
    
    private String senderId;
    
    private String receiverId;
    
    private String subject;
    
    private String content;
    
    private Integer isRead;  // 0:Unread, 1:Read
    
    private Integer isDeletedBySender;  // 0:Active, 1:Deleted
    
    private Integer isDeletedByReceiver;  // 0:Active, 1:Deleted
    
    private Date sendTime;
}
