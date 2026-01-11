package com.oa.module.dashboard.dto;

import lombok.Data;

@Data
public class TeamMemberDTO {
    private String userId;
    private String name;
    private String avatar;
    private String status; // "online", "busy", "offline"
    private String position;
}