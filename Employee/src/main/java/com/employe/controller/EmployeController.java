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
import org.springframework.web.client.RestTemplate;

import com.employe.entity.EmployeeMicroservices;
import com.employe.entity.UserPrinciples;
import com.employe.helper.CustomAnnotation;
import com.employe.helper.rolemapping;
import com.employe.service.EmployeService;
import com.employe.userRequest.employeeUserRequest;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
	public ResponseEntity<Object> Insert(@Validated @RequestBody employeeUserRequest employe) throws Exception{
		log.info("*********inside insert employeeController");
		return new ResponseEntity<Object>(employeeService.Insert(employe),HttpStatus.OK);
	//	return employeeService.Insert(employe);
	}
	

	@GetMapping("/getAllEmployesUnderMe")
	@CustomAnnotation(allowedRoles = {"1005","1006","1007"})
	public ResponseEntity<Map<String, Object>> getAllEmployesUnderMe(HttpServletRequest request) throws Exception{
		log.info("inside getAllEmployesUnderMe EmployeController");
//        String authServiceUrl = "http://localhost:8087/auth/authenticate"; 
//        ResponseEntity<String> authServiceResponse = restTemplate.exchange(authServiceUrl, HttpMethod.GET, new HttpEntity<>(request), String.class);
//        log.info("****after authentication getAllEmployes EmployeController");
        Map<String, Object> map=new LinkedHashMap<>();
		map.put("Message : ", "All Employee Details Fetched Sucefullt ");
		map.put("Result : ", employeeService.getAllEmployesUnderMe(request));
		map.put("Status : ", HttpStatus.OK);
		map.put("code : ", HttpStatus.OK.value());
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	@GetMapping("/getAllEmployees")
	@CustomAnnotation(allowedRoles = {"1007"})
	public ResponseEntity<Map<String, Object>> GetAllEmployes(HttpServletRequest request) throws Exception{
		log.info("inside getAllEmployes EmployeController");
        Map<String, Object> map=new LinkedHashMap<>();
		map.put("Message : ", "All Employee Details Fetched Sucefullt ");
		map.put("Result : ", employeeService.GetAllEmployes(request));
		map.put("Status : ", HttpStatus.OK);
		map.put("code : ", HttpStatus.OK.value());
		return new ResponseEntity<>(map,HttpStatus.OK);
		 
	}
	
	//insert Employe into data base
	
	
	
	
	//Get By Employe ID
	//@Role(value = 1001)
	//GetEmployee Details using id
	@GetMapping("/getOthers/{id}")
	@CustomAnnotation(allowedRoles = {"1005","1006","1007"} )
	public ResponseEntity<Object> getById(@PathVariable Long id) throws Exception{
		
		log.info("********inside getById employeeController");
		return employeeService.GetById(id);
	}
	
	
	//GetEmployeeDetails using Token
	@GetMapping("/getSelf")
	@CustomAnnotation(allowedRoles = {"1001","1002","1003","1004","1005","1006","1007"})
	public EmployeeMicroservices getById(HttpServletRequest request) {
		log.info("*********inside getById Using Tokin employeeController");
		return employeeService.GetById(request);
	}
	//Update By Id
	
	@PutMapping("/update/Employee/Details/{id}")
	@CustomAnnotation(allowedRoles = {"1004","1005","1006","1007"})

	public  Object updateById(@PathVariable(required = false) Long id,@Valid @RequestBody(required = false) employeeUserRequest employee) throws Exception{
		 Map<String, Object> map=new LinkedHashMap<>();
		 log.info("**********inside updateById employeController");
			map.put("Message : ", "Employee Details Updated sucefully ");
			map.put("Result : ", employeeService.UpdateById(id,employee));
			map.put("Status : ", HttpStatus.OK);
			map.put("code : ", HttpStatus.OK.value());
			return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	//Delete By Id
	@DeleteMapping("/deleteById/{id}")
	@CustomAnnotation(allowedRoles = {"1005","1006","1007"} )

	public  Object DeleteById(@PathVariable(required = false) Long id) throws Exception{
		 log.info("**********inside DeleteById employeController");

		Map<String, Object> map=new LinkedHashMap<>();
			map.put("Message : ", "Employee Deleted sucefully");
			map.put("Result : ", employeeService.DeleteById(id));
			map.put("Status : ", HttpStatus.OK);
			map.put("code : ", HttpStatus.OK.value());
			return new ResponseEntity<>(map,HttpStatus.OK);	}
	
	@GetMapping("/getHr")
	public ResponseEntity<EmployeeMicroservices> getHr() throws Exception{
		log.info("***********inside getHr EmployeeController");
		return (ResponseEntity<EmployeeMicroservices>) employeeService.getHr();
	}
	
	//  Password Reset
	@PostMapping("/password/reset/request")
	@CustomAnnotation(allowedRoles = {"1001","1002","1003","1004","1005","1006","1007"})

	public ResponseEntity<Object> initiatePasswordReset(@RequestParam(required = true) Long userId) throws UserPrincipalNotFoundException, MessagingException {
		log.info("**********inside initiatePasswordReset EmployeeController");
		EmployeeMicroservices employee=employeeService.initiatePasswordReset( userId);
		Map<String , Object> map=new LinkedHashMap<>();
		map.put("result : ", "otp sent to your registered email "+employee.getEmail());
	    return ResponseEntity.ok(map);
	}
	
	// Password Confirmation
	
	// 1. User submits the received OTP and a new password (controller)
	@PostMapping("/password/reset/confirm")
	@CustomAnnotation(allowedRoles = {"1001","1002","1003","1004","1005","1006","1007"})

	public ResponseEntity<Object> confirmPasswordReset(
		
	        @RequestParam(required = true) Long userId, 
	        @RequestParam(required = true) String otp, 
	        @RequestParam(required = true) String newPassword) throws NotFoundException, Exception {
		log.info("***********inside confirmPasswordReset EmployeeController");
		return ResponseEntity.ok(employeeService.confirmPasswordReset(userId,otp, newPassword));
	
	}

	@GetMapping("/getUser/{id}")
	public Object getUser(@PathVariable Long id) throws Exception{
		log.info("********inside getUser Employee Controller");
		return employeeService.getUser(id);
	}
	
	
	
}
