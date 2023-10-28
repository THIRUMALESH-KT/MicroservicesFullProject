package com.auth.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.auth.config.CustomUserDetails;
import com.auth.config.JwtAuthenticationFilter;
import com.auth.config.UserPrinciples;
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
	public  ResponseEntity<Map<String , Object>> addEmployee(@RequestBody employeeUserRequest employe) {
		log.info("*******inside addEmployee AuthController");
		Map< String , Object>map=new HashMap<>();
		map.put("Message " , "Employee Created sucefully");
		map.put("result " , authService.addEmployee(employe));
		map.put("status ", HttpStatus.CREATED);
		map.put("status Code ", HttpStatus.CREATED.value());
		return ResponseEntity.ok(map);
	}
	//Generate Token
	@GetMapping("/login")
	public ResponseEntity<Map<String , Object>> login(@RequestBody LoginRequest request)throws Exception{
		log.info("******** inside login AuthController");
		
		Map<String, Object> map=new LinkedHashMap<>();
		map.put("Message  ", "Employee Loged in sucefully");
	
	Authentication authenticate=	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getId(), request.getPassword()));
	if(authenticate.isAuthenticated()) {
		map.put("Token  ", authService.Login(request));
		map.put("AccessLevel  ", userDetailsService.loadUserByUsername(String.valueOf(request.getId())).getAuthorities().toArray()[0].toString().substring(5));
		return ResponseEntity.ok(map);
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
	

	

}
