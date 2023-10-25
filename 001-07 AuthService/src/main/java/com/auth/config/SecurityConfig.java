package com.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth.exception.CustomAccessDeniedException;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter filter;
	@Autowired
	private AuthEntryPoint authEntryPoint;
	@Autowired
	private AuthenticationProvider authProvider;
	@Bean
	public SecurityFilterChain chain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf->
				csrf.disable())
				
				.authorizeHttpRequests(authorizweRequests->
				authorizweRequests
				.requestMatchers("/auth/welcome","/auth/login","/auth/hello","/employee/welcome","/auth/authenticate/**","/leave/welcome")
				.permitAll()
				.requestMatchers("/auth/addEmployee","employee/deleteById")
				.hasRole("1007")
				.requestMatchers("/employee/getAllEmployes","/manager/getAllEmploye","/leave/applyLeave/**","/leave/allEmployeesLeaveData","/leave/approveLeave/**","/leave/rejectLeave/**","/leaveType/addLeaveType")
				.hasRole("1005")
				.requestMatchers("/employee/getById","/employee/update/Personal/Details","/employee/password/reset/request","/employee/password/reset/confirm","/leave/applyLeave","/leave/delete/**","/leaveType/getAllLeaveTypes")
				.hasRole("1001")
				.anyRequest()
				.authenticated()
				)
				.exceptionHandling(exception->
				exception
				.accessDeniedHandler((request, response, accessDeniedException) -> {
			        log.error("Access Denied: " + accessDeniedException.getMessage());
		            throw new CustomAccessDeniedException("Access Denied: " + accessDeniedException.getMessage());

			    })
				.authenticationEntryPoint(authEntryPoint))
				.authenticationProvider(authProvider)

				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		String hierarchy = "ROLE_1008 > ROLE_1007 > ROLE_1005 > ROLE_1004 > ROLE_1003 > ROLE_1002 > ROLE_1001";
		roleHierarchy.setHierarchy(hierarchy);
		return roleHierarchy;
	}
}
