package com.leave.userRequest;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserLeaveRequest {

	private LocalDate leaveDate;
	private Long employeeId;
	private String reason;
	
}
