package com.employe.service;

import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.employe.entity.EmployeeMicroservices;
import com.employe.repository.EmployeeRepository;
import com.employe.userRequest.UserLeaveRequest;

@Service
public class EmployeService {
	 @Autowired
	    private RestTemplate restTemplate;
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public Object DeleteById(Long id) throws Exception {
		EmployeeMicroservices employee=GetById(id);
		employeeRepository.delete(employee);
		return "Employee Sucefully Deleted : "+id;
	}

	public EmployeeMicroservices UpdateById(Long id, EmployeeMicroservices employee) throws Exception {
		EmployeeMicroservices employee1=GetById(id);
		employee1.setDesignation(employee.getDesignation());
		employee1.setMobile(employee.getMobile());
		employee1.setName(employee.getPassword());
		
		return employeeRepository.save(employee1);
	}

	public EmployeeMicroservices GetById(Long id) throws Exception {
		EmployeeMicroservices employee=employeeRepository.findById(id).orElseThrow(()-> new Exception());
		return employee;
	}

	public EmployeeMicroservices Insert(EmployeeMicroservices employe) throws Exception {
		EmployeeMicroservices employee=employeeRepository.findByName(employe.getName());
		if(employee!=null) 
			throw new Exception("(Dublicate Employee) Employee already registred with this employee name : "+employe.getName());
		
		
		return employeeRepository.save(employe);
	}

	public Object GetAllEmployes() throws Exception {
		List<EmployeeMicroservices> list=employeeRepository.findAll();
		if(list==null) throw new Exception();
		return list;
	}

//	public Object TakeLeave(UserLeaveRequest object) {
//		 HttpHeaders headers = new HttpHeaders();
//	        headers.set("Content-Type", "application/json");
//	        HttpEntity<UserLeaveRequest> requestEntity = new HttpEntity<>(object, headers);
//
//        URI leaveServiceUrl = URI.create("http://localhost:8081/leave/addLeave"); // Replace with the appropriate URL
//        ResponseEntity<Object> responseEntity = restTemplate.exchange(leaveServiceUrl, HttpMethod.PUT, requestEntity, Object.class);
//        Object response = responseEntity.getBody();
//
//		return response;
//	}

	
}
