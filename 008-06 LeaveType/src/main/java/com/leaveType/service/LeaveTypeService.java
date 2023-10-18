package com.leaveType.service;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leaveType.entity.LeaveType;
import com.leaveType.repository.LeaveTypeRepositore;
import com.leaveType.userRequest.LeaveTypeUserRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaveTypeService {

	@Autowired
	private LeaveTypeRepositore leaveTypeRepositore;
	@Autowired 
	private JwtService jwtService;
	public Object CreateLeaveType(LeaveTypeUserRequest leaveTypeUserRequest, HttpServletRequest httprequest) {
		log.info("*********inside CreateLeaveType LeaveTypeService");
		Long employeeId=Long.valueOf(jwtService.extractEmployeeId( httprequest.getHeader("Authorization").substring(7)));
		LeaveType leaveType =new LeaveType(null, leaveTypeUserRequest.getLeaveCode(), leaveTypeUserRequest.getDescription(), LocalDate.now(), employeeId);
		return leaveTypeRepositore.save(leaveType);
	}

	public Object getAllLeaveTypes() {
		return leaveTypeRepositore.findAll();
	}

	public String getDescription(String id) throws Exception {
		String description=leaveTypeRepositore.findByLeaveCode(id);
		if(description==null)throw new Exception("invalid leave code");
		return description;
	}

}
