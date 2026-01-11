package com.oa.module.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.attendance.entity.OaOvertimeRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OaOvertimeRequestMapper extends BaseMapper<OaOvertimeRequest> {
    
    /**
     * ?
     */
    List<OaOvertimeRequest> selectByUserId(@Param("userId") Long userId);
    
    /**
     * ?
     */
    List<OaOvertimeRequest> selectPendingRequests(@Param("approverId") Long approverId);
}