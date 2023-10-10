package com.leave.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave.service.LeaveService;
import com.leave.userRequest.UserLeaveRequest;

@RestController
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	private LeaveService leaveService;
	
	@PutMapping("/applyLeave")
	public Object AddLeave(@RequestBody(required = false) UserLeaveRequest object){
		
//		return leaveService.AddLeave(object);
		return "apply leave working LeaveController";
	}
	@GetMapping("/AllEmployeesLeaveData")
	public Object AllEmployeesLeaveData() {
		return "AllEmployees Leave Data working ";
	}
	
}
