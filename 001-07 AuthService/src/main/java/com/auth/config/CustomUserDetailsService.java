 package com.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import com.auth.exception.EmployeeNotFoundException;
import com.auth.userRequest.employeeUserRequest;
import com.google.common.base.Optional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private RestTemplate restTemplate;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("************inside LoadByUserNamee");
		ResponseEntity<UserPrinciples> credentials=restTemplate.exchange("http://localhost:8083/employee/getUser/"+Long.valueOf(username), HttpMethod.GET, null, UserPrinciples.class);
//		System.out.println(credentials.getStatusCode()+"  : "+credentials.getBody());
//
//		if(credentials.getStatusCode()==HttpStatus.BAD_REQUEST) {
//			log.info("**********getting bad request from employee getUser");
//			System.out.println(credentials.getStatusCode()+"  : "+credentials.getBody());
//
//			throw new UsernameNotFoundException(credentials.getBody().toString());
//		}else {
//			log.info("no bad request");
//			System.out.println(credentials.getStatusCode()+"  : "+credentials.getBody());
//		}
//		
		log.info("******** credentials : "+credentials.getBody());
		return new CustomUserDetails(credentials.getBody());
	}

}
