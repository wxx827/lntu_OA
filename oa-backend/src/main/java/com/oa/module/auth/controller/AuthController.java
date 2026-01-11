package com.oa.module.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.auth.dto.LoginDTO;
import com.oa.module.system.entity.SysEmployee;
import com.oa.module.system.service.SysEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Login and User Info")
public class AuthController {

    @Autowired
    private SysEmployeeService employeeService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public Result<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
        SysEmployee employee = employeeService.getByUsername(loginDTO.getUsername());
        if (employee == null) {
            return Result.error("User not found");
        }
        
        String inputMd5 = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        
        if (!employee.getPassword().equals(inputMd5)) {
            if (!employee.getPassword().equals(loginDTO.getPassword())) {
                return Result.error("Invalid password");
            }
        }

        String token = jwtUtil.generateToken(employee.getEmpName(), employee.getEmpId());
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("role", employee.getRole() != null ? employee.getRole() : "EMPLOYEE");
        data.put("username", employee.getEmpName());
        return Result.success(data);
    }

    @PostMapping("/register")
    @Operation(summary = "Register")
    public Result<String> register(@RequestBody com.oa.module.auth.dto.RegisterDTO registerDTO) {
        SysEmployee existing = employeeService.getByUsername(registerDTO.getUsername());
        if (existing != null) {
            return Result.error("Username already exists");
        }

        SysEmployee employee = new SysEmployee();
        employee.setEmpName(registerDTO.getUsername());
        // MD5 encrypt password
        String md5Pwd = DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        employee.setPassword(md5Pwd);
        employee.setRole(registerDTO.getRole());
        employee.setSex(registerDTO.getSex());
        employee.setTel(registerDTO.getTel());
        employee.setEmail(registerDTO.getEmail());
        
        // Save using Mybatis-Plus service
        boolean success = employeeService.save(employee);
        if (success) {
            return Result.success("Registration successful");
        } else {
            return Result.error("Registration failed");
        }
    }

    @GetMapping("/userInfo")
    @Operation(summary = "Get User Info")
    public Result<SysEmployee> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtil.extractUsername(token);
            SysEmployee employee = employeeService.getByUsername(username);
            employee.setPassword(null); // Hide password
            return Result.success(employee);
        }
        return Result.error(401, "Unauthorized");
    }
}