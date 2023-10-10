package com.gateway.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.gateway.dto.EmployeDetails;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfiguration {

	
	 @Bean
	    @LoadBalanced
	    public RestTemplate restTemplate() {
		 log.info("inside restTemplate() AppConfiguration");
	        return new RestTemplate();
	    }
	 
	  @Bean
	    public UserDetailsService detailsService() {
		  log.info("inside detailsService AppConfiguration");
	        return new UserDetailsService() {
	            @Override
	            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	                EmployeDetails customer = restTemplate().getForObject("http://EMPLOYEE-SERVICE/employee/loadUserDetails/username", EmployeDetails.class);
	                if (customer == null) {
	                    throw new UsernameNotFoundException("User not found with username: " + username);
	                }
	                return new UserDetailsImpl(customer);
	            }
	        };
	    }
	  @Bean
		public AuthenticationProvider authenticationProvider() {
		  log.info("inside authenticationProvider AppConfiguration");
			DaoAuthenticationProvider dao=new DaoAuthenticationProvider();
			dao.setPasswordEncoder(passwordEncoder());
			dao.setUserDetailsService(detailsService());
			return dao;
		}
		@Bean
		public PasswordEncoder passwordEncoder() {
			log.info("Inside passwordEncoder AppConfiguration" );
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
			log.info("inside authenitcionManager AppConfiguration");
			return config.getAuthenticationManager();
		}
		
		
}
