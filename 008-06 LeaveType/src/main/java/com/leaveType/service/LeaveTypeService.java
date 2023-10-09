package com.leaveType.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.leaveType.entity.LeaveType;
import com.leaveType.userRequest.LeaveTypeUserRequest;

public class LeaveTypeService {

	@Autowired
	private LeaveTypeRepositore leaveTypeRepositore;
	public Object CreateLeaveType(LeaveTypeUserRequest leaveTypeUserRequest) {
		LeaveType leaveType=new LeaveType();
		return leaveTypeRepositore.save(leaveType);
	}

	public Object getAllLeaveTypes() {
		return leaveTypeRepositore.findAll();
	}

}
