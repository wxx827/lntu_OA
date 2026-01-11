package com.oa.module.hr.controller;

import com.oa.module.hr.entity.OaDepartment;
import com.oa.module.hr.entity.OaEmployeeProfile;
import com.oa.module.hr.entity.OaPosition;
import com.oa.module.hr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hr")
@CrossOrigin
public class HrController {
    
    @Autowired
    private HrService hrService;
    
    // ==========  ==========
    
    /**
     * ?
     */
    @GetMapping("/department/list")
    public Map<String, Object> getDepartmentList() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaDepartment> departments = hrService.getDepartmentList();
            result.put("success", true);
            result.put("data", departments);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ?
     */
    @GetMapping("/department/tree")
    public Map<String, Object> getDepartmentTree() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaDepartment> tree = hrService.getDepartmentTree();
            result.put("success", true);
            result.put("data", tree);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ?
     */
    @GetMapping("/department/{id}")
    public Map<String, Object> getDepartmentById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaDepartment department = hrService.getDepartmentById(id);
            result.put("success", true);
            result.put("data", department);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     */
    @PostMapping("/department/add")
    public Map<String, Object> addDepartment(@RequestBody OaDepartment department) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaDepartment saved = hrService.addDepartment(department);
            result.put("success", true);
            result.put("message", "");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     */
    @PutMapping("/department/update")
    public Map<String, Object> updateDepartment(@RequestBody OaDepartment department) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaDepartment updated = hrService.updateDepartment(department);
            result.put("success", true);
            result.put("message", "");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     */
    @DeleteMapping("/department/{id}")
    public Map<String, Object> deleteDepartment(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            hrService.deleteDepartment(id);
            result.put("success", true);
            result.put("message", "");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    // ========== ?==========
    
    /**
     * ?
     */
    @GetMapping("/position/list")
    public Map<String, Object> getPositionList() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaPosition> positions = hrService.getPositionList();
            result.put("success", true);
            result.put("data", positions);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ID?
     */
    @GetMapping("/position/dept/{deptId}")
    public Map<String, Object> getPositionsByDeptId(@PathVariable Long deptId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaPosition> positions = hrService.getPositionsByDeptId(deptId);
            result.put("success", true);
            result.put("data", positions);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     */
    @PostMapping("/position/add")
    public Map<String, Object> addPosition(@RequestBody OaPosition position) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaPosition saved = hrService.addPosition(position);
            result.put("success", true);
            result.put("message", "");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     */
    @PutMapping("/position/update")
    public Map<String, Object> updatePosition(@RequestBody OaPosition position) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaPosition updated = hrService.updatePosition(position);
            result.put("success", true);
            result.put("message", "");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     */
    @DeleteMapping("/position/{id}")
    public Map<String, Object> deletePosition(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            hrService.deletePosition(id);
            result.put("success", true);
            result.put("message", "");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    // ========== ?==========
    
    /**
     * ?
     */
    @GetMapping("/employee/profile/{userId}")
    public Map<String, Object> getEmployeeProfile(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaEmployeeProfile profile = hrService.getEmployeeProfile(userId);
            result.put("success", true);
            result.put("data", profile);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ?
     */
    @GetMapping("/employee/profile/list")
    public Map<String, Object> getEmployeeProfileList() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaEmployeeProfile> profiles = hrService.getEmployeeProfileList();
            result.put("success", true);
            result.put("data", profiles);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ID?
     */
    @GetMapping("/employee/dept/{deptId}")
    public Map<String, Object> getEmployeesByDeptId(@PathVariable Long deptId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaEmployeeProfile> employees = hrService.getEmployeesByDeptId(deptId);
            result.put("success", true);
            result.put("data", employees);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ?
     */
    @PostMapping("/employee/profile/create")
    public Map<String, Object> createEmployeeProfile(@RequestBody OaEmployeeProfile profile) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaEmployeeProfile saved = hrService.createEmployeeProfile(profile);
            result.put("success", true);
            result.put("message", "");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ?
     */
    @PutMapping("/employee/profile/update")
    public Map<String, Object> updateEmployeeProfile(@RequestBody OaEmployeeProfile profile) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaEmployeeProfile updated = hrService.updateEmployeeProfile(profile);
            result.put("success", true);
            result.put("message", "");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}