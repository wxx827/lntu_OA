package com.oa.module.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.attendance.entity.OaAttendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface OaAttendanceMapper extends BaseMapper<OaAttendance> {
    
    /**
     * 
     */
    OaAttendance selectByUserIdAndDate(@Param("userId") Long userId, @Param("workDate") Date workDate);
    
    /**
     * ?
     */
    List<OaAttendance> selectByUserIdAndDateRange(@Param("userId") Long userId, 
                                                    @Param("startDate") Date startDate, 
                                                    @Param("endDate") Date endDate);
    
    /**
     * ?
     */
    Integer countLateByUserIdAndMonth(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);
    
    /**
     * 
     */
    Integer countEarlyLeaveByUserIdAndMonth(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);
    
    /**
     * 
     */
    Boolean hasTodayAttendance(@Param("userId") Long userId);
    
    /**
     * ?
     */
    Integer countTodayAttendanceByDept(@Param("deptId") Long deptId);
}