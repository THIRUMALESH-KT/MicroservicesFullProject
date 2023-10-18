package com.leave.controller;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.leave.entity.EmployeeLeave;
import com.leave.service.LeaveService;
import com.leave.userRequest.UserLeaveRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/leave")
@Slf4j
public class LeaveController {

	@Autowired
	private LeaveService leaveService;
	
	
	@GetMapping("/welcome")
	public String welcome() {
		log.info("*******inside Welcome AuthController");
		return "Hello This is welcomePage";
	}
	
	
	@PostMapping("/applyLeave/{employeeId}")
	public ResponseEntity<Map<String,Object>> applyLeave(@RequestBody(required = false) UserLeaveRequest reqest,HttpServletRequest request ,@PathVariable Long employeeId,@RequestParam(name="file",required = false)MultipartFile file)throws Exception{
		log.info("************inside ApplyLeave LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		EmployeeLeave leave= leaveService.ApplyLeave(reqest,request.getHeader("Authorization"),employeeId,file);
		map.put("for employe : ",leave.getEmployeeId());
		if(leave.getToDate()!=null) {
			map.put("total Days : ",  ChronoUnit.DAYS.between(leave.getFromDate(), leave.getToDate()));
			map.put("from : ", leave.getFromDate()	);
			map.put("toDate", leave.getToDate());
			
		}
		else {
			map.put("total Days : ", 1);
			map.put("leave date : ", leave.getFromDate());
		}
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	@PostMapping("/applyLeave")
	public ResponseEntity<Map<String , Object>> applyLeave(@RequestBody UserLeaveRequest leaveRequest,@RequestParam(name = "file",required = false)MultipartFile file, HttpServletRequest httpRequest,@PathVariable(required = false) Long employeeId)throws Exception{
		log.info("**********inside applyLeave2 LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
	EmployeeLeave leave=	leaveService.ApplyLeave(leaveRequest, httpRequest.getHeader("Authorization"), employeeId, file);
	map.put("for employe : ",leave.getEmployeeId());
	if(leave.getToDate()!=null) {
		map.put("total Days : ",  ChronoUnit.DAYS.between(leave.getFromDate(), leave.getToDate()));
		map.put("from : ", leave.getFromDate()	);
		map.put("toDate", leave.getToDate());
		
	}
	else {
		map.put("total Days : ", 1);
		map.put("leave date : ", leave.getFromDate());
	}
	map.put("Status : ", HttpStatus.OK);
	map.put("Code : ", HttpStatus.OK.value());
	
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	@GetMapping("/allEmployeesLeaveData")
	public ResponseEntity<Map<String, Object>> AllEmployeesLeaveData() {
		log.info("***********inside AllEmployeesLeaveData LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("Result : ",leaveService.getAllEmployeesLeaveData());
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, Object>> DeleteLeave(@PathVariable Long id) throws Exception{
		log.info("********inside Delete Leave LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("Message : ", "Leave Deleted sucefully");
		map.put("Result : ",leaveService.DeleteLeave(id));
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	@PutMapping("/approveLeave/{id}")
	public ResponseEntity<Map<String, Object>> ApproveLeave(@PathVariable Long id) throws Exception{
		log.info("**********inside ApproveLeave LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("Message : ", "Leave Approved sucefully");
		EmployeeLeave leave=leaveService.ApproveLeave(id);
		map.put("for employe : ",leave.getEmployeeId());
		if(leave.getToDate()!=null) {
			map.put("total Days : ",  ChronoUnit.DAYS.between(leave.getFromDate(), leave.getToDate()));
			map.put("from : ", leave.getFromDate()	);
			map.put("toDate", leave.getToDate());
			
		}
		else {
			map.put("total Days : ", 1);
			map.put("leave date : ", leave.getFromDate());
		}
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	@PutMapping("/rejectLeave/{id}")
	public ResponseEntity<Map<String, Object>> RejectLeave(@PathVariable Long id) throws Exception{
		log.info("**********inside RejectLeave LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("Message : ", "Leave Rejected sucefully");
		EmployeeLeave leave=leaveService.RejectLeave(id);
		map.put("for employe : ",leave.getEmployeeId());
		if(leave.getToDate()!=null) {
			map.put("total Days : ",  ChronoUnit.DAYS.between(leave.getFromDate(), leave.getToDate()));
			map.put("from : ", leave.getFromDate()	);
			map.put("toDate", leave.getToDate());
			
		}
		else {
			map.put("total Days : ", 1);
			map.put("leave date : ", leave.getFromDate());
		}
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	
}
