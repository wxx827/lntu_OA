package com.oa.module.attendance.service;

import com.oa.module.attendance.entity.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AttendanceService {
    
    // ==========  ==========
    /**
     * ?
     */
    OaAttendance clockIn(Long userId, String location);
    
    /**
     * 
     */
    OaAttendance clockOut(Long userId, String location);
    
    /**
     * 
     */
    List<OaAttendance> getMyAttendanceRecords(Long userId, Date startDate, Date endDate);
    
    /**
     * ?
     */
    OaAttendance getTodayAttendance(Long userId);
    
    // ==========  ==========
    /**
     * ?
     */
    OaLeaveRequest applyLeave(OaLeaveRequest request);
    
    /**
     * 
     */
    List<OaLeaveRequest> getMyLeaveRequests(Long userId);
    
    /**
     * ?
     */
    OaLeaveRequest approveLeave(Long requestId, Long approverId, String status, String remark);
    
    // ==========  ==========
    /**
     * ?
     */
    OaOvertimeRequest applyOvertime(OaOvertimeRequest request);
    
    /**
     * 
     */
    List<OaOvertimeRequest> getMyOvertimeRequests(Long userId);
    
    /**
     * ?
     */
    OaOvertimeRequest approveOvertime(Long requestId, Long approverId, String status, String remark);
    
    // ==========  ==========
    /**
     * ?
     */
    OaBusinessTrip applyBusinessTrip(OaBusinessTrip request);
    
    /**
     * 
     */
    List<OaBusinessTrip> getMyBusinessTrips(Long userId);
    
    /**
     * ?
     */
    OaBusinessTrip approveBusinessTrip(Long tripId, Long approverId, String status, String remark);
    
    // ==========  ==========
    /**
     * ?
     */
    OaAttendanceSummary getMonthlySummary(Long userId, String yearMonth);
    
    /**
     * ?
     */
    OaAttendanceSummary generateMonthlySummary(Long userId, String yearMonth);
    
    /**
     * 
     */
    Map<String, Object> getAttendanceStats(Long userId, String yearMonth);
}