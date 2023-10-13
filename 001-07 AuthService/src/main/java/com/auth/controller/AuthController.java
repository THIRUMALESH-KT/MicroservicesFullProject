package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.service.AuthService;
import com.auth.userRequest.LoginRequest;
import com.auth.userRequest.employeeUserRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	@GetMapping("/welcome")
	public String welcome() {
		log.info("*******inside Welcome AuthController");
		return authService.welcome();
	}
	@GetMapping("/hello")
	public String hello() {
		log.info("*******inside hello AuthController");
		return "This is hello page from AuthoController";
	}
	@PostMapping("/addEmployee")
	public ResponseEntity<Object> addEmployee(@RequestBody employeeUserRequest employe) {
		log.info("*******inside addEmployee AuthController");
		return new ResponseEntity<Object>(authService.addEmployee(employe),HttpStatus.OK);
	}
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest request)throws Exception{
		log.info("inside login AuthController");
		return new ResponseEntity<String>(authService.Login(request),HttpStatus.OK);
	}
}
