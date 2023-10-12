package com.employe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.employe.UserResponse.UserResponse;
import com.employe.entity.EmployeeMicroservices;
import com.employe.service.EmployeService;
import com.employe.userRequest.UserLeaveRequest;
import com.employe.userRequest.employeeUserRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeController {

	@Autowired
	private EmployeService employeeService;
	
	
	
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
	public Object GetAllEmployes() throws Exception{
		log.info("inside getAllEmployes EmployeController");

		return "getAllEmployes working EmployeeController";
	}
	
	
	//insert Employe into data base
	
	
	
	//Get By Employe ID
	
	@GetMapping("/getById/{id}")
	public String getById(@PathVariable(required = false) Long id) throws Exception{
		return "getBYId working EmployeeController";
	}
	
	//Update By Id
	
	@PutMapping("/update")
	public  Object updateById(@RequestParam(required = false) Long id,@RequestBody(required = false) EmployeeMicroservices employee) throws Exception{
		return "updateBYId working EmployeeController";
	}
	
	//Delete By Id
	@DeleteMapping("/deleteById")
	public  Object deleteById(@RequestParam(required = false) Long id) throws Exception{
		return "deleteByid working EmployeeController";
	}
	

	
}
