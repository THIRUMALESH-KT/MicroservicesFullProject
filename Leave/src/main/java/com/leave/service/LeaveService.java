package com.leave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.entity.EmployeeLeave;
import com.leave.repository.LeaveRepository;
import com.leave.userRequest.UserLeaveRequest;

@Service
public class LeaveService {

	@Autowired
	private LeaveRepository repo;
	public EmployeeLeave AddLeave(UserLeaveRequest request) {
		EmployeeLeave leave=new EmployeeLeave();
		leave.setLeaveDate(request.getLeaveDate());
		leave.setEmployeeId(request.getEmployeeId());
		leave.setReason(request.getReason());
		
		return repo.save(leave);
	}

}
