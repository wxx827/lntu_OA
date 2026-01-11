package com.oa.module.hr.service;

import com.oa.module.hr.entity.OaDepartment;
import com.oa.module.hr.entity.OaEmployeeProfile;
import com.oa.module.hr.entity.OaPosition;

import java.util.List;

public interface HrService {
    
    // ==========  ==========
    /**
     * ?
     */
    List<OaDepartment> getDepartmentList();
    
    /**
     * ?
     */
    List<OaDepartment> getDepartmentTree();
    
    /**
     * D?
     */
    OaDepartment getDepartmentById(Long deptId);
    
    /**
     * 
     */
    OaDepartment addDepartment(OaDepartment department);
    
    /**
     * 
     */
    OaDepartment updateDepartment(OaDepartment department);
    
    /**
     * 
     */
    void deleteDepartment(Long deptId);
    
    // ========== ?==========
    /**
     * ?
     */
    List<OaPosition> getPositionList();
    
    /**
     * ID?
     */
    List<OaPosition> getPositionsByDeptId(Long deptId);
    
    /**
     * 
     */
    OaPosition addPosition(OaPosition position);
    
    /**
     * 
     */
    OaPosition updatePosition(OaPosition position);
    
    /**
     * 
     */
    void deletePosition(Long positionId);
    
    // ========== ?==========
    /**
     * ID?
     */
    OaEmployeeProfile getEmployeeProfile(Long userId);
    
    /**
     * ?
     */
    List<OaEmployeeProfile> getEmployeeProfileList();
    
    /**
     * ID?
     */
    List<OaEmployeeProfile> getEmployeesByDeptId(Long deptId);
    
    /**
     * ?
     */
    OaEmployeeProfile createEmployeeProfile(OaEmployeeProfile profile);
    
    /**
     * ?
     */
    OaEmployeeProfile updateEmployeeProfile(OaEmployeeProfile profile);
}