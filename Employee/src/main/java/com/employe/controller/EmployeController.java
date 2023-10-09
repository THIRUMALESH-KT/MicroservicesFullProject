package com.employe.controller;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.client.RestTemplate;

import com.employe.entity.EmployeeMicroservices;
import com.employe.service.EmployeService;
import com.employe.userRequest.UserLeaveRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeController {

	private static final String managerBaseUrl="http://MANAGER-SERVICE/manager";
	@Autowired
	private EmployeService employeeService;
	
	@Autowired
	private RestTemplate restTemplate;
	//Retrive All Employes
	
	@GetMapping("/getAllEmployes")
	public Object GetAllEmployes() throws Exception{
		log.info("inside getAllEmployes EmployeController");
		Map<String,Object> map=new HashMap<>();
		map.put(" Result  : ", " Sucess ");
		map.put(" Message  : ", employeeService.GetAllEmployes() );
		map.put(" Status : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	
	//insert Employe into data base
	
	@PostMapping("/insert")
	public Object Insert(@RequestBody EmployeeMicroservices employe) throws Exception{
		Map<String,Object> map=new HashMap<>();
		String getbyIdurl=managerBaseUrl+"/getById/"+employe.getManagerId();
		Object object=restTemplate.getForObject(getbyIdurl, Object.class);
		System.out.println(object.toString());
	//    Object emp	=employeeService.Insert(employe);
		Object emp=null;
	    System.out.println(emp.toString());
	    emp.toString();
		map.put(" Result  : ", " Sucess ");
		map.put(" Message  : ", emp );
		map.put(" Status : ", HttpStatus.OK.value());
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	
	//Get By Employe ID
	
	@GetMapping("/getById/{id}")
	public Object getById(@PathVariable Long id) throws Exception{
		return employeeService.GetById(id);
	}
	
	//Update By Id
	
	@PutMapping("/update")
	public  Object updateById(@RequestParam Long id,@RequestBody EmployeeMicroservices employee) throws Exception{
		return employeeService.UpdateById(id,employee);
	}
	
	//Delete By Id
	@DeleteMapping("/deleteById")
	public  Object deleteById(@RequestParam Long id) throws Exception{
		return employeeService.DeleteById(id);
	}
//	@PutMapping("/takeLeave")
//	public ResponseEntity<Map<String , Object>> TakeLeave(@RequestBody UserLeaveRequest object){
//		Map<String,Object> map=new HashMap<>();
//		map.put(" Result  : ", " Sucess ");
//		map.put(" Message  : ", employeeService.TakeLeave(object) );
//		map.put(" Status : ", HttpStatus.OK.value());
//		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
//	}
}
