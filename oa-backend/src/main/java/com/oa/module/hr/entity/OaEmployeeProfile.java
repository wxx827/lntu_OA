package com.oa.module.hr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_EMPLOYEE_PROFILE", autoResultMap = true)
public class OaEmployeeProfile {
    @TableId(value = "PROFILE_ID", type = IdType.ASSIGN_ID)
    @TableField("PROFILE_ID")
    private Long profileId;
    
    @TableField("USER_ID")
    private Long userId;
    
    @TableField("DEPT_ID")
    private Long deptId;
    
    @TableField("POSITION_ID")
    private Long positionId;
    
    @TableField("ID_CARD")
    private String idCard;
    
    @TableField("GENDER")
    private String gender; // MALE, FEMALE
    
    @TableField("BIRTH_DATE")
    private Date birthDate;
    
    @TableField("EDUCATION")
    private String education; // HIGH_SCHOOL, COLLEGE, BACHELOR, MASTER, DOCTOR
    
    @TableField("MAJOR")
    private String major;
    
    @TableField("SCHOOL")
    private String school;
    
    @TableField("JOIN_DATE")
    private Date joinDate;
    
    @TableField("REGULAR_DATE")
    private Date regularDate;
    
    @TableField("WORK_STATUS")
    private String workStatus; // PROBATION, REGULAR, RESIGNED
    
    @TableField("EMERGENCY_CONTACT")
    private String emergencyContact;
    
    @TableField("EMERGENCY_PHONE")
    private String emergencyPhone;
    
    @TableField("ADDRESS")
    private String address;
    
    @TableField("BANK_NAME")
    private String bankName;
    
    @TableField("BANK_ACCOUNT")
    private String bankAccount;
    
    @TableField("CREATE_TIME")
    private Date createTime;
    
    @TableField("UPDATE_TIME")
    private Date updateTime;
    
    // ?
    @TableField(exist = false)
    private String userName; // 
    
    @TableField(exist = false)
    private String deptName; // ?
    
    @TableField(exist = false)
    private String positionName; // 
}