package com.oa.module.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.attendance.entity.OaLeaveRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OaLeaveRequestMapper extends BaseMapper<OaLeaveRequest> {
    
    /**
     * ?
     */
    List<OaLeaveRequest> selectByUserId(@Param("userId") Long userId);
    
    /**
     * ?
     */
    List<OaLeaveRequest> selectPendingRequests(@Param("approverId") Long approverId);
}