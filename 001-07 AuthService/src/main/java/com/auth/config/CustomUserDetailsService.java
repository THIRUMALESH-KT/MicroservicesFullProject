package com.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import com.auth.userRequest.employeeUserRequest;
import com.google.common.base.Optional;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private RestTemplate restTemplate;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ResponseEntity<UserPrinciples> credentials=restTemplate.exchange("http://localhost:8083/employee/getUser/"+Long.valueOf(username), HttpMethod.GET, null, UserPrinciples.class);
		return new CustomUserDetails(credentials.getBody());
	}

}
