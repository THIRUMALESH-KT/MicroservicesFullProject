package com.leave.exception;

import com.leave.userRequest.employeeUserRequest;

public class EmployeeNotFoundException extends Exception {

	public EmployeeNotFoundException(employeeUserRequest body) {
		// TODO Auto-generated constructor stub
		super(body.toString());
	}

	public EmployeeNotFoundException(String string) {
		// TODO Auto-generated constructor stub
	}

}
