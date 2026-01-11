package com.oa.entity;

import lombok.Data;

/**
 * Calendar Event DTO for Schedule Aggregation
 */
@Data
public class CalendarEvent {
    private String id;           // Event unique ID (e.g., "M1", "W1", "C1")
    private String title;        // Event title
    private String start;        // Start time (ISO 8601 format)
    private String end;          // End time (optional)
    private String type;         // Event type: meeting, workflow, car
    private String description;  // Additional details
    private String status;       // Event status
}