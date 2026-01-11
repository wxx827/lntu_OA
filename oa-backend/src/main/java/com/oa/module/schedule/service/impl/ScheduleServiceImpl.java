package com.oa.module.schedule.service.impl;

import com.oa.entity.CalendarEvent;
import com.oa.module.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Schedule Service Implementation
 * Aggregates events from multiple sources (Meeting, Workflow, Car Application)
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    @Override
    public List<CalendarEvent> getMyEvents(Long userId, String startDate, String endDate) {
        List<CalendarEvent> events = new ArrayList<>();

        // 1. Get Meeting Bookings
        String meetingSql = "SELECT BOOKING_ID, TITLE, START_TIME, END_TIME, STATUS " +
                "FROM OA_MEETING_BOOKING " +
                "WHERE USER_ID = ? AND START_TIME BETWEEN STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d') " +
                "ORDER BY START_TIME";
        
        try {
            List<Map<String, Object>> meetings = jdbcTemplate.queryForList(meetingSql, userId, startDate, endDate);
            for (Map<String, Object> row : meetings) {
                CalendarEvent event = new CalendarEvent();
                event.setId("M" + row.get("BOOKING_ID"));
                event.setTitle((String) row.get("TITLE"));
                event.setStart(ISO_FORMAT.format(row.get("START_TIME")));
                event.setEnd(ISO_FORMAT.format(row.get("END_TIME")));
                event.setType("meeting");
                event.setStatus((String) row.get("STATUS"));
                events.add(event);
            }
        } catch (Exception e) {
            // Table might not exist yet, skip
        }

        // 2. Get Workflow Tasks (with due dates)
        String workflowSql = "SELECT WORKFLOW_ID, TITLE, DUE_DATE, STATUS " +
                "FROM OA_WORKFLOW " +
                "WHERE INITIATOR_ID = ? AND DUE_DATE BETWEEN STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d') " +
                "ORDER BY DUE_DATE";
        
        try {
            List<Map<String, Object>> workflows = jdbcTemplate.queryForList(workflowSql, userId, startDate, endDate);
            for (Map<String, Object> row : workflows) {
                CalendarEvent event = new CalendarEvent();
                event.setId("W" + row.get("WORKFLOW_ID"));
                event.setTitle((String) row.get("TITLE"));
                event.setStart(ISO_FORMAT.format(row.get("DUE_DATE")));
                event.setType("workflow");
                event.setStatus((String) row.get("STATUS"));
                events.add(event);
            }
        } catch (Exception e) {
            // Table might not exist yet, skip
        }

        // 3. Get Car Applications
        String carSql = "SELECT APPLICATION_ID, TITLE, START_TIME, END_TIME, STATUS " +
                "FROM OA_CAR_APPLICATION " +
                "WHERE USER_ID = ? AND START_TIME BETWEEN STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d') " +
                "ORDER BY START_TIME";
        
        try {
            List<Map<String, Object>> carApps = jdbcTemplate.queryForList(carSql, userId, startDate, endDate);
            for (Map<String, Object> row : carApps) {
                CalendarEvent event = new CalendarEvent();
                event.setId("C" + row.get("APPLICATION_ID"));
                event.setTitle((String) row.get("TITLE"));
                event.setStart(ISO_FORMAT.format(row.get("START_TIME")));
                event.setEnd(ISO_FORMAT.format(row.get("END_TIME")));
                event.setType("car");
                event.setStatus((String) row.get("STATUS"));
                events.add(event);
            }
        } catch (Exception e) {
            // Table might not exist yet, skip
        }

        return events;
    }
}