package com.oa.module.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oa.module.hr.entity.OaDepartment;
import com.oa.module.hr.entity.OaEmployeeProfile;
import com.oa.module.hr.entity.OaPosition;
import com.oa.module.hr.mapper.OaDepartmentMapper;
import com.oa.module.hr.mapper.OaEmployeeProfileMapper;
import com.oa.module.hr.mapper.OaPositionMapper;
import com.oa.module.hr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HrServiceImpl implements HrService {
    
    @Autowired
    private OaDepartmentMapper departmentMapper;
    
    @Autowired
    private OaPositionMapper positionMapper;
    
    @Autowired
    private OaEmployeeProfileMapper employeeProfileMapper;
    
    @Override
    public List<OaDepartment> getDepartmentList() {
        return departmentMapper.selectDepartmentListWithDetails();
    }
    
    @Override
    public List<OaDepartment> getDepartmentTree() {
        List<OaDepartment> allDepts = departmentMapper.selectList(null);
        return buildDepartmentTree(allDepts, null);
    }
    
    private List<OaDepartment> buildDepartmentTree(List<OaDepartment> allDepts, Long parentId) {
        List<OaDepartment> result = new ArrayList<>();
        
        for (OaDepartment dept : allDepts) {
            if ((parentId == null && dept.getParentId() == null) || 
                (parentId != null && parentId.equals(dept.getParentId()))) {
                
                Integer empCount = departmentMapper.countEmployeesByDeptId(dept.getDeptId());
                dept.setEmployeeCount(empCount);
                
                List<OaDepartment> children = buildDepartmentTree(allDepts, dept.getDeptId());
                if (!children.isEmpty()) {
                    dept.setChildren(children);
                }
                
                result.add(dept);
            }
        }
        
        return result.stream()
                .sorted((a, b) -> {
                    int orderA = a.getSortOrder() != null ? a.getSortOrder() : 0;
                    int orderB = b.getSortOrder() != null ? b.getSortOrder() : 0;
                    return Integer.compare(orderA, orderB);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public OaDepartment getDepartmentById(Long deptId) {
        OaDepartment dept = departmentMapper.selectById(deptId);
        if (dept != null) {
            Integer empCount = departmentMapper.countEmployeesByDeptId(deptId);
            dept.setEmployeeCount(empCount);
        }
        return dept;
    }
    
    @Override
    @Transactional
    public OaDepartment addDepartment(OaDepartment department) {
        department.setCreateTime(new Date());
        department.setUpdateTime(new Date());
        
        if (department.getParentId() != null) {
            OaDepartment parent = departmentMapper.selectById(department.getParentId());
            if (parent != null) {
                department.setDeptLevel(parent.getDeptLevel() + 1);
            }
        } else {
            department.setDeptLevel(1);
        }
        
        departmentMapper.insert(department);
        return department;
    }
    
    @Override
    @Transactional
    public OaDepartment updateDepartment(OaDepartment department) {
        department.setUpdateTime(new Date());
        departmentMapper.updateById(department);
        return department;
    }
    
    @Override
    @Transactional
    public void deleteDepartment(Long deptId) {
        QueryWrapper<OaDepartment> wrapper = new QueryWrapper<>();
        wrapper.eq("PARENT_ID", deptId);
        Long count = departmentMapper.selectCount(wrapper);
        
        if (count > 0) {
            throw new RuntimeException("Cannot delete department with sub-departments");
        }
        
        Integer empCount = departmentMapper.countEmployeesByDeptId(deptId);
        if (empCount > 0) {
            throw new RuntimeException("Cannot delete department with employees");
        }
        
        departmentMapper.deleteById(deptId);
    }
    
    @Override
    public List<OaPosition> getPositionList() {
        return positionMapper.selectPositionListWithDept();
    }
    
    @Override
    public List<OaPosition> getPositionsByDeptId(Long deptId) {
        return positionMapper.selectByDeptId(deptId);
    }
    
    @Override
    @Transactional
    public OaPosition addPosition(OaPosition position) {
        position.setCreateTime(new Date());
        position.setUpdateTime(new Date());
        positionMapper.insert(position);
        return position;
    }
    
    @Override
    @Transactional
    public OaPosition updatePosition(OaPosition position) {
        position.setUpdateTime(new Date());
        positionMapper.updateById(position);
        return position;
    }
    
    @Override
    @Transactional
    public void deletePosition(Long positionId) {
        QueryWrapper<OaEmployeeProfile> wrapper = new QueryWrapper<>();
        wrapper.eq("POSITION_ID", positionId);
        Long count = employeeProfileMapper.selectCount(wrapper);
        
        if (count > 0) {
            throw new RuntimeException("Cannot delete position with assigned employees");
        }
        
        positionMapper.deleteById(positionId);
    }
    
    @Override
    public OaEmployeeProfile getEmployeeProfile(Long userId) {
        return employeeProfileMapper.selectByUserId(userId);
    }
    
    @Override
    public List<OaEmployeeProfile> getEmployeeProfileList() {
        return employeeProfileMapper.selectProfileListWithDetails();
    }
    
    @Override
    public List<OaEmployeeProfile> getEmployeesByDeptId(Long deptId) {
        return employeeProfileMapper.selectByDeptId(deptId);
    }
    
    @Override
    @Transactional
    public OaEmployeeProfile createEmployeeProfile(OaEmployeeProfile profile) {
        profile.setCreateTime(new Date());
        profile.setUpdateTime(new Date());
        employeeProfileMapper.insert(profile);
        return profile;
    }
    
    @Override
    @Transactional
    public OaEmployeeProfile updateEmployeeProfile(OaEmployeeProfile profile) {
        profile.setUpdateTime(new Date());
        employeeProfileMapper.updateById(profile);
        return profile;
    }
}