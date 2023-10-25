package com.manager.controller;

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

import com.manager.entity.Manager;
import com.manager.service.ManagerService;
import com.manager.userRequest.MangerUserRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/manager")
@Slf4j
public class ManagerController {

	@Autowired
	private ManagerService managerService;
	
	
	//Retrive All Employes
	
	@GetMapping("/getAllEmployes")
	public Object GetAllManagers() throws Exception{
//		return 	employeeService.GetAllEmployes()  ;
		return "getAllManagers working ManagerController";
	}
	
	
	//insert Employe into data base
	
	@PostMapping("/insert")
	public Object Insert(@RequestBody(required = false) MangerUserRequest Manager) throws Exception{
		log.info("inside insert ManagerController");
		 
		return managerService.Insert(Manager);
	}
	
	
	//Get By Employe ID
	
	@GetMapping("/getById/{id}")
	public ResponseEntity<Manager> getById(@PathVariable Long id) throws Exception{
	log.info("inside getById ManagerController");
		return new ResponseEntity<Manager>(managerService.GetById(id),HttpStatus.OK);
	}
	
	//Update By Id
	
	@PutMapping("/update/{id}")
	public Object updateById(@PathVariable Long id,@RequestBody(required = false) MangerUserRequest Manager) throws Exception{
		log.info("*********inside updateById manager Controller " );
		return managerService.UpdateById(id,Manager);
	}
	
	//Delete By Id
	@DeleteMapping("/deleteById/{id}")
	public Object deleteById(@PathVariable(required = false) Long id) throws Exception{
		return managerService.DeleteById(id);
	}
	@PutMapping("/addEmployeeId/{managerId}/{employeeId}")
	public void AddEMployeeId(@PathVariable("managerId")Long managerId,@PathVariable("employeeId")Long employeeId) throws Exception {
		log.info("*********inside AddEMployeeId ManagerController");

		managerService.AddEmployeeId(managerId,employeeId);
	}
	@PutMapping("/removeEmployeeId/{managerId}/{employeeId}")
	public void RemoveEmployeeId(@PathVariable("managerId")Long managerId,@PathVariable("employeeId")Long employeeId) throws Exception {
		log.info("******inside removeEmployeeId ManagerController ");
		managerService.RemoveEmployeeId(managerId,employeeId);
	}
}
