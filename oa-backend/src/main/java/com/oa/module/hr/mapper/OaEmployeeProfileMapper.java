package com.oa.module.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.hr.entity.OaEmployeeProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OaEmployeeProfileMapper extends BaseMapper<OaEmployeeProfile> {
    
    /**
     * ID?
     */
    OaEmployeeProfile selectByUserId(@Param("userId") Long userId);
    
    /**
     * 
     */
    List<OaEmployeeProfile> selectProfileListWithDetails();
    
    /**
     * ID?
     */
    List<OaEmployeeProfile> selectByDeptId(@Param("deptId") Long deptId);
    
    /**
     * IDID
     */
    Long getDepartmentIdByUserId(@Param("userId") Long userId);
    
    /**
     * ?
     */
    Integer countByDepartmentId(@Param("deptId") Long deptId);
}