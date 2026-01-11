package com.oa.module.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import javax.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "OA_EXPENSE_CLAIM", autoResultMap = true)
public class OaExpenseClaim {
    @TableId(value = "CLAIM_ID", type = IdType.ASSIGN_ID)
    @TableField("CLAIM_ID")
    private Long claimId;
    
    @TableField("USER_ID")
    @NotNull(message = "D")
    private Long userId;
    
    @TableField("CLAIM_NO")
    private String claimNo;
    
    @TableField("EXPENSE_TYPE_ID")
    @NotNull(message = "")
    private Long expenseTypeId;
    
    @TableField("TOTAL_AMOUNT")
    @NotNull(message = "")
    @DecimalMin(value = "0.01", message = "")
    @DecimalMax(value = "100000.00", message = "?0")
    private BigDecimal totalAmount;
    
    @TableField("CLAIM_DATE")
    private Date claimDate;
    
    @TableField("DESCRIPTION")
    @NotBlank(message = "")
    @Size(max = 500, message = "500")
    private String description;
    
    @TableField("STATUS")
    private String status; // PENDING, APPROVED, REJECTED, CANCELLED
    
    @TableField("APPROVER_ID")
    private Long approverId;
    
    @TableField("APPROVE_TIME")
    private Date approveTime;
    
    @TableField("APPROVE_REMARK")
    private String approveRemark;
    
    @TableField("PAYMENT_STATUS")
    private String paymentStatus; // UNPAID, PAID, PROCESSING
    
    @TableField("PAYMENT_DATE")
    private Date paymentDate;
    
    @TableField("CREATE_TIME")
    private Date createTime;
    
    @TableField("UPDATE_TIME")
    private Date updateTime;
    
    // ?
    @TableField(exist = false)
    private String userName; // ?
    
    @TableField(exist = false)
    private String expenseTypeName; // ?
    
    @TableField(exist = false)
    private String approverName; // ?
    
    @TableField(exist = false)
    private List<OaInvoice> invoices; // ?
}