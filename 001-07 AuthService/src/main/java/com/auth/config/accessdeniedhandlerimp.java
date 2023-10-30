package com.auth.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.auth.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class accessdeniedhandlerimp implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		 CustomException cu=new CustomException(HttpStatus.FORBIDDEN,LocalDateTime.now(),"You don't have authority to access this page",request.getServletPath(),403);
			
			Map<Object, Object> map1=new HashMap<Object, Object>();
			map1.put("statusCode", cu.getStatusCode());
			map1.put("Status", cu.getStatus());
			map1.put("Timestamp", cu.getTimestamp());
			map1.put("StatusMessage", cu.getMessage());
			map1.put("Description", cu.getDescription());
			final ObjectMapper mapper = new ObjectMapper();
			 JavaTimeModule javaTimeModule = new JavaTimeModule();
			 mapper.registerModule(javaTimeModule);
			 mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);   
			mapper.writeValue(response.getOutputStream(), map1);

}}
