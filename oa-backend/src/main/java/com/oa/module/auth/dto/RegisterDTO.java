package com.oa.module.auth.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String role; // ADMIN, EMPLOYEE, INTERN
    private String sex;
    private String tel;
    private String email;
}