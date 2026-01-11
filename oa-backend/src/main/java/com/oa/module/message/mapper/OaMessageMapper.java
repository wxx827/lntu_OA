package com.oa.module.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.message.entity.OaMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 内部消息Mapper接口
 */
@Mapper
public interface OaMessageMapper extends BaseMapper<OaMessage> {
    // MyBatis-Plus provides basic CRUD operations
    // Custom queries can be added here if needed
}
