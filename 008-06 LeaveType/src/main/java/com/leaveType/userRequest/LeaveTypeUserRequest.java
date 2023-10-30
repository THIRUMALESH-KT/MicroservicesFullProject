package com.leaveType.userRequest;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LeaveTypeUserRequest {

	@NotEmpty(message = "LeaveCode Must Not Be Empty")
	private String leaveCode;
	@NotEmpty(message = "description Must Not Be Empty")
	private String description;
}
