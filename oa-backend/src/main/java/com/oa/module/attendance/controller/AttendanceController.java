package com.oa.module.attendance.controller;

import com.oa.module.attendance.entity.*;
import com.oa.module.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @PostMapping("/clock-in")
    public Map<String, Object> clockIn(@RequestParam Long userId, 
                                        @RequestParam(required = false) String location) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaAttendance attendance = attendanceService.clockIn(userId, location);
            result.put("success", true);
            result.put("message", "Success");
            result.put("data", attendance);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/clock-out")
    public Map<String, Object> clockOut(@RequestParam Long userId, 
                                         @RequestParam(required = false) String location) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaAttendance attendance = attendanceService.clockOut(userId, location);
            result.put("success", true);
            result.put("message", "Success");
            result.put("data", attendance);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/today")
    public Map<String, Object> getTodayAttendance(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaAttendance attendance = attendanceService.getTodayAttendance(userId);
            result.put("success", true);
            result.put("data", attendance);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/my-records")
    public Map<String, Object> getMyRecords(@RequestParam Long userId,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaAttendance> records = attendanceService.getMyAttendanceRecords(userId, startDate, endDate);
            result.put("success", true);
            result.put("data", records);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/leave/apply")
    public Map<String, Object> applyLeave(@RequestBody OaLeaveRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaLeaveRequest saved = attendanceService.applyLeave(request);
            result.put("success", true);
            result.put("message", "Success");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/leave/my-requests")
    public Map<String, Object> getMyLeaveRequests(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaLeaveRequest> requests = attendanceService.getMyLeaveRequests(userId);
            result.put("success", true);
            result.put("data", requests);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/leave/approve")
    public Map<String, Object> approveLeave(@RequestParam Long requestId,
                                             @RequestParam Long approverId,
                                             @RequestParam String status,
                                             @RequestParam(required = false) String remark) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaLeaveRequest approved = attendanceService.approveLeave(requestId, approverId, status, remark);
            result.put("success", true);
            result.put("message", "Success");
            result.put("data", approved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/overtime/apply")
    public Map<String, Object> applyOvertime(@RequestBody OaOvertimeRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaOvertimeRequest saved = attendanceService.applyOvertime(request);
            result.put("success", true);
            result.put("message", "Success");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/overtime/my-requests")
    public Map<String, Object> getMyOvertimeRequests(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaOvertimeRequest> requests = attendanceService.getMyOvertimeRequests(userId);
            result.put("success", true);
            result.put("data", requests);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/overtime/approve")
    public Map<String, Object> approveOvertime(@RequestParam Long requestId,
                                                @RequestParam Long approverId,
                                                @RequestParam String status,
                                                @RequestParam(required = false) String remark) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaOvertimeRequest approved = attendanceService.approveOvertime(requestId, approverId, status, remark);
            result.put("success", true);
            result.put("message", "Success");
            result.put("data", approved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/trip/apply")
    public Map<String, Object> applyBusinessTrip(@RequestBody OaBusinessTrip request) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaBusinessTrip saved = attendanceService.applyBusinessTrip(request);
            result.put("success", true);
            result.put("message", "Success");
            result.put("data", saved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/trip/my-requests")
    public Map<String, Object> getMyBusinessTrips(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OaBusinessTrip> trips = attendanceService.getMyBusinessTrips(userId);
            result.put("success", true);
            result.put("data", trips);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/trip/approve")
    public Map<String, Object> approveBusinessTrip(@RequestParam Long tripId,
                                                    @RequestParam Long approverId,
                                                    @RequestParam String status,
                                                    @RequestParam(required = false) String remark) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaBusinessTrip approved = attendanceService.approveBusinessTrip(tripId, approverId, status, remark);
            result.put("success", true);
            result.put("message", "Success");
            result.put("data", approved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/summary/{userId}/{yearMonth}")
    public Map<String, Object> getMonthlySummary(@PathVariable Long userId, 
                                                  @PathVariable String yearMonth) {
        Map<String, Object> result = new HashMap<>();
        try {
            OaAttendanceSummary summary = attendanceService.getMonthlySummary(userId, yearMonth);
            if (summary == null) {
                summary = attendanceService.generateMonthlySummary(userId, yearMonth);
            }
            result.put("success", true);
            result.put("data", summary);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/stats/{userId}/{yearMonth}")
    public Map<String, Object> getAttendanceStats(@PathVariable Long userId, 
                                                   @PathVariable String yearMonth) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> stats = attendanceService.getAttendanceStats(userId, yearMonth);
            result.put("success", true);
            result.put("data", stats);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}