package com.leave.userRequest;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserLeaveRequest {

	private Long id;
	private Long employeeId;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String reason;
}
