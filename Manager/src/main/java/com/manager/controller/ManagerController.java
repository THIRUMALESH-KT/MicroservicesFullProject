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
	public ResponseEntity<Object> Insert(@RequestBody(required = false) MangerUserRequest Manager) throws Exception{
		log.info("inside insert ManagerController");
		 
		return managerService.Insert(Manager);
	}
	
	
	//Get By Employe ID
	
	@GetMapping("/getById/{id}")
	public ResponseEntity<Object> getById(@PathVariable Long id) throws Exception{
	log.info("inside getById ManagerController");
		return managerService.GetById(id);
	}
	
	//Update By Id
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateById(@PathVariable Long id,@RequestBody(required = false) MangerUserRequest Manager) throws Exception{
		log.info("*********inside updateById manager Controller " );
		return managerService.UpdateById(id,Manager);
	}
	
	//Delete By Id
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<Object> deleteById(@PathVariable(required = false) Long id) throws Exception{
		return managerService.DeleteById(id);
	}
	@PutMapping("/addEmployeeId/{managerId}/{employeeId}")
	public ResponseEntity<Object> AddEMployeeId(@PathVariable("managerId")Long managerId,@PathVariable("employeeId")Long employeeId) throws Exception {
		log.info("*********inside AddEMployeeId ManagerController");

		return managerService.AddEmployeeId(managerId,employeeId);
	}
	@PutMapping("/removeEmployeeId/{managerId}/{employeeId}")
	public ResponseEntity<Object> RemoveEmployeeId(@PathVariable("managerId")Long managerId,@PathVariable("employeeId")Long employeeId) throws Exception {
		log.info("******inside removeEmployeeId ManagerController ");
		return managerService.RemoveEmployeeId(managerId,employeeId);
	}
}
