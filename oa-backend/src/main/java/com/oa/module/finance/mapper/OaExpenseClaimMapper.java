package com.oa.module.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.finance.entity.OaExpenseClaim;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OaExpenseClaimMapper extends BaseMapper<OaExpenseClaim> {
    
    /**
     * ?
     */
    List<OaExpenseClaim> selectClaimListWithDetails();
    
    /**
     * ID?
     */
    List<OaExpenseClaim> selectByUserId(@Param("userId") Long userId);
    
    /**
     * ?
     */
    List<OaExpenseClaim> selectPendingClaims(@Param("approverId") Long approverId);
    
    /**
     * 
     */
    OaExpenseClaim selectByClaimNo(@Param("claimNo") String claimNo);
}