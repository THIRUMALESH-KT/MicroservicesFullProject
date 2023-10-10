package com.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

	@Autowired
	private JwtAuthenticationFilter filter;
	@Autowired
	private AuthenticationProvider provider;
	@Autowired
	private AuthEntryPoint entryPoint;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		System.out.println("insdie sercurity filter chain");
		log.info("inside securityFilterChain SecurityConfiguration");
		return http
				.csrf(csrf->
				csrf
				.disable())
				.authorizeHttpRequests(authorizeHttpRequests->
				authorizeHttpRequests
				.requestMatchers("/welcome")
				.permitAll()
			
//				.anyRequest()
//				.authenticated()
				)
				.sessionManagement(sessionManagement->
				sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exceptionHandling->
				exceptionHandling
				.accessDeniedPage("/accessDenied")
				.authenticationEntryPoint(entryPoint)
				)
				.formLogin(formLogin->
				formLogin.permitAll())
				//.authenticationProvider(provider)
				//.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}
