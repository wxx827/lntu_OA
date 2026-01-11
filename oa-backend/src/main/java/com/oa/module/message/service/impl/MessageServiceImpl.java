package com.oa.module.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.module.message.entity.OaMessage;
import com.oa.module.message.mapper.OaMessageMapper;
import com.oa.module.message.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 内部消息服务实现类
 */
@Service
public class MessageServiceImpl extends ServiceImpl<OaMessageMapper, OaMessage> 
        implements MessageService {
    
    @Override
    public List<OaMessage> getInbox(String userId) {
        return this.list(new LambdaQueryWrapper<OaMessage>()
                .eq(OaMessage::getReceiverId, userId)
                .eq(OaMessage::getIsDeletedByReceiver, 0)
                .orderByDesc(OaMessage::getSendTime));
    }
    
    @Override
    public List<OaMessage> getOutbox(String userId) {
        return this.list(new LambdaQueryWrapper<OaMessage>()
                .eq(OaMessage::getSenderId, userId)
                .eq(OaMessage::getIsDeletedBySender, 0)
                .orderByDesc(OaMessage::getSendTime));
    }
    
    @Override
    public List<OaMessage> getDrafts(String userId) {
        // 草稿功能预留,暂时返回空列表
        // 需要在数据库表中添加 STATUS 字段来区分草稿和已发送
        return List.of();
    }
    
    @Override
    public OaMessage getDetail(String msgId) {
        return this.getById(msgId);
    }
    
    @Override
    @Transactional
    public boolean sendMessage(OaMessage message) {
        message.setIsRead(0);
        message.setIsDeletedBySender(0);
        message.setIsDeletedByReceiver(0);
        message.setSendTime(new Date());
        
        return this.save(message);
    }
    
    @Override
    @Transactional
    public boolean markAsRead(String msgId) {
        OaMessage message = this.getById(msgId);
        if (message != null) {
            message.setIsRead(1);
            return this.updateById(message);
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean deleteMessage(String msgId, String userId) {
        OaMessage message = this.getById(msgId);
        if (message != null) {
            // 根据当前用户是发送者还是接收者来设置删除标记
            if (userId.equals(message.getSenderId())) {
                message.setIsDeletedBySender(1);
            } else if (userId.equals(message.getReceiverId())) {
                message.setIsDeletedByReceiver(1);
            } else {
                return false; // 用户无权删除此消息
            }
            return this.updateById(message);
        }
        return false;
    }
    
    @Override
    public Long getUnreadCount(String userId) {
        return this.count(new LambdaQueryWrapper<OaMessage>()
                .eq(OaMessage::getReceiverId, userId)
                .eq(OaMessage::getIsRead, 0)
                .eq(OaMessage::getIsDeletedByReceiver, 0));
    }
}
