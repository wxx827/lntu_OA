package com.oa.module.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.finance.entity.OaInvoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OaInvoiceMapper extends BaseMapper<OaInvoice> {
    
    /**
     * D?
     */
    List<OaInvoice> selectByClaimId(@Param("claimId") Long claimId);
}