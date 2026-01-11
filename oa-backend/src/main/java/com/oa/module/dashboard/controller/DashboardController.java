package com.oa.module.dashboard.controller;

import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.dashboard.dto.ActivityDTO;
import com.oa.module.dashboard.dto.AnnouncementDTO;
import com.oa.module.dashboard.dto.DashboardStatisticsDTO;
import com.oa.module.dashboard.dto.ProjectProgressDTO;
import com.oa.module.dashboard.dto.TeamMemberDTO;
import com.oa.module.dashboard.entity.OaActivity;
import com.oa.module.dashboard.mapper.OaActivityMapper;
import com.oa.module.dashboard.service.DashboardService;

import com.oa.module.meeting.entity.OaMeetingBook;
import com.oa.module.meeting.service.MeetingService;
import com.oa.module.system.service.SysEmployeeService;
import com.oa.module.workflow.mapper.OaWorkflowMapper;
import com.oa.module.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired(required = false)
    private WorkflowService workflowService;

    @Autowired(required = false)
    private SysEmployeeService employeeService;

    @Autowired(required = false)
    private MeetingService meetingService;

    @Autowired(required = false)
    private OaWorkflowMapper workflowMapper;

    @Autowired(required = false)
    private OaActivityMapper activityMapper;

    @Autowired(required = false)
    private JwtUtil jwtUtil;

    @GetMapping("/statistics")
    public Result<DashboardStatisticsDTO> getStatistics(HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        DashboardStatisticsDTO statistics = dashboardService.getDashboardStatistics(userId);

        if (workflowService != null) {
            int pendingTasks = workflowService.getMyTasks(userId, null, null).size();
            statistics.setPendingTasks(pendingTasks);
            statistics.setPendingApprovals(pendingTasks);
        }

        if (employeeService != null) {
            int userCount = safeToInt(employeeService.count());
            statistics.setUserCount(userCount);
            statistics.setTeamTotal(userCount);
        }

        if (workflowMapper != null) {
            statistics.setWorkflowCount(safeToInt(workflowMapper.selectCount(null)));
        }



        if (meetingService != null) {
            statistics.setTodayMeetings(countTodayMeetings(userId));
        }

        return Result.success(statistics);
    }

    @GetMapping("/activities")
    public Result<List<ActivityDTO>> getActivities() {
        return Result.success(dashboardService.getRecentActivities());
    }

    @GetMapping("/team-status")
    public Result<List<TeamMemberDTO>> getTeamStatus(HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        return Result.success(dashboardService.getTeamStatus(userId));
    }

    @GetMapping("/announcements")
    public Result<List<AnnouncementDTO>> getAnnouncements() {
        return Result.success(dashboardService.getAnnouncements());
    }

    @GetMapping("/projects")
    public Result<List<ProjectProgressDTO>> getProjects() {
        return Result.success(dashboardService.getProjects());
    }

    @PostMapping("/activity")
    public Result<?> createActivity(@RequestBody Map<String, String> params, HttpServletRequest request) {
        if (activityMapper == null) {
            return Result.error("Activity service not available");
        }
        try {
            OaActivity activity = new OaActivity();
            activity.setId("ACT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            activity.setTitle(params.get("title"));
            activity.setDescription(params.get("description"));
            activity.setType(params.getOrDefault("type", "primary"));
            String userId = getUserIdFromRequest(request);
            activity.setUserId(userId);
            activity.setUserName(params.getOrDefault("userName", "系统"));
            activity.setCreateTime(new Date());
            activityMapper.insert(activity);
            return Result.success("发布成功");
        } catch (Exception e) {
            return Result.error("发布失败: " + e.getMessage());
        }
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ") && jwtUtil != null) {
            return jwtUtil.extractUserId(authHeader.substring(7));
        }
        String userIdParam = request.getParameter("userId");
        if (userIdParam != null && !userIdParam.isEmpty()) {
            return userIdParam;
        }
        return "E001";
    }

    private int safeToInt(Long value) {
        if (value == null) {
            return 0;
        }
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : value.intValue();
    }

    private int countTodayMeetings(String userId) {
        List<OaMeetingBook> bookings = meetingService.getMyBookings(userId, null);
        if (bookings == null || bookings.isEmpty()) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        Date start = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        int count = 0;
        for (OaMeetingBook booking : bookings) {
            Date startTime = booking.getStartTime();
            if (startTime != null && !startTime.before(start) && startTime.before(end)) {
                count++;
            }
        }
        return count;
    }
}
