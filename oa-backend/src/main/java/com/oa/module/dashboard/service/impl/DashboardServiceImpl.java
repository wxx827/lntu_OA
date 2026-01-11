
package com.oa.module.dashboard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.module.attendance.mapper.OaAttendanceMapper;
import com.oa.module.dashboard.dto.ActivityDTO;
import com.oa.module.dashboard.dto.AnnouncementDTO;
import com.oa.module.dashboard.dto.DashboardStatisticsDTO;
import com.oa.module.dashboard.dto.ProjectProgressDTO;
import com.oa.module.dashboard.dto.TeamMemberDTO;
import com.oa.module.dashboard.service.DashboardService;
import com.oa.module.dashboard.entity.OaProject;
import com.oa.module.dashboard.entity.OaActivity;
import com.oa.module.dashboard.mapper.OaProjectMapper;
import com.oa.module.dashboard.mapper.OaActivityMapper;

import com.oa.module.hr.mapper.OaEmployeeProfileMapper;
import com.oa.module.system.entity.SysEmployee;
import com.oa.module.system.service.SysEmployeeService;
import com.oa.module.workflow.entity.OaWorkflow;
import com.oa.module.workflow.mapper.OaWorkflowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.oa.module.announcement.entity.OaAnnouncement;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired(required = false)
    private OaWorkflowMapper workflowMapper;

    @Autowired(required = false)
    private OaAttendanceMapper attendanceMapper;

    @Autowired(required = false)
    private OaEmployeeProfileMapper employeeProfileMapper;

    @Autowired(required = false)
    private SysEmployeeService employeeService;

    @Autowired(required = false)
    private OaProjectMapper projectMapper;
    
    @Autowired(required = false)
    private OaActivityMapper activityMapper;
    
    @Autowired(required = false)
    private com.oa.module.announcement.service.AnnouncementService announcementService;

    @Override
    public DashboardStatisticsDTO getDashboardStatistics(String userId) {
        DashboardStatisticsDTO stats = new DashboardStatisticsDTO();

        try {
            int userCount = employeeService != null ? safeToInt(employeeService.count()) : 0;
            int workflowCount = workflowMapper != null ? safeToInt(workflowMapper.selectCount(null)) : 0;
            int fileCount = 0;

            stats.setUserCount(userCount);
            stats.setWorkflowCount(workflowCount);
            stats.setFileCount(fileCount);

            stats.setTeamTotal(userCount);
            stats.setSystemVisits(fileCount);

            int pendingTasks = 0;
            if (workflowMapper != null && userId != null && !userId.isEmpty()) {
                pendingTasks = safeToInt(workflowMapper.selectCount(new LambdaQueryWrapper<OaWorkflow>()
                        .eq(OaWorkflow::getCurrentApproverId, userId)
                        .eq(OaWorkflow::getStatus, 0)));
            }
            stats.setPendingTasks(pendingTasks);
            stats.setPendingApprovals(pendingTasks);

            stats.setTeamPresent(resolveTeamPresent(userId, userCount));
            stats.setAttendanceStatus(resolveAttendanceStatus(userId));
            stats.setLeaveDays(0);
        } catch (Exception e) {
            stats.setPendingTasks(0);
            stats.setTeamTotal(0);
            stats.setTeamPresent(0);
            stats.setSystemVisits(0);
            stats.setAttendanceStatus("Unknown");
            stats.setLeaveDays(0);
            stats.setPendingApprovals(0);
            stats.setUserCount(0);
            stats.setWorkflowCount(0);
            stats.setFileCount(0);
        }

        return stats;
    }

    @Override
    public List<ActivityDTO> getRecentActivities() {
        List<ActivityDTO> activities = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        // 从数据库读取动态
        if (activityMapper != null) {
            try {
                List<OaActivity> dbActivities = activityMapper.selectList(
                    new LambdaQueryWrapper<OaActivity>()
                        .orderByDesc(OaActivity::getCreateTime)
                        .last("limit 10")
                );
                for (OaActivity act : dbActivities) {
                    ActivityDTO dto = new ActivityDTO();
                    dto.setTitle(act.getTitle());
                    dto.setDescription(act.getDescription());
                    dto.setType(act.getType() != null ? act.getType() : "primary");
                    dto.setTimestamp(act.getCreateTime() != null ? sdf.format(act.getCreateTime()) : "");
                    activities.add(dto);
                }
                if (!activities.isEmpty()) {
                    return activities;
                }
            } catch (Exception e) {
                // fallback to default
            }
        }

        // 默认数据
        ActivityDTO activity1 = new ActivityDTO();
        activity1.setTitle("系统更新");
        activity1.setDescription("已升级至新版企业主题");
        activity1.setTimestamp(sdf.format(new Date(System.currentTimeMillis() - 2L * 24 * 60 * 60 * 1000)));
        activity1.setType("primary");
        activities.add(activity1);

        ActivityDTO activity2 = new ActivityDTO();
        activity2.setTitle("会议通知");
        activity2.setDescription("产品评审会 14:00");
        activity2.setTimestamp(sdf.format(new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000)));
        activity2.setType("success");
        activities.add(activity2);

        return activities;
    }

    @Override
    public List<TeamMemberDTO> getTeamStatus(String userId) {
        List<TeamMemberDTO> team = new ArrayList<>();
        if (employeeService == null) {
            return team;
        }

        List<SysEmployee> employees = employeeService.list(new LambdaQueryWrapper<SysEmployee>().last("limit 5"));
        for (SysEmployee employee : employees) {
            TeamMemberDTO member = new TeamMemberDTO();
            member.setUserId(employee.getEmpId());
            member.setName(employee.getEmpName());
            member.setAvatar(buildAvatarUrl(employee.getEmpName()));
            member.setStatus("online");
            member.setPosition(employee.getRole() != null ? employee.getRole() : "员工");
            team.add(member);
        }

        return team;
    }

    @Override
    public List<AnnouncementDTO> getAnnouncements() {
        List<AnnouncementDTO> announcements = new ArrayList<>();
        
        // 从公告服务获取最新公告
        if (announcementService != null) {
            try {
                List<OaAnnouncement> dbAnnouncements = announcementService.getLatestAnnouncements(5);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                
                for (OaAnnouncement ann : dbAnnouncements) {
                    AnnouncementDTO dto = new AnnouncementDTO();
                    dto.setId(ann.getId());
                    dto.setTitle(ann.getTitle());
                    dto.setContent(ann.getContent());
                    dto.setType(ann.getType());
                    dto.setPublishTime(ann.getPublishTime() != null ? 
                        sdf.format(java.sql.Timestamp.valueOf(ann.getPublishTime())) : "");
                    dto.setPublisher(ann.getPublisherName() != null ? ann.getPublisherName() : "系统管理员");
                    announcements.add(dto);
                }
                
                return announcements;
            } catch (Exception e) {
                // 如果查询失败,返回空列表
                return announcements;
            }
        }
        
        return announcements;
    }

    @Override
    public List<ProjectProgressDTO> getProjects() {
        List<ProjectProgressDTO> projects = new ArrayList<>();
        
        // 从PROJECT表加载数据
        if (projectMapper != null) {
            try {
                List<OaProject> dbProjects = projectMapper.selectList(
                    new LambdaQueryWrapper<OaProject>().orderByDesc(OaProject::getCreateTime)
                );
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for (OaProject p : dbProjects) {
                    ProjectProgressDTO dto = new ProjectProgressDTO();
                    dto.setName(p.getName());
                    dto.setDesc(p.getDescription());
                    dto.setProgress(p.getProgress() != null ? p.getProgress() : 0);
                    dto.setColor(p.getColor() != null ? p.getColor() : "#409eff");
                    dto.setStatus(p.getStatus() != null ? p.getStatus() : "进行中");
                    dto.setStatusType(getStatusType(p.getStatus()));
                    dto.setIcon(p.getIcon() != null ? p.getIcon() : "Platform");
                    dto.setIconClass(getIconClass(p.getColor()));
                    dto.setUsers(buildDefaultUsers());
                    dto.setDate(p.getDueDate() != null ? sdf.format(p.getDueDate()) : "");
                    projects.add(dto);
                }
                
                if (!projects.isEmpty()) {
                    return projects;
                }
            } catch (Exception e) {
                // fallback to default
            }
        }
        
        // 默认数据（当数据库表不存在时）
        ProjectProgressDTO project1 = new ProjectProgressDTO();
        project1.setName("前端界面优化");
        project1.setDesc("Vue3 布局与样式调整");
        project1.setProgress(85);
        project1.setColor("#409eff");
        project1.setStatus("进行中");
        project1.setStatusType("");
        project1.setIcon("Platform");
        project1.setIconClass("text-blue");
        project1.setUsers(buildDefaultUsers());
        project1.setDate("2026-01-20");
        projects.add(project1);

        ProjectProgressDTO project2 = new ProjectProgressDTO();
        project2.setName("后端接口开发");
        project2.setDesc("服务清理与数据对接");
        project2.setProgress(30);
        project2.setColor("#e6a23c");
        project2.setStatus("开发中");
        project2.setStatusType("warning");
        project2.setIcon("Box");
        project2.setIconClass("text-orange");
        project2.setUsers(buildDefaultUsers());
        project2.setDate("2026-02-15");
        projects.add(project2);

        ProjectProgressDTO project3 = new ProjectProgressDTO();
        project3.setName("AI 功能试点");
        project3.setDesc("模型集成测试");
        project3.setProgress(10);
        project3.setColor("#f56c6c");
        project3.setStatus("已计划");
        project3.setStatusType("info");
        project3.setIcon("DataAnalysis");
        project3.setIconClass("text-red");
        project3.setUsers(buildDefaultUsers());
        project3.setDate("2026-03-01");
        projects.add(project3);

        return projects;
    }
    
    private String getStatusType(String status) {
        if ("进行中".equals(status)) return "";
        if ("开发中".equals(status)) return "warning";
        if ("已计划".equals(status)) return "info";
        if ("已完成".equals(status)) return "success";
        return "";
    }
    
    private String getIconClass(String color) {
        if ("#409eff".equals(color)) return "text-blue";
        if ("#e6a23c".equals(color)) return "text-orange";
        if ("#f56c6c".equals(color)) return "text-red";
        if ("#67c23a".equals(color)) return "text-green";
        return "text-blue";
    }

    private int resolveTeamPresent(String userId, int userCount) {
        if (attendanceMapper != null && employeeProfileMapper != null) {
            Long userIdLong = parseLong(userId);
            if (userIdLong != null) {
                Long deptId = employeeProfileMapper.getDepartmentIdByUserId(userIdLong);
                if (deptId != null) {
                    Integer presentCount = attendanceMapper.countTodayAttendanceByDept(deptId);
                    if (presentCount != null) {
                        return presentCount;
                    }
                }
            }
        }
        return Math.min(userCount, 5);
    }

    private String resolveAttendanceStatus(String userId) {
        if (attendanceMapper != null) {
            Long userIdLong = parseLong(userId);
            if (userIdLong != null) {
                Boolean hasClockIn = attendanceMapper.hasTodayAttendance(userIdLong);
                return Boolean.TRUE.equals(hasClockIn) ? "已到岗" : "未到岗";
            }
        }
        return "未知";
    }

    private Long parseLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private int safeToInt(Long value) {
        if (value == null) {
            return 0;
        }
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : value.intValue();
    }

    private List<String> buildDefaultUsers() {
        List<String> users = new ArrayList<>();
        users.add(buildAvatarUrl("Admin"));
        users.add(buildAvatarUrl("Manager"));
        return users;
    }

    private String buildAvatarUrl(String name) {
        String safeName = name == null ? "User" : name;
        String encoded = URLEncoder.encode(safeName, StandardCharsets.UTF_8);
        return "https://ui-avatars.com/api/?name=" + encoded + "&background=409eff&color=fff";
    }
}
