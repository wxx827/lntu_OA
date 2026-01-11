package com.oa.module.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.hr.entity.OaPosition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OaPositionMapper extends BaseMapper<OaPosition> {
    
    /**
     * ?
     */
    List<OaPosition> selectPositionListWithDept();
    
    /**
     * ID?
     */
    List<OaPosition> selectByDeptId(@Param("deptId") Long deptId);
}