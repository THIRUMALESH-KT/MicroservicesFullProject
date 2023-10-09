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

@RestController
@RequestMapping("/manager")
public class ManagerController {

	@Autowired
	private ManagerService employeeService;
	
	
	//Retrive All Employes
	
	@GetMapping("/getAllEmployes")
	public Object GetAllEmployes() throws Exception{
		return 	employeeService.GetAllEmployes()  ;
	}
	
	
	//insert Employe into data base
	
	@PostMapping("/insert")
	public Object Insert(@RequestBody MangerUserRequest Manager) throws Exception{

		 
		return employeeService.Insert(Manager);
	}
	
	
	//Get By Employe ID
	
	@GetMapping("/getById/{id}")
	public Object getById(@PathVariable Long id) throws Exception{
	
		return employeeService.GetById(id);
	}
	
	//Update By Id
	
	@PutMapping("/update")
	public Object updateById(@RequestParam Long id,@RequestBody MangerUserRequest Manager) throws Exception{
		return employeeService.UpdateById(id,Manager);
	}
	
	//Delete By Id
	@DeleteMapping("/deleteById")
	public Object deleteById(@RequestParam Long id) throws Exception{
		return employeeService.DeleteById(id);
	}
}
