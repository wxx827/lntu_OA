package com.oa.module.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.workflow.entity.OaWorkflow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OaWorkflowMapper extends BaseMapper<OaWorkflow> {
    
    /**
     * Count pending tasks by user
     */
    Integer countPendingTasksByUser(@Param("userId") Long userId);
}