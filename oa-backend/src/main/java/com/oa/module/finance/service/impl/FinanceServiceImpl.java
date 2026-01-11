package com.oa.module.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oa.module.finance.entity.OaExpenseClaim;
import com.oa.module.finance.entity.OaExpenseType;
import com.oa.module.finance.entity.OaInvoice;
import com.oa.module.finance.mapper.OaExpenseClaimMapper;
import com.oa.module.finance.mapper.OaExpenseTypeMapper;
import com.oa.module.finance.mapper.OaInvoiceMapper;
import com.oa.module.finance.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FinanceServiceImpl implements FinanceService {
    
    @Autowired
    private OaExpenseClaimMapper expenseClaimMapper;
    
    @Autowired
    private OaInvoiceMapper invoiceMapper;
    
    @Autowired
    private OaExpenseTypeMapper expenseTypeMapper;
    
    @Override
    @Transactional
    public OaExpenseClaim submitExpenseClaim(OaExpenseClaim claim, List<OaInvoice> invoices) {
        // ?
        String claimNo = generateClaimNo();
        claim.setClaimNo(claimNo);
        claim.setClaimDate(new Date());
        claim.setStatus("PENDING");
        claim.setPaymentStatus("UNPAID");
        claim.setCreateTime(new Date());
        claim.setUpdateTime(new Date());
        
        // ?
        expenseClaimMapper.insert(claim);
        
        // ?
        if (invoices != null && !invoices.isEmpty()) {
            for (OaInvoice invoice : invoices) {
                invoice.setClaimId(claim.getClaimId());
                invoice.setCreateTime(new Date());
                invoice.setUpdateTime(new Date());
                invoiceMapper.insert(invoice);
            }
        }
        
        return claim;
    }
    
    private String generateClaimNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        
        // ?
        QueryWrapper<OaExpenseClaim> wrapper = new QueryWrapper<>();
        wrapper.likeRight("CLAIM_NO", "EXP-" + dateStr);
        Long count = expenseClaimMapper.selectCount(wrapper);
        
        return String.format("EXP-%s-%03d", dateStr, count + 1);
    }
    
    @Override
    public List<OaExpenseClaim> getMyExpenseClaims(Long userId) {
        return expenseClaimMapper.selectByUserId(userId);
    }
    
    @Override
    public List<OaExpenseClaim> getPendingClaims(Long approverId) {
        return expenseClaimMapper.selectPendingClaims(approverId);
    }
    
    @Override
    @Transactional
    public OaExpenseClaim approveExpenseClaim(Long claimId, Long approverId, String status, String remark) {
        OaExpenseClaim claim = expenseClaimMapper.selectById(claimId);
        if (claim == null) {
            throw new RuntimeException("");
        }
        
        if (!"PENDING".equals(claim.getStatus())) {
            throw new RuntimeException("");
        }
        
        claim.setStatus(status);
        claim.setApproverId(approverId);
        claim.setApproveTime(new Date());
        claim.setApproveRemark(remark);
        claim.setUpdateTime(new Date());
        
        expenseClaimMapper.updateById(claim);
        return claim;
    }
    
    @Override
    public OaExpenseClaim getExpenseClaimDetail(Long claimId) {
        OaExpenseClaim claim = expenseClaimMapper.selectById(claimId);
        if (claim != null) {
            // ?
            List<OaInvoice> invoices = invoiceMapper.selectByClaimId(claimId);
            claim.setInvoices(invoices);
        }
        return claim;
    }
    
    @Override
    public List<OaExpenseType> getAllExpenseTypes() {
        QueryWrapper<OaExpenseType> wrapper = new QueryWrapper<>();
        wrapper.eq("STATUS", "ACTIVE");
        return expenseTypeMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional
    public OaExpenseType addExpenseType(OaExpenseType expenseType) {
        expenseType.setStatus("ACTIVE");
        expenseType.setCreateTime(new Date());
        expenseType.setUpdateTime(new Date());
        expenseTypeMapper.insert(expenseType);
        return expenseType;
    }
}