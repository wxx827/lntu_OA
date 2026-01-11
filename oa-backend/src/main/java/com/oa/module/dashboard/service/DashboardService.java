package com.oa.module.dashboard.service;

import com.oa.module.dashboard.dto.ActivityDTO;
import com.oa.module.dashboard.dto.AnnouncementDTO;
import com.oa.module.dashboard.dto.DashboardStatisticsDTO;
import com.oa.module.dashboard.dto.ProjectProgressDTO;
import com.oa.module.dashboard.dto.TeamMemberDTO;

import java.util.List;

public interface DashboardService {
    /**
     * Get dashboard statistics
     */
    DashboardStatisticsDTO getDashboardStatistics(String userId);
    
    /**
     * Get recent activities
     */
    List<ActivityDTO> getRecentActivities();
    
    /**
     * Get team status
     */
    List<TeamMemberDTO> getTeamStatus(String userId);
    
    /**
     * Get announcements
     */
    List<AnnouncementDTO> getAnnouncements();

    List<ProjectProgressDTO> getProjects();
}