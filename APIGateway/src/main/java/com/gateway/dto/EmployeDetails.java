package com.gateway.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeDetails {
	private String employeeId;
	private String password;
	private String accessCode;
}
