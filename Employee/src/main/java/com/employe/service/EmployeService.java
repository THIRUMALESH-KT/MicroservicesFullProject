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

import com.employe.dto.UserDetails;
import com.employe.entity.EmployeeMicroservices;
import com.employe.repository.EmployeeRepository;
import com.employe.userRequest.UserLeaveRequest;
import com.employe.userRequest.employeeUserRequest;

@Service
public class EmployeService {
	 @Autowired
	    private RestTemplate restTemplate;
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public Object DeleteById(Long id) throws Exception {
	return null;
	}

	public EmployeeMicroservices UpdateById(Long id, EmployeeMicroservices employee) throws Exception {
		
		
		return  null;
	}

	public EmployeeMicroservices GetById(Long id) throws Exception {
		return null;
	}

	public EmployeeMicroservices Insert(employeeUserRequest employe) throws Exception {
		
		return null;
	}

	public Object GetAllEmployes() throws Exception {
		
		return null;
	}

	public Object LoadUserDetails(Long id) throws Exception {


		EmployeeMicroservices empdetails=employeeRepository.findByEmployeeId(id);
		UserDetails userDetails=new UserDetails();
		userDetails.setEmployeeId(empdetails.getId());
		userDetails.setPassword(empdetails.getPassword());
		userDetails.setAccessCode(empdetails.getAccesCode());
	
		return userDetails;
	}



	
}
