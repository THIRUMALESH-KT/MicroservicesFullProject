package com.auth.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.service.AuthService;
import com.auth.service.JwtService;
import com.auth.userRequest.LoginRequest;
import com.auth.userRequest.employeeUserRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtService jwtService;
	
	@GetMapping("/welcome")
	public String welcome() {
		log.info("*******inside Welcome AuthController");
		return "Hello This is welcomePage";
	}
	@GetMapping("/hello")
	public String hello() {
		log.info("*******inside hello AuthController");
		return "This is hello page from AuthoController";
	}
	//Register User
	@PostMapping("/addEmployee")
	public  Object addEmployee(@RequestBody employeeUserRequest employe) {
		log.info("*******inside addEmployee AuthController");
		return authService.addEmployee(employe);
	}
	//Generate Token
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest request)throws Exception{
		log.info("inside login AuthController");
	Authentication authenticate=	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getId(), request.getPassword()));
	if(authenticate.isAuthenticated()) {
		return new ResponseEntity<String>(authService.Login(request),HttpStatus.OK);
	}
	else {
		throw new RuntimeException("Invalid User Details");
	}
	}
	@GetMapping("/authenticate")
	public void LoadUserDetails( HttpServletRequest request) {
		
		log.info("********inside loadUserDetails AuthController");
		String header=request.getHeader("Authorization");
		
		String Token=header.substring(7);
		log.info("*******token : "+Token);
		String UserName=jwtService.extractEmployeeId(Token);
		UserDetails userDetails=userDetailsService.loadUserByUsername(UserName);
		log.info("****userDetails: "+userDetails);
		UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(UserName, userDetails.getPassword(), userDetails.getAuthorities());
		log.info("Token Details : "+authtoken);
		System.out.println(authtoken);
		authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authtoken);
	
	}
}
