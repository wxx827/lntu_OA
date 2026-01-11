package com.oa.module.finance.controller;

import com.oa.module.finance.entity.OaExpenseClaim;
import com.oa.module.finance.entity.OaExpenseType;
import com.oa.module.finance.entity.OaInvoice;
import com.oa.module.finance.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
@CrossOrigin
public class FinanceController {
    
    @Autowired
    private FinanceService financeService;
    
    /**
     * ?
     */
    @PostMapping("/expense/submit")
    public Map<String, Object> submitExpenseClaim(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaExpenseClaim claim = (OaExpenseClaim) request.get("claim");
            List<OaInvoice> invoices = (List<OaInvoice>) request.get("invoices");
            
            OaExpenseClaim saved = financeService.submitExpenseClaim(claim, invoices);
            result.put("success", true);
            result.put("message", "");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     */
    @GetMapping("/expense/my-claims")
    public Map<String, Object> getMyExpenseClaims(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaExpenseClaim> claims = financeService.getMyExpenseClaims(userId);
            result.put("success", true);
            result.put("data", claims);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ?
     */
    @GetMapping("/expense/pending")
    public Map<String, Object> getPendingClaims(@RequestParam Long approverId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaExpenseClaim> claims = financeService.getPendingClaims(approverId);
            result.put("success", true);
            result.put("data", claims);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ?
     */
    @PostMapping("/expense/approve")
    public Map<String, Object> approveExpenseClaim(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long claimId = Long.valueOf(request.get("claimId").toString());
            Long approverId = Long.valueOf(request.get("approverId").toString());
            String status = request.get("status").toString();
            String remark = request.get("remark") != null ? request.get("remark").toString() : "";
            
            OaExpenseClaim claim = financeService.approveExpenseClaim(claimId, approverId, status, remark);
            result.put("success", true);
            result.put("message", "");
            result.put("data", claim);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ?
     */
    @GetMapping("/expense/detail/{claimId}")
    public Map<String, Object> getExpenseClaimDetail(@PathVariable Long claimId) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaExpenseClaim claim = financeService.getExpenseClaimDetail(claimId);
            result.put("success", true);
            result.put("data", claim);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * ?
     */
    @GetMapping("/expense-types")
    public Map<String, Object> getAllExpenseTypes() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaExpenseType> types = financeService.getAllExpenseTypes();
            result.put("success", true);
            result.put("data", types);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}