package com.oa.module.material.controller;

import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.material.entity.OaMatApply;
import com.oa.module.material.entity.OaMaterial;
import com.oa.module.material.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/material")
@Tag(name = "Material Management", description = "Inventory and Application")
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    @Operation(summary = "Get material list")
    public Result<List<OaMaterial>> getList() {
        return Result.success(materialService.getList());
    }

    @PostMapping("/apply")
    @Operation(summary = "Submit material application")
    public Result<String> apply(@RequestBody OaMatApply apply, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String empId = jwtUtil.extractUserId(token);
        apply.setEmpId(empId);
        
        materialService.apply(apply);
        return Result.success("Application submitted");
    }

    @GetMapping("/apply/my")
    @Operation(summary = "Get my applications")
    public Result<List<OaMatApply>> getMyApply(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String empId = jwtUtil.extractUserId(token);
        return Result.success(materialService.getMyApply(empId));
    }
    
    // Admin only endpoint typically, but exposed for Phase 1 demo
    @PostMapping("/approve/{id}")
    @Operation(summary = "Approve application (Demo)")
    public Result<String> approve(@PathVariable String id) {
        materialService.approve(id);
        return Result.success("Approved");
    }
}