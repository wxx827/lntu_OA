package com.oa.module.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.attendance.entity.OaAttendanceSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OaAttendanceSummaryMapper extends BaseMapper<OaAttendanceSummary> {
    
    /**
     * ?
     */
    OaAttendanceSummary selectByUserIdAndMonth(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);
}