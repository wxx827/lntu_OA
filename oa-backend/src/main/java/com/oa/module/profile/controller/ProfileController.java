package com.oa.module.profile.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.exception.BusinessException;
import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.profile.entity.OaActivityLog;
import com.oa.module.profile.mapper.OaActivityLogMapper;
import com.oa.module.system.entity.SysEmployee;
import com.oa.module.system.mapper.SysEmployeeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profile Management", description = "User Profile Management")
public class ProfileController {

    @Autowired
    private SysEmployeeMapper employeeMapper;
    
    @Autowired
    private OaActivityLogMapper activityLogMapper;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/info")
    @Operation(summary = "Get current user info")
    public Result<SysEmployee> getInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);
        
        SysEmployee employee = employeeMapper.selectById(userId);
        if (employee != null) {
            employee.setPassword(null);
        }
        return Result.success(employee);
    }

    @PutMapping("/update")
    @Operation(summary = "Update profile info")
    public Result<String> updateInfo(
            @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);
        
        SysEmployee employee = employeeMapper.selectById(userId);
        if (employee == null) {
            throw new BusinessException("User not found");
        }
        
        if (params.containsKey("tel")) {
            employee.setTel(params.get("tel"));
        }
        if (params.containsKey("email")) {
            employee.setEmail(params.get("email"));
        }
        
        employeeMapper.updateById(employee);
        
        logActivity(userId, "Update Profile", "Updated contact info", getClientIp(request));
        
        return Result.success("");
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change password")
    public Result<String> changePassword(
            @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);
        
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        SysEmployee employee = employeeMapper.selectById(userId);
        if (employee == null) {
            throw new BusinessException("User not found");
        }
        
        String oldPasswordMd5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!oldPasswordMd5.equals(employee.getPassword())) {
            throw new BusinessException("Incorrect old password");
        }
        
        String newPasswordMd5 = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        employee.setPassword(newPasswordMd5);
        employeeMapper.updateById(employee);
        
        logActivity(userId, "Change Password", "User changed password", getClientIp(request));
        
        return Result.success("Password changed successfully");
    }

    @GetMapping("/activity-log")
    @Operation(summary = "Get activity logs")
    public Result<List<OaActivityLog>> getActivityLog(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);
        
        List<OaActivityLog> logs = activityLogMapper.selectList(
            new LambdaQueryWrapper<OaActivityLog>()
                .eq(OaActivityLog::getUserId, userId)
                .orderByDesc(OaActivityLog::getCreateTime)
                .last("LIMIT 20")
        );
        
        return Result.success(logs);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get user stats")
    public Result<Map<String, Object>> getStats(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);
        
        Map<String, Object> stats = new HashMap<>();
        
        Long loginCount = activityLogMapper.selectCount(
            new LambdaQueryWrapper<OaActivityLog>()
                .eq(OaActivityLog::getUserId, userId)
                .eq(OaActivityLog::getAction, "Login")
        );
        stats.put("loginCount", loginCount);
        
        stats.put("initiatedWorkflows", 12);
        stats.put("attendanceRate", "98%");
        
        return Result.success(stats);
    }
    
    private void logActivity(String userId, String action, String detail, String ip) {
        OaActivityLog log = new OaActivityLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setDetail(detail);
        log.setIpAddress(ip);
        log.setCreateTime(new Date());
        activityLogMapper.insert(log);
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}