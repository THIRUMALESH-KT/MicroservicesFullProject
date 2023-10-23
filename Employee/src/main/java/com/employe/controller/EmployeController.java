package com.employe.controller;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.TimeLimitExceededException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
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
import org.springframework.web.client.RestTemplate;

import com.employe.entity.EmployeeMicroservices;
import com.employe.service.EmployeService;
import com.employe.userRequest.employeeUserRequest;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeController {

	@Autowired
	private EmployeService employeeService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/welcome")
	public String welcome() {
		log.info("******inside welcome EmployeeController");
		return "This is welcome page";
	}
	@PostMapping("/insert")
	public ResponseEntity<Map<String, Object>> Insert(@RequestBody employeeUserRequest employe) throws Exception{
		Map<String, Object> map=new HashMap<>();
		map.put("result", employeeService.Insert(employe));
		map.put("status", HttpStatus.OK);
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	//	return employeeService.Insert(employe);
	}
	
	@GetMapping("/getAllEmployes")
	public ResponseEntity<Map<String, Object>> GetAllEmployes(HttpServletRequest request) throws Exception{
		log.info("inside getAllEmployes EmployeController");
//        String authServiceUrl = "http://localhost:8087/auth/authenticate"; 
//        ResponseEntity<String> authServiceResponse = restTemplate.exchange(authServiceUrl, HttpMethod.GET, new HttpEntity<>(request), String.class);
//        log.info("****after authentication getAllEmployes EmployeController");
        Map<String, Object> map=new LinkedHashMap<>();
		map.put("Message : ", "All Employee Details Fetched Sucefullt ");
		map.put("Result : ", employeeService.GetAllEmployes());
		map.put("Status : ", HttpStatus.OK);
		map.put("code : ", HttpStatus.OK.value());
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	
	//insert Employe into data base
	
	
	
	//Get By Employe ID
	//@Role(value = 1001)
	@GetMapping("/getById/{id}")
	public EmployeeMicroservices getById(@PathVariable(required = false) Long id) throws Exception{
		
		log.info("********inside getById employeeController");
		return employeeService.GetById(id);
	}
	
	//Update By Id
	
	@PutMapping("/update/Personal/Details")
	public  Object updateById(@RequestParam(required = false) Long id,@RequestBody(required = false) EmployeeMicroservices employee) throws Exception{
		return "updateBYId working EmployeeController";
	}
	
	//Delete By Id
	@DeleteMapping("/deleteById")
	public  Object deleteById(@RequestParam(required = false) Long id) throws Exception{
		return "deleteByid working EmployeeController";
	}
	
	@GetMapping("/getHr")
	public EmployeeMicroservices getHr() throws Exception{
		log.info("***********inside getHr EmployeeController");
		return employeeService.getHr();
	}
	
	//  Password Reset
	@PostMapping("/password/reset/request")
	public ResponseEntity<Object> initiatePasswordReset(@RequestParam Long userId) throws UserPrincipalNotFoundException, MessagingException {
		log.info("**********inside initiatePasswordReset EmployeeController");
		EmployeeMicroservices employee=employeeService.initiatePasswordReset( userId);
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("result : ", "otp sent to your registered email "+employee.getEmail());
	    return ResponseEntity.ok(map);
	}
	
	// Password Confirmation
	
	// 1. User submits the received OTP and a new password (controller)
	@PostMapping("/password/reset/confirm")
	public ResponseEntity<Object> confirmPasswordReset(
		
	        @RequestParam Long userId, 
	        @RequestParam String otp, 
	        @RequestParam String newPassword) throws NotFoundException, TimeLimitExceededException {
		log.info("***********inside confirmPasswordReset EmployeeController");
		return ResponseEntity.ok(employeeService.confirmPasswordReset(userId,otp, newPassword));
	
	}

	@GetMapping("/getUser/{id}")
	public ResponseEntity<Object> getUser(@PathVariable Long id) throws UserPrincipalNotFoundException{
		log.info("********inside getUser Employee Controller");
		return ResponseEntity.ok(employeeService.getUser(id));
	}
	
}
