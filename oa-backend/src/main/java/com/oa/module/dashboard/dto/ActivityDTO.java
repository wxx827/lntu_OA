package com.oa.module.dashboard.dto;

import lombok.Data;

@Data
public class ActivityDTO {
    private String title;
    private String description;
    private String timestamp;
    private String type; // "primary", "success", "warning", "info"
}