package com.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class AuthConfig {

	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	@Bean
	public SecurityFilterChain chain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf->
				csrf.disable())
				.authorizeHttpRequests(authorizweRequests->
				authorizweRequests
				.requestMatchers("/auth/hello","auth/login","/auth/authenticate/**")
				.permitAll()
				.requestMatchers("/auth/welcome","/leave/welcome")
				.hasRole("1005")
				.anyRequest()
				.authenticated()
				)
				.exceptionHandling(exception->
				exception
				.accessDeniedHandler((request, response, accessDeniedException) -> {
			        log.error("Access Denied: " + accessDeniedException.getMessage());
			        // You can add more details or custom responses here.
			    }))
				.build();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	@Bean
	public UserDetailsService detailsService() {
		return new CustomUserDetailsService();
	}
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(detailsService());
		return provider;
	}
	
}
