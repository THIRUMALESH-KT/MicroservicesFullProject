package com.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.configuration.JwtService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GatewayController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsService userDetailsService;
	@GetMapping("/login")
	public String login(@RequestParam("userName")String userName,@RequestParam("password")String password) throws Exception {
		log.info("inside login GateWayController");
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		}catch(Exception ex) {
			throw new Exception(ex.getLocalizedMessage());
		}
		return jwtService.generateToken(userDetailsService.loadUserByUsername(userName));
	}
	@GetMapping("/welcome")
	public String welcomemessage() {
		return "this is welcom message";
	}
}
