package com.oa.module.finance.service;

import com.oa.module.finance.entity.OaExpenseClaim;
import com.oa.module.finance.entity.OaExpenseType;
import com.oa.module.finance.entity.OaInvoice;

import java.util.List;

public interface FinanceService {
    
    // ========== ?==========
    /**
     * ?
     */
    OaExpenseClaim submitExpenseClaim(OaExpenseClaim claim, List<OaInvoice> invoices);
    
    /**
     * 
     */
    List<OaExpenseClaim> getMyExpenseClaims(Long userId);
    
    /**
     * ?
     */
    List<OaExpenseClaim> getPendingClaims(Long approverId);
    
    /**
     * ?
     */
    OaExpenseClaim approveExpenseClaim(Long claimId, Long approverId, String status, String remark);
    
    /**
     * ?
     */
    OaExpenseClaim getExpenseClaimDetail(Long claimId);
    
    // ==========  ==========
    /**
     * ?
     */
    List<OaExpenseType> getAllExpenseTypes();
    
    /**
     * ?
     */
    OaExpenseType addExpenseType(OaExpenseType expenseType);
}