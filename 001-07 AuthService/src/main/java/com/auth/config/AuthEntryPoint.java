package com.auth.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.auth.exception.CustomAccessDeniedException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthEntryPoint implements AuthenticationEntryPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		 log.info("*******inside commence AuthEntryPoint");
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.setContentType("application/json");
		    log.info("****** exception instance: " + authException.getClass());
		    Map<String, Object> map = new HashMap<>();
		    map.put("result", "failed");
		    map.put("message", authException.getLocalizedMessage()); // Changed from "result" to "message"
		    map.put("status", HttpStatus.BAD_REQUEST);
		    map.put("status code", HttpStatus.BAD_REQUEST.value());

		    ObjectMapper objectMapper = new ObjectMapper();
		    String jsonResponse = objectMapper.writeValueAsString(map);

		    response.getWriter().write(jsonResponse);
	}

}
