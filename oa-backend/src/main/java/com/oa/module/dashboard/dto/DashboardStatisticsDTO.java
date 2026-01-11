package com.oa.module.dashboard.dto;

import lombok.Data;

@Data
public class DashboardStatisticsDTO {
    // 
    private Integer pendingTasks;
    
    // ?
    private Integer teamPresent;
    
    // ?
    private Integer teamTotal;
    
    // ?
    private Integer systemVisits;

    private Integer todayMeetings;
    
    // ?
    private String attendanceStatus; // "? or "?
    
    // 
    private Integer leaveDays;
    
    // ?
    private Integer pendingApprovals;

    private Integer userCount;
    private Integer workflowCount;
    private Integer fileCount;
}