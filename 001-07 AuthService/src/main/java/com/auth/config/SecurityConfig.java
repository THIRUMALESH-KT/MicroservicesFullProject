package com.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter filter;
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
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}
