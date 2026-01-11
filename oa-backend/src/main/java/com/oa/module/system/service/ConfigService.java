package com.oa.module.system.service;

import com.oa.module.system.entity.SysConfig;

import java.util.List;
import java.util.Map;

/**
 * 系统配置服务接口
 */
public interface ConfigService {
    
    /**
     * 获取配置值
     * @param key 配置键
     * @return 配置值，如果不存在返回 null
     */
    String getValue(String key);
    
    /**
     * 获取配置值，如果不存在返回默认值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    String getValue(String key, String defaultValue);
    
    /**
     * 获取整型配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 整型配置值
     */
    Integer getIntValue(String key, Integer defaultValue);
    
    /**
     * 获取长整型配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 长整型配置值
     */
    Long getLongValue(String key, Long defaultValue);
    
    /**
     * 获取布尔型配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 布尔值
     */
    Boolean getBoolValue(String key, Boolean defaultValue);
    
    /**
     * 获取所有配置
     * @return 配置列表
     */
    List<SysConfig> getAllConfigs();
    
    /**
     * 根据分组获取配置
     * @param group 分组名称
     * @return 配置列表
     */
    List<SysConfig> getConfigsByGroup(String group);
    
    /**
     * 根据分组获取配置Map
     * @param group 分组名称
     * @return key-value Map
     */
    Map<String, String> getConfigMapByGroup(String group);
    
    /**
     * 更新配置值
     * @param key 配置键
     * @param value 新值
     * @return 是否成功
     */
    boolean updateValue(String key, String value);
    
    /**
     * 刷新配置缓存
     */
    void refreshCache();
    
    /**
     * 获取单个配置实体
     * @param key 配置键
     * @return 配置实体
     */
    SysConfig getConfig(String key);
}
