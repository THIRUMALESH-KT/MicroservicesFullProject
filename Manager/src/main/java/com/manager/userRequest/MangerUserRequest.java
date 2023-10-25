package com.manager.userRequest;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MangerUserRequest {
	
	private Long   employeeId;
	private Long managerId;
    private String name;
    private String mobile;
    private String designation;
    private String email;
    private LocalDate startDate;
    private String skill;
}
