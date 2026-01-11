package com.oa.module.announcement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.module.announcement.entity.OaAnnouncement;
import java.util.List;

/**
 * 公告服务接口
 */
public interface AnnouncementService extends IService<OaAnnouncement> {
    
    /**
     * 获取所有已发布的公告列表(按置顶和发布时间排序)
     */
    List<OaAnnouncement> getPublishedList();
    
    /**
     * 获取最新的N条公告
     */
    List<OaAnnouncement> getLatestAnnouncements(int limit);
    
    /**
     * 发布公告
     */
    boolean publishAnnouncement(OaAnnouncement announcement);
    
    /**
     * 撤回公告
     */
    boolean withdrawAnnouncement(String id);
    
    /**
     * 置顶/取消置顶
     */
    boolean toggleTop(String id);
    
    /**
     * 增加浏览次数
     */
    void incrementViews(String id);
    
    /**
     * 获取公告详情
     */
    OaAnnouncement getDetail(String id);
}
