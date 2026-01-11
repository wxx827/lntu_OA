package com.oa.module.announcement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.module.announcement.entity.OaAnnouncement;
import com.oa.module.announcement.mapper.OaAnnouncementMapper;
import com.oa.module.announcement.service.AnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告服务实现类
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<OaAnnouncementMapper, OaAnnouncement> 
        implements AnnouncementService {
    
    @Override
    public List<OaAnnouncement> getPublishedList() {
        return this.list(new LambdaQueryWrapper<OaAnnouncement>()
                .eq(OaAnnouncement::getStatus, 1) // 已发布
                .orderByDesc(OaAnnouncement::getIsTop) // 置顶优先
                .orderByDesc(OaAnnouncement::getPublishTime)); // 按发布时间倒序
    }
    
    @Override
    public List<OaAnnouncement> getLatestAnnouncements(int limit) {
        return this.list(new LambdaQueryWrapper<OaAnnouncement>()
                .eq(OaAnnouncement::getStatus, 1)
                .orderByDesc(OaAnnouncement::getIsTop)
                .orderByDesc(OaAnnouncement::getPublishTime)
                .last("LIMIT " + limit));
    }
    
    @Override
    @Transactional
    public boolean publishAnnouncement(OaAnnouncement announcement) {
        announcement.setStatus(1); // 设置为已发布
        announcement.setPublishTime(LocalDateTime.now());
        
        if (announcement.getId() == null) {
            // 新建公告
            return this.save(announcement);
        } else {
            // 更新公告
            return this.updateById(announcement);
        }
    }
    
    @Override
    public boolean withdrawAnnouncement(String id) {
        OaAnnouncement announcement = this.getById(id);
        if (announcement != null) {
            announcement.setStatus(2); // 设置为已撤回
            return this.updateById(announcement);
        }
        return false;
    }
    
    @Override
    public boolean toggleTop(String id) {
        OaAnnouncement announcement = this.getById(id);
        if (announcement != null) {
            announcement.setIsTop(announcement.getIsTop() == 1 ? 0 : 1);
            return this.updateById(announcement);
        }
        return false;
    }
    
    @Override
    public void incrementViews(String id) {
        OaAnnouncement announcement = this.getById(id);
        if (announcement != null) {
            announcement.setViews((announcement.getViews() != null ? announcement.getViews() : 0) + 1);
            this.updateById(announcement);
        }
    }
    
    @Override
    public OaAnnouncement getDetail(String id) {
        OaAnnouncement announcement = this.getById(id);
        if (announcement != null) {
            // 增加浏览次数
            incrementViews(id);
        }
        return announcement;
    }
}
