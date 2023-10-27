package com.leaveType.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leaveType.service.LeaveTypeService;
import com.leaveType.userRequest.LeaveTypeUserRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/leaveType")
@Slf4j
public class LeaveTypeController {

	@Autowired
	private LeaveTypeService leaveTypeService;
	@PostMapping("/addLeaveType")
	public ResponseEntity<Map<String, Object>> CreateLeaveType(@RequestBody LeaveTypeUserRequest leaveTypeUserRequest,HttpServletRequest httprequest){
		log.info("inside CreateLeaveType LeaveTypeController");
		Map<String, Object> map	=new HashMap<>();
		map.put("Message : ", "Leave Type Created sucefully");
		map.put("result", leaveTypeService.CreateLeaveType(leaveTypeUserRequest,httprequest));
		map.put("status", HttpStatus.OK	);
		map.put("code", HttpStatus.OK.value());
		
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		
	}
	@GetMapping("/getAllLeaveTypes")
	public ResponseEntity<Map<String, Object>> getAllLeaveTypes(){
		log.info("inside ApplyLeave LeaveTypeController");
		Map<String, Object> map	=new HashMap<>();
		map.put("result", leaveTypeService.getAllLeaveTypes());
		map.put("status", HttpStatus.OK	);
		map.put("code", HttpStatus.OK.value());
		
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	@GetMapping("/getDescription/{id}")
	public String getDescription(@PathVariable String id) throws Exception {
		log.info("*****inside getDescription LeaveTypeController");
		return leaveTypeService.getDescription(id);
	}
}
