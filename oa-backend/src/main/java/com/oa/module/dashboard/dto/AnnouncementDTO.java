package com.oa.module.dashboard.dto;

import lombok.Data;

@Data
public class AnnouncementDTO {
    private Long id;
    private String title;
    private String content;
    private String type; // "important", "normal"
    private String publishTime;
    private String publisher;
}