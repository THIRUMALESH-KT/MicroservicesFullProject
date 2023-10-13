package com.leave.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leave.service.LeaveService;
import com.leave.userRequest.UserLeaveRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/leave")
@Slf4j
public class LeaveController {

	@Autowired
	private LeaveService leaveService;
	
	@PutMapping("/applyLeave/{id}")
	public ResponseEntity<String> applyLeave(@RequestBody(required = false) UserLeaveRequest reqest,@RequestParam("Authorization") String tokenHeader,@PathVariable Long employeeId)throws Exception{
		log.info("************inside ApplyLeave");
		return new ResponseEntity<String>(leaveService.ApplyLeave(reqest,tokenHeader,employeeId),HttpStatus.OK);
	}
	@GetMapping("/AllEmployeesLeaveData")
	public Object AllEmployeesLeaveData() {
		return "AllEmployees Leave Data working ";
	}
	
}
