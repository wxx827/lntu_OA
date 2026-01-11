package com.oa.module.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oa.module.attendance.entity.*;
import com.oa.module.attendance.mapper.*;
import com.oa.module.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    
    @Autowired
    private OaAttendanceMapper attendanceMapper;
    
    @Autowired
    private OaLeaveRequestMapper leaveRequestMapper;
    
    @Autowired
    private OaOvertimeRequestMapper overtimeRequestMapper;
    
    @Autowired
    private OaBusinessTripMapper businessTripMapper;
    
    @Autowired
    private OaAttendanceSummaryMapper summaryMapper;
    
    @Override
    @Transactional
    public OaAttendance clockIn(Long userId, String location) {
        Date today = new Date();
        Date todayStart = getStartOfDay(today);
        
        // ?
        OaAttendance existing = attendanceMapper.selectByUserIdAndDate(userId, todayStart);
        if (existing != null && existing.getClockInTime() != null) {
            throw new RuntimeException("");
        }
        
        OaAttendance attendance = new OaAttendance();
        attendance.setUserId(userId);
        attendance.setWorkDate(todayStart);
        attendance.setClockInTime(today);
        attendance.setClockInLocation(location);
        attendance.setCreateTime(today);
        attendance.setUpdateTime(today);
        
        // ?(?:00)
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        
        if (hour > 9 || (hour == 9 && minute > 15)) {
            attendance.setIsLate(1);
            attendance.setStatus("LATE");
        } else {
            attendance.setIsLate(0);
            attendance.setStatus("NORMAL");
        }
        
        attendanceMapper.insert(attendance);
        return attendance;
    }
    
    @Override
    @Transactional
    public OaAttendance clockOut(Long userId, String location) {
        Date today = new Date();
        Date todayStart = getStartOfDay(today);
        
        // 
        OaAttendance attendance = attendanceMapper.selectByUserIdAndDate(userId, todayStart);
        if (attendance == null) {
            throw new RuntimeException("");
        }
        if (attendance.getClockOutTime() != null) {
            throw new RuntimeException("");
        }
        
        attendance.setClockOutTime(today);
        attendance.setClockOutLocation(location);
        attendance.setUpdateTime(today);
        
        // ?
        long diff = today.getTime() - attendance.getClockInTime().getTime();
        BigDecimal hours = new BigDecimal(diff).divide(new BigDecimal(3600000), 2, RoundingMode.HALF_UP);
        attendance.setWorkHours(hours);
        
        //  (?8:00)
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        
        if (hour < 18 || (hour == 18 && minute < 0)) {
            attendance.setIsEarlyLeave(1);
            if ("LATE".equals(attendance.getStatus())) {
                attendance.setStatus("LATE_AND_EARLY");
            } else {
                attendance.setStatus("EARLY_LEAVE");
            }
        } else {
            attendance.setIsEarlyLeave(0);
        }
        
        attendanceMapper.updateById(attendance);
        return attendance;
    }
    
    @Override
    public List<OaAttendance> getMyAttendanceRecords(Long userId, Date startDate, Date endDate) {
        return attendanceMapper.selectByUserIdAndDateRange(userId, startDate, endDate);
    }
    
    @Override
    public OaAttendance getTodayAttendance(Long userId) {
        Date today = getStartOfDay(new Date());
        return attendanceMapper.selectByUserIdAndDate(userId, today);
    }
    
    @Override
    @Transactional
    public OaLeaveRequest applyLeave(OaLeaveRequest request) {
        request.setStatus("PENDING");
        request.setCreateTime(new Date());
        request.setUpdateTime(new Date());
        leaveRequestMapper.insert(request);
        return request;
    }
    
    @Override
    public List<OaLeaveRequest> getMyLeaveRequests(Long userId) {
        QueryWrapper<OaLeaveRequest> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_ID", userId);
        wrapper.orderByDesc("CREATE_TIME");
        return leaveRequestMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional
    public OaLeaveRequest approveLeave(Long requestId, Long approverId, String status, String remark) {
        OaLeaveRequest request = leaveRequestMapper.selectById(requestId);
        if (request == null) {
            throw new RuntimeException("");
        }
        
        request.setStatus(status);
        request.setApproverId(approverId);
        request.setApproveTime(new Date());
        request.setApproveRemark(remark);
        request.setUpdateTime(new Date());
        
        leaveRequestMapper.updateById(request);
        return request;
    }
    
    @Override
    @Transactional
    public OaOvertimeRequest applyOvertime(OaOvertimeRequest request) {
        request.setStatus("PENDING");
        request.setCreateTime(new Date());
        request.setUpdateTime(new Date());
        overtimeRequestMapper.insert(request);
        return request;
    }
    
    @Override
    public List<OaOvertimeRequest> getMyOvertimeRequests(Long userId) {
        QueryWrapper<OaOvertimeRequest> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_ID", userId);
        wrapper.orderByDesc("CREATE_TIME");
        return overtimeRequestMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional
    public OaOvertimeRequest approveOvertime(Long requestId, Long approverId, String status, String remark) {
        OaOvertimeRequest request = overtimeRequestMapper.selectById(requestId);
        if (request == null) {
            throw new RuntimeException("");
        }
        
        request.setStatus(status);
        request.setApproverId(approverId);
        request.setApproveTime(new Date());
        request.setApproveRemark(remark);
        request.setUpdateTime(new Date());
        
        overtimeRequestMapper.updateById(request);
        return request;
    }
    
    @Override
    @Transactional
    public OaBusinessTrip applyBusinessTrip(OaBusinessTrip request) {
        request.setStatus("PENDING");
        request.setCreateTime(new Date());
        request.setUpdateTime(new Date());
        businessTripMapper.insert(request);
        return request;
    }
    
    @Override
    public List<OaBusinessTrip> getMyBusinessTrips(Long userId) {
        QueryWrapper<OaBusinessTrip> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_ID", userId);
        wrapper.orderByDesc("CREATE_TIME");
        return businessTripMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional
    public OaBusinessTrip approveBusinessTrip(Long tripId, Long approverId, String status, String remark) {
        OaBusinessTrip trip = businessTripMapper.selectById(tripId);
        if (trip == null) {
            throw new RuntimeException("");
        }
        
        trip.setStatus(status);
        trip.setApproverId(approverId);
        trip.setApproveTime(new Date());
        trip.setApproveRemark(remark);
        trip.setUpdateTime(new Date());
        
        businessTripMapper.updateById(trip);
        return trip;
    }
    
    @Override
    public OaAttendanceSummary getMonthlySummary(Long userId, String yearMonth) {
        return summaryMapper.selectByUserIdAndMonth(userId, yearMonth);
    }
    
    @Override
    @Transactional
    public OaAttendanceSummary generateMonthlySummary(Long userId, String yearMonth) {
        // ?
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date monthStart = null;
        Date monthEnd = null;
        
        try {
            monthStart = sdf.parse(yearMonth);
            Calendar cal = Calendar.getInstance();
            cal.setTime(monthStart);
            cal.add(Calendar.MONTH, 1);
            monthEnd = cal.getTime();
        } catch (Exception e) {
            throw new RuntimeException("");
        }
        
        List<OaAttendance> records = attendanceMapper.selectByUserIdAndDateRange(userId, monthStart, monthEnd);
        
        // 
        int actualDays = records.size();
        int lateTimes = 0;
        int earlyLeaveTimes = 0;
        
        for (OaAttendance record : records) {
            if (record.getIsLate() != null && record.getIsLate() == 1) {
                lateTimes++;
            }
            if (record.getIsEarlyLeave() != null && record.getIsEarlyLeave() == 1) {
                earlyLeaveTimes++;
            }
        }
        
        // ?
        QueryWrapper<OaLeaveRequest> leaveWrapper = new QueryWrapper<>();
        leaveWrapper.eq("USER_ID", userId);
        leaveWrapper.like("START_TIME", yearMonth);
        leaveWrapper.eq("STATUS", "APPROVED");
        List<OaLeaveRequest> leaves = leaveRequestMapper.selectList(leaveWrapper);
        BigDecimal leaveDays = BigDecimal.ZERO;
        for (OaLeaveRequest leave : leaves) {
            leaveDays = leaveDays.add(leave.getDays());
        }
        
        // ?
        QueryWrapper<OaOvertimeRequest> overtimeWrapper = new QueryWrapper<>();
        overtimeWrapper.eq("USER_ID", userId);
        overtimeWrapper.like("OVERTIME_DATE", yearMonth);
        overtimeWrapper.eq("STATUS", "APPROVED");
        List<OaOvertimeRequest> overtimes = overtimeRequestMapper.selectList(overtimeWrapper);
        BigDecimal overtimeHours = BigDecimal.ZERO;
        for (OaOvertimeRequest overtime : overtimes) {
            overtimeHours = overtimeHours.add(overtime.getHours());
        }
        
        // ?
        QueryWrapper<OaBusinessTrip> tripWrapper = new QueryWrapper<>();
        tripWrapper.eq("USER_ID", userId);
        tripWrapper.like("START_TIME", yearMonth);
        tripWrapper.eq("STATUS", "APPROVED");
        List<OaBusinessTrip> trips = businessTripMapper.selectList(tripWrapper);
        BigDecimal tripDays = BigDecimal.ZERO;
        for (OaBusinessTrip trip : trips) {
            tripDays = tripDays.add(trip.getDays());
        }
        
        // ?(22)
        int workDays = 22;
        
        // ?
        BigDecimal attendanceRate = new BigDecimal(actualDays)
                .divide(new BigDecimal(workDays), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        
        // ?
        OaAttendanceSummary summary = summaryMapper.selectByUserIdAndMonth(userId, yearMonth);
        if (summary == null) {
            summary = new OaAttendanceSummary();
            summary.setUserId(userId);
            summary.setYearMonth(yearMonth);
            summary.setCreateTime(new Date());
        }
        
        summary.setWorkDays(workDays);
        summary.setActualDays(actualDays);
        summary.setLateTimes(lateTimes);
        summary.setEarlyLeaveTimes(earlyLeaveTimes);
        summary.setAbsentDays(workDays - actualDays);
        summary.setLeaveDays(leaveDays);
        summary.setOvertimeHours(overtimeHours);
        summary.setTripDays(tripDays);
        summary.setAttendanceRate(attendanceRate);
        summary.setUpdateTime(new Date());
        
        if (summary.getSummaryId() == null) {
            summaryMapper.insert(summary);
        } else {
            summaryMapper.updateById(summary);
        }
        
        return summary;
    }
    
    @Override
    public Map<String, Object> getAttendanceStats(Long userId, String yearMonth) {
        OaAttendanceSummary summary = getMonthlySummary(userId, yearMonth);
        if (summary == null) {
            summary = generateMonthlySummary(userId, yearMonth);
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("workDays", summary.getWorkDays());
        stats.put("actualDays", summary.getActualDays());
        stats.put("lateTimes", summary.getLateTimes());
        stats.put("earlyLeaveTimes", summary.getEarlyLeaveTimes());
        stats.put("absentDays", summary.getAbsentDays());
        stats.put("leaveDays", summary.getLeaveDays());
        stats.put("overtimeHours", summary.getOvertimeHours());
        stats.put("tripDays", summary.getTripDays());
        stats.put("attendanceRate", summary.getAttendanceRate());
        
        return stats;
    }
    
    private Date getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}