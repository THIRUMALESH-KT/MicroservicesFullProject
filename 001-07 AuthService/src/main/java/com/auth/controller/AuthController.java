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

import com.auth.config.JwtAuthenticationFilter;
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
		log.info("******** inside login AuthController");
	Authentication authenticate=	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getId(), request.getPassword()));
	if(authenticate.isAuthenticated()) {
		return new ResponseEntity<String>(authService.Login(request),HttpStatus.OK);
	}
	else {
		throw new RuntimeException("Invalid User Details");
	}
	}
	@GetMapping("/authenticate")
	public ResponseEntity<String> loadUserDetails(HttpServletRequest request) {
//	    try {
//	        log.info("********inside loadUserDetails AuthController");
//	        String header = request.getHeader("Authorization");
//	        log.info("******header : " + header);
//	        if (header == null || !header.startsWith("Bearer ")) {
//	            throw new IllegalArgumentException("Invalid or missing authorization header");
//	        }
//	        String token = header.substring(7);
//	        log.info("*******token : " + token);
//	        String userName = jwtService.extractEmployeeId(token);
//	        log.info("*******userName : " + userName);
//	        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//	        log.info("****userDetails: " + userDetails);
//	        UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(userName, userDetails.getPassword(), userDetails.getAuthorities());
//	        log.info("Token Details : " + authtoken);
//	        authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//	        SecurityContextHolder.getContext().setAuthentication(authtoken);
//	        return ResponseEntity.ok("Authentication successful");
//	    } catch (Exception ex) {
//	        log.error("Exception in /authenticate endpoint: " + ex.getMessage());
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
//	    }
		return null;
	}
	
//	@GetMapping("/filter")
//	public void FilterAcess() {
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
//        jwtAuthenticationFilter.doFilter(null, null, null)
//	}
	

}
