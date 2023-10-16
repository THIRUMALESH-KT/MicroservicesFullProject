package com.gateway.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JwtFilter extends OncePerRequestFilter {


	@Autowired
	private RestTemplate restTemplate;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	log.info("*******inside doFilterInternal JwtFilter ");
		String header=request.getHeader("Authorization");
		if(header==null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		else {
		restTemplate.exchange("http://AUTH-SERVICE/auth/authenticate", HttpMethod.GET,null, void.class);
		
		filterChain.doFilter(request, response);
		}
		filterChain.doFilter(request, response);

	}

	}


