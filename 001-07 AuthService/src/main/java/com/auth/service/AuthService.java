package com.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.auth.userRequest.LoginRequest;
import com.auth.userRequest.employeeUserRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

	private static final String employeeBasePath="http://localhost:8083/employee";
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	public String welcome() {
		log.info("*********inside welcome AuthService");
		return restTemplate.getForObject(employeeBasePath+"/welcome",String.class);
	}
	public Object addEmployee(employeeUserRequest employe) {
		
		log.info("*********inside addEmployee AuthService");
		employe.setPassword(passwordEncoder.encode(employe.getPassword()));
		  ResponseEntity<String> response = restTemplate.exchange(employeeBasePath + "/insert", HttpMethod.POST, new HttpEntity<>(employe), String.class);

		  System.out.println("**************"+response.getBody()+"**********************");
		  return response.getBody();
	}
	public String Login(LoginRequest request) throws Exception {
		log.info("********inside Login AuthService");
		ResponseEntity<employeeUserRequest> employee=restTemplate.exchange(employeeBasePath+"/getById/"+request.getId(), HttpMethod.GET, new HttpEntity<LoginRequest>(request), employeeUserRequest.class);
		if(employee==null) {
			throw new Exception("Invalid User Details");
		}
		return jwtService.generteToken(String.valueOf(request.getId()));
	}

}
