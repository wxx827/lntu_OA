package com.oa.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.system.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 系统配置 Mapper 接口
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {
    
    /**
     * 根据配置键查询配置
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE CONFIG_KEY = #{key}")
    SysConfig selectByKey(@Param("key") String key);
    
    /**
     * 根据配置分组查询配置列表
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE CONFIG_GROUP = #{group}")
    List<SysConfig> selectByGroup(@Param("group") String group);
    
    /**
     * 更新配置值
     */
    @Update("UPDATE SYS_CONFIG SET CONFIG_VALUE = #{value}, UPDATE_TIME = NOW() WHERE CONFIG_KEY = #{key}")
    int updateValueByKey(@Param("key") String key, @Param("value") String value);
}
