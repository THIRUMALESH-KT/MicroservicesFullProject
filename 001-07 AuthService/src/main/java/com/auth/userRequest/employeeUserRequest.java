package com.auth.userRequest;

import lombok.Data;
@Data
public class employeeUserRequest {
	private Long   employeeId;
    private String accesCode;
    private String name;
    private String mobile;
    private String designation;
    private String password;
    private String email;
    private String startDate;
    private String skill;
    private Long   managerId;
}
