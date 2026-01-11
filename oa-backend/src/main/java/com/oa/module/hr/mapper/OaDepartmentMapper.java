package com.oa.module.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.hr.entity.OaDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OaDepartmentMapper extends BaseMapper<OaDepartment> {
    
    /**
     * ?
     */
    List<OaDepartment> selectDepartmentListWithDetails();
    
    /**
     * ?
     */
    List<OaDepartment> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * ?
     */
    List<OaDepartment> selectDepartmentTree();
    
    /**
     * ?
     */
    Integer countEmployeesByDeptId(@Param("deptId") Long deptId);
}