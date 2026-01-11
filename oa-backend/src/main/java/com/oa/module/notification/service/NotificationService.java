package com.oa.module.notification.service;

import com.oa.module.notification.entity.OaNotification;

import java.util.List;

public interface NotificationService {
    List<OaNotification> getList(String userId, String type);
    
    void markAsRead(String notifId);
    
    void markAllAsRead(String userId);
    
    Long getUnreadCount(String userId);
    
    void createNotification(String userId, String type, String title, String content);
}