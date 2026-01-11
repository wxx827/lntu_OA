package com.oa.module.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "OA_INVOICE", autoResultMap = true)
public class OaInvoice {
    @TableId(value = "INVOICE_ID", type = IdType.ASSIGN_ID)
    @TableField("INVOICE_ID")
    private Long invoiceId;
    
    @TableField("CLAIM_ID")
    private Long claimId;
    
    @TableField("INVOICE_NO")
    private String invoiceNo;
    
    @TableField("INVOICE_TYPE")
    private String invoiceType; // VAT_SPECIAL, VAT_ORDINARY, RECEIPT, OTHER
    
    @TableField("AMOUNT")
    private BigDecimal amount;
    
    @TableField("INVOICE_DATE")
    private Date invoiceDate;
    
    @TableField("COMPANY_NAME")
    private String companyName;
    
    @TableField("TAX_NO")
    private String taxNo;
    
    @TableField("FILE_PATH")
    private String filePath;
    
    @TableField("REMARK")
    private String remark;
    
    @TableField("CREATE_TIME")
    private Date createTime;
    
    @TableField("UPDATE_TIME")
    private Date updateTime;
}