package com.employe.userRequest;

import java.time.LocalDate;

import lombok.Data;
@Data
public class employeeUserRequest {
	private Long   employeeId;
    private String accesCode;
    private String name;
    private Long mobile;
    private String designation;
    private String password;
    private String email;
    private LocalDate startDate;
    private String skill;
    private Long   managerId;
}
