package com.oa.module.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.module.message.entity.OaMessage;

import java.util.List;

/**
 * 内部消息服务接口
 */
public interface MessageService extends IService<OaMessage> {
    
    /**
     * 获取收件箱消息列表
     */
    List<OaMessage> getInbox(String userId);
    
    /**
     * 获取发件箱消息列表
     */
    List<OaMessage> getOutbox(String userId);
    
    /**
     * 获取草稿箱消息列表 (预留功能)
     */
    List<OaMessage> getDrafts(String userId);
    
    /**
     * 获取消息详情
     */
    OaMessage getDetail(String msgId);
    
    /**
     * 发送消息
     */
    boolean sendMessage(OaMessage message);
    
    /**
     * 标记消息为已读
     */
    boolean markAsRead(String msgId);
    
    /**
     * 删除消息 (软删除)
     */
    boolean deleteMessage(String msgId, String userId);
    
    /**
     * 获取未读消息数量
     */
    Long getUnreadCount(String userId);
}
