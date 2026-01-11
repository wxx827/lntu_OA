package com.oa.module.system.controller;

import com.oa.common.Result;
import com.oa.module.system.entity.SysConfig;
import com.oa.module.system.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统配置管理控制器
 */
@Tag(name = "系统配置管理")
@RestController
@RequestMapping("/api/system/config")
public class ConfigController {
    
    @Autowired
    private ConfigService configService;
    
    /**
     * 获取所有配置
     */
    @Operation(summary = "获取所有配置")
    @GetMapping
    public Result<List<SysConfig>> getAllConfigs() {
        List<SysConfig> configs = configService.getAllConfigs();
        return Result.success(configs);
    }
    
    /**
     * 根据分组获取配置
     */
    @Operation(summary = "根据分组获取配置")
    @GetMapping("/group/{group}")
    public Result<List<SysConfig>> getConfigsByGroup(@PathVariable String group) {
        List<SysConfig> configs = configService.getConfigsByGroup(group);
        return Result.success(configs);
    }
    
    /**
     * 获取单个配置
     */
    @Operation(summary = "获取单个配置")
    @GetMapping("/{key}")
    public Result<SysConfig> getConfig(@PathVariable String key) {
        SysConfig config = configService.getConfig(key);
        if (config == null) {
            return Result.error("配置不存在: " + key);
        }
        return Result.success(config);
    }
    
    /**
     * 获取配置值
     */
    @Operation(summary = "获取配置值")
    @GetMapping("/value/{key}")
    public Result<String> getConfigValue(@PathVariable String key) {
        String value = configService.getValue(key);
        if (value == null) {
            return Result.error("配置不存在: " + key);
        }
        return Result.success(value);
    }
    
    /**
     * 更新配置值
     */
    @Operation(summary = "更新配置值")
    @PutMapping("/{key}")
    public Result<String> updateConfig(@PathVariable String key, @RequestBody Map<String, String> body) {
        String value = body.get("value");
        if (value == null) {
            return Result.error("缺少value参数");
        }
        boolean success = configService.updateValue(key, value);
        if (success) {
            return Result.success("配置更新成功");
        }
        return Result.error("配置更新失败，可能配置不存在");
    }
    
    /**
     * 刷新配置缓存
     */
    @Operation(summary = "刷新配置缓存")
    @PostMapping("/refresh")
    public Result<String> refreshCache() {
        configService.refreshCache();
        return Result.success("缓存刷新成功");
    }
}
