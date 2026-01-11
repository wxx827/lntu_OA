package com.oa.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.Result;
import com.oa.module.hr.entity.OaDepartment;
import com.oa.module.hr.service.HrService;
import com.oa.module.system.entity.SysConfig;
import com.oa.module.system.entity.SysEmployee;
import com.oa.module.system.mapper.SysEmployeeMapper;
import com.oa.module.system.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@CrossOrigin
public class SystemController {

    @Autowired
    private HrService hrService;

    @Autowired
    private SysEmployeeMapper employeeMapper;
    
    @Autowired(required = false)
    private ConfigService configService;

    @GetMapping("/employees")
    public Result<List<SysEmployee>> getEmployees(@RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<SysEmployee> queryWrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like(SysEmployee::getEmpName, keyword)
                    .or().like(SysEmployee::getEmail, keyword)
                    .or().like(SysEmployee::getTel, keyword);
        }
        List<SysEmployee> employees = employeeMapper.selectList(queryWrapper);
        employees.forEach(emp -> emp.setPassword(null));
        return Result.success(employees);
    }

    @GetMapping("/employees/stats")
    public Result<Map<String, Object>> getEmployeeStats() {
        Map<String, Object> stats = new java.util.HashMap<>();
        long total = employeeMapper.selectCount(null);
        stats.put("total", total);
        stats.put("active", total); // All employees are active by default
        stats.put("onLeave", 0);
        stats.put("newThisMonth", 0);
        return Result.success(stats);
    }

    @GetMapping("/employees/{id}")
    public Result<SysEmployee> getEmployee(@PathVariable String id) {
        SysEmployee employee = employeeMapper.selectById(id);
        if (employee != null) {
            employee.setPassword(null);
        }
        return Result.success(employee);
    }

    @GetMapping("/departments")
    public Result<List<OaDepartment>> getDepartmentTree() {
        List<OaDepartment> departments = hrService.getDepartmentTree();
        return Result.success(departments);
    }
    
    // ============ 系统配置管理接口 ============
    
    @GetMapping("/configs")
    public Result<List<SysConfig>> getAllConfigs() {
        if (configService == null) {
            return Result.error("配置服务不可用");
        }
        return Result.success(configService.getAllConfigs());
    }
    
    @GetMapping("/configs/group/{group}")
    public Result<List<SysConfig>> getConfigsByGroup(@PathVariable String group) {
        if (configService == null) {
            return Result.error("配置服务不可用");
        }
        return Result.success(configService.getConfigsByGroup(group));
    }
    
    @GetMapping("/configs/{key}")
    public Result<SysConfig> getConfig(@PathVariable String key) {
        if (configService == null) {
            return Result.error("配置服务不可用");
        }
        SysConfig config = configService.getConfig(key);
        if (config == null) {
            return Result.error("配置不存在: " + key);
        }
        return Result.success(config);
    }
    
    @PutMapping("/configs/{key}")
    public Result<String> updateConfig(@PathVariable String key, @RequestBody Map<String, String> body) {
        if (configService == null) {
            return Result.error("配置服务不可用");
        }
        String value = body.get("value");
        if (value == null) {
            return Result.error("缺少value参数");
        }
        boolean success = configService.updateValue(key, value);
        if (success) {
            return Result.success("配置更新成功");
        }
        return Result.error("配置更新失败");
    }
    
    @PostMapping("/configs/refresh")
    public Result<String> refreshCache() {
        if (configService == null) {
            return Result.error("配置服务不可用");
        }
        configService.refreshCache();
        return Result.success("缓存刷新成功");
    }
}
