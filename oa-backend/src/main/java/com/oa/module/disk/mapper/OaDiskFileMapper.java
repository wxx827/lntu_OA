package com.oa.module.disk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.disk.entity.OaDiskFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 云盘文件Mapper接口
 */
@Mapper
public interface OaDiskFileMapper extends BaseMapper<OaDiskFile> {
    // MyBatis-Plus provides basic CRUD operations
    // Custom queries can be added here if needed
}
