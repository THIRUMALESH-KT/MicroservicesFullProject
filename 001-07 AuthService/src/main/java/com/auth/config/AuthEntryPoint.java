package com.auth.config;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.auth.exception.CustomAccessDeniedException;

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
	        log.info("****** exception instance : "+authException.getClass());
	        response.getWriter().write(authException.getLocalizedMessage());
	}

}
