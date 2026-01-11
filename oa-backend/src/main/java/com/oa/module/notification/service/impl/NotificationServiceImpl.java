package com.oa.module.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.oa.module.notification.entity.OaNotification;
import com.oa.module.notification.mapper.OaNotificationMapper;
import com.oa.module.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private OaNotificationMapper notificationMapper;

    @Override
    public List<OaNotification> getList(String userId, String type) {
        LambdaQueryWrapper<OaNotification> query = new LambdaQueryWrapper<>();
        query.eq(OaNotification::getUserId, userId);
        
        if (type != null && !type.isEmpty() && !"all".equals(type)) {
            query.eq(OaNotification::getType, type);
        }
        
        query.orderByDesc(OaNotification::getCreateTime);
        return notificationMapper.selectList(query);
    }

    @Override
    public void markAsRead(String notifId) {
        OaNotification notification = notificationMapper.selectById(notifId);
        if (notification != null) {
            notification.setIsRead(1);
            notificationMapper.updateById(notification);
        }
    }

    @Override
    public void markAllAsRead(String userId) {
        LambdaUpdateWrapper<OaNotification> update = new LambdaUpdateWrapper<>();
        update.eq(OaNotification::getUserId, userId)
              .set(OaNotification::getIsRead, 1);
        notificationMapper.update(null, update);
    }

    @Override
    public Long getUnreadCount(String userId) {
        return notificationMapper.selectCount(new LambdaQueryWrapper<OaNotification>()
                .eq(OaNotification::getUserId, userId)
                .eq(OaNotification::getIsRead, 0));
    }

    @Override
    public void createNotification(String userId, String type, String title, String content) {
        OaNotification notification = new OaNotification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(0);
        notification.setCreateTime(new Date());
        notificationMapper.insert(notification);
    }
}