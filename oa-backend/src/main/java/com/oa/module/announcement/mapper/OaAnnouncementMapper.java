package com.oa.module.announcement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.announcement.entity.OaAnnouncement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 公告Mapper接口
 */
@Mapper
public interface OaAnnouncementMapper extends BaseMapper<OaAnnouncement> {
    
    /**
     * 增加浏览次数
     */
    @Update("UPDATE OA_ANNOUNCEMENT SET views = views + 1 WHERE id = #{id}")
    void incrementViews(Long id);
}
