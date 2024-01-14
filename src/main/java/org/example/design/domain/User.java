package org.example.design.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private String name;
    private String username;
    private String email;
    private String password;
    private String sex;
    private String college;
    private String id;
    private String phone;
    private Integer grade;
    private Integer status;
    private LocalDateTime time;
}
