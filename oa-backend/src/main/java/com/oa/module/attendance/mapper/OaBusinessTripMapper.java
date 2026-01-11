package com.oa.module.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.attendance.entity.OaBusinessTrip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OaBusinessTripMapper extends BaseMapper<OaBusinessTrip> {
    
    /**
     * ?
     */
    List<OaBusinessTrip> selectByUserId(@Param("userId") Long userId);
    
    /**
     * ?
     */
    List<OaBusinessTrip> selectPendingRequests(@Param("approverId") Long approverId);
}