package com.oa.module.dashboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectProgressDTO {
    private String name;
    private String desc;
    private Integer progress;
    private String color;
    private String status;
    private String statusType;
    private String icon;
    private String iconClass;
    private List<String> users;
    private String date;
}
