package com.leave.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.multipart.MultipartFile;

import com.leave.entity.EmployeeLeave;
import com.leave.entity.EmployeeLeaveSummary;
import com.leave.helper.CustomAnnotation;
import com.leave.service.LeaveService;
import com.leave.userRequest.UserLeaveRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
	@CustomAnnotation(allowedRoles = {"1001","1002","1003","1004","1005","1006","1007"})
	public ResponseEntity<Object> applyLeave(  @RequestBody @Valid UserLeaveRequest reqest,HttpServletRequest request ,@PathVariable Long employeeId,@RequestParam(name="file",required = false)MultipartFile file)throws Exception{
		log.info("************inside ApplyLeave LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		ResponseEntity<EmployeeLeave> leave= (ResponseEntity<EmployeeLeave>) leaveService.ApplyLeave(reqest,request.getHeader("Authorization"),employeeId,file);
		
		if(leave.getStatusCode()==HttpStatus.BAD_REQUEST) {
			return ResponseEntity.badRequest().body(leave.getBody());
		}
		map.put("for employe : ",leave.getBody().getEmployeeId());
		
		if(leave.getBody().getToDate()!=null) {
			map.put("from : ", leave.getBody().getFromDate()	);
			map.put("toDate", leave.getBody().getToDate());
			
		}
		else {
			map.put("total Days : ", 1);
			map.put("leave date : ", leave.getBody().getFromDate());
		}
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		
		return ResponseEntity.ok(map);
	}
	@PostMapping("/applyLeave")
	@CustomAnnotation(allowedRoles = {"1001","1002","1003","1004","1005","1006","1007"})
	public ResponseEntity<Object> applyLeave(@Valid @RequestBody UserLeaveRequest leaveRequest,@RequestParam(name = "file",required = false)MultipartFile file, HttpServletRequest httpRequest,@PathVariable(required = false) Long employeeId)throws Exception{
		log.info("**********inside applyLeave2 LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
	ResponseEntity<EmployeeLeave> leave=	(ResponseEntity<EmployeeLeave>) leaveService.ApplyLeave(leaveRequest, httpRequest.getHeader("Authorization"), employeeId, file);
	if(leave.getStatusCode()==HttpStatus.BAD_REQUEST) {
		return ResponseEntity.badRequest().body(leave.getBody());
	}
	map.put("for employe : ",leave.getBody().getEmployeeId());
	if(leave.getBody().getToDate()!=null) {
		map.put("from : ", leave.getBody().getFromDate()	);
		map.put("toDate", leave.getBody().getToDate());
		
	}
	else {
		map.put("total Days : ", 1);
		map.put("leave date : ", leave.getBody().getFromDate());
	}
	map.put("Status : ", HttpStatus.OK);
	map.put("Code : ", HttpStatus.OK.value());
	
	
	return ResponseEntity.ok(map);	}
	@GetMapping("/allEmployeesLeaveData")
	@CustomAnnotation(allowedRoles = {"1004","1005","1006","1007"})
	public ResponseEntity<Map<String, Object>> AllEmployeesLeaveData(HttpServletRequest request) {
		log.info("***********inside AllEmployeesLeaveData LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("Result : ",leaveService.getAllEmployeesLeaveData(request));
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);	}
	@DeleteMapping("/delete/{id}")
	@CustomAnnotation(allowedRoles = {"1004","1005","1006","1007"})
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
	@CustomAnnotation(allowedRoles = {"1005","1006","1007"})

	public ResponseEntity<Map<String, Object>> ApproveLeave(@PathVariable Long id) throws Exception{
		log.info("**********inside ApproveLeave LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("Message : ", "Leave Approved sucefully");
		EmployeeLeave leave=leaveService.ApproveLeave(id);
		map.put("for employe : ",leave.getEmployeeId());
		if(leave.getToDate()!=null) {
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
	@CustomAnnotation(allowedRoles = {"1005","1006","1007"})

	public ResponseEntity<Map<String, Object>> RejectLeave(@PathVariable Long id) throws Exception{
		log.info("**********inside RejectLeave LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("Message : ", "Leave Rejected sucefully");
		EmployeeLeave leave=leaveService.RejectLeave(id);
		map.put("for employe : ",leave.getEmployeeId());
		if(leave.getToDate()!=null) {
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
	
	@GetMapping("/myLeaves")
	@CustomAnnotation(allowedRoles = {"1001","1002","1003","1004","1005","1006","1007"})

	public ResponseEntity<Map<String , Object>> MyLeaves(HttpServletRequest request) {
		log.info("**********inside MyLeaves LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("Message : ", "Leave Data fetched succesfully");
		map.put("Result : ",leaveService.MyLeaves(request));
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteMyLeave/{id}")
	@CustomAnnotation(allowedRoles = {"1001","1002","1003","1004","1005","1006","1007"})

	public ResponseEntity<Map<String, Object>> deleteMyLeave(@PathVariable Long id,HttpServletRequest request) throws Exception{
		log.info("**********inside deleteMyLeave LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("Message : ", "Your Leave Deleted  succesfully");
		map.put("Result : ",leaveService.deleteMyLeave(id,request));
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		
	}
	@GetMapping("/takenLeaves/{employeeId}/{leaveStatus}/{requeriedMonth}")
	@CustomAnnotation(allowedRoles = {"1005","1006","1007"})

	public ResponseEntity<Map<String , Object>> takenLeaves(@PathVariable Long employeeId,@PathVariable String leaveStatus,@PathVariable String requeriedMonth) {
		log.info("********inside takenLeaves LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("message ","Employee :"+ employeeId +" "+ leaveStatus+"Leave Data Fetched sucefully ");
		map.put("Date " , requeriedMonth);
		Float list=leaveService.takenLeaves(employeeId,leaveStatus,requeriedMonth);
		map.put("Result ", list);
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return ResponseEntity.ok(map);
	}
	@GetMapping("/monthlyLeave/{employeeId}/{leaveStatus}/{requeriedMonth}")
	@CustomAnnotation(allowedRoles = {"1005","1006","1007"})

	public ResponseEntity<Map<String , Object>> monthlyLeave(@PathVariable Long employeeId,@PathVariable String leaveStatus,@PathVariable String requeriedMonth) {
		log.info("********inside takenLeaves LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("message ","Employee :"+ employeeId +" "+ leaveStatus+"Leave Data Fetched sucefully ");
		map.put("Date " , requeriedMonth);
		map.put("LeaveStatus ", leaveStatus);
		List<EmployeeLeave> list=leaveService.monthlyLeave(employeeId,leaveStatus,requeriedMonth);
		map.put("Result ", list);
		map.put("ToalLeaves ", leaveService.takenLeaves(employeeId, leaveStatus, requeriedMonth));
		map.put("Status : ", HttpStatus.OK);
		map.put("Code : ", HttpStatus.OK.value());
		return ResponseEntity.ok(map);
	}
	@GetMapping("/monthlyLeave/{leaveStatus}/{requeriedMonth}")
	@CustomAnnotation(allowedRoles = {"1005","1006","1007"})
	public ResponseEntity<Map<String , Object>> HrLevelMonthlyLeave(@PathVariable String leaveStatus, @PathVariable String requeriedMonth ){
		log.info("**********inside HrLevelMOnthlyLeave LeaveController");
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("message ", "employee "+leaveStatus +" Leave Data Fethed Succesfully");
		map.put("Data ", requeriedMonth);
		map.put("LeaveStatus ", leaveStatus);
		List<EmployeeLeave> list=leaveService.HrLevelMonthyLeave(leaveStatus,requeriedMonth);
		map.put("Result ", list);
		map.put("status", HttpStatus.OK);
		map.put("Code ", HttpStatus.OK.value());
		return ResponseEntity.ok(map);
	}
	
}
