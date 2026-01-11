package com.oa.module.schedule.service;

import com.oa.entity.CalendarEvent;
import java.util.List;

/**
 * Schedule Service Interface
 */
public interface ScheduleService {
    
    /**
     * Get aggregated calendar events for a user
     * @param userId User ID
     * @param startDate Start date (YYYY-MM-DD)
     * @param endDate End date (YYYY-MM-DD)
     * @return List of calendar events
     */
    List<CalendarEvent> getMyEvents(Long userId, String startDate, String endDate);
}