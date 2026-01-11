package com.oa.module.system.service.impl;

import com.oa.module.system.entity.SysConfig;
import com.oa.module.system.mapper.SysConfigMapper;
import com.oa.module.system.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统配置服务实现类
 * 使用本地缓存提高读取性能
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    
    @Autowired
    private SysConfigMapper configMapper;
    
    /**
     * 配置缓存
     */
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    
    /**
     * 初始化时加载所有配置到缓存
     */
    @PostConstruct
    public void init() {
        refreshCache();
    }
    
    @Override
    public String getValue(String key) {
        return cache.get(key);
    }
    
    @Override
    public String getValue(String key, String defaultValue) {
        String value = cache.get(key);
        if (value == null || value.isEmpty()) {
            // 尝试从数据库加载
            SysConfig config = configMapper.selectByKey(key);
            if (config != null && config.getConfigValue() != null) {
                cache.put(key, config.getConfigValue());
                return config.getConfigValue();
            }
            return defaultValue;
        }
        return value;
    }
    
    @Override
    public Integer getIntValue(String key, Integer defaultValue) {
        String value = getValue(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    @Override
    public Long getLongValue(String key, Long defaultValue) {
        String value = getValue(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    @Override
    public Boolean getBoolValue(String key, Boolean defaultValue) {
        String value = getValue(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(value) || "1".equals(value) || "yes".equalsIgnoreCase(value);
    }
    
    @Override
    public List<SysConfig> getAllConfigs() {
        return configMapper.selectList(null);
    }
    
    @Override
    public List<SysConfig> getConfigsByGroup(String group) {
        return configMapper.selectByGroup(group);
    }
    
    @Override
    public Map<String, String> getConfigMapByGroup(String group) {
        List<SysConfig> configs = getConfigsByGroup(group);
        Map<String, String> map = new HashMap<>();
        for (SysConfig config : configs) {
            map.put(config.getConfigKey(), config.getConfigValue());
        }
        return map;
    }
    
    @Override
    public boolean updateValue(String key, String value) {
        int rows = configMapper.updateValueByKey(key, value);
        if (rows > 0) {
            // 更新缓存
            cache.put(key, value);
            return true;
        }
        return false;
    }
    
    @Override
    public void refreshCache() {
        cache.clear();
        List<SysConfig> configs = configMapper.selectList(null);
        if (configs != null) {
            for (SysConfig config : configs) {
                if (config.getConfigKey() != null && config.getConfigValue() != null) {
                    cache.put(config.getConfigKey(), config.getConfigValue());
                }
            }
        }
        System.out.println("[ConfigService] Cache refreshed, loaded " + cache.size() + " configurations");
    }
    
    @Override
    public SysConfig getConfig(String key) {
        return configMapper.selectByKey(key);
    }
}
