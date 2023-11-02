package com.auth.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.auth.exception.CustomAccessDeniedException;
import com.auth.exception.CustomException;
import com.auth.helper.ConstantValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.error("Authentication error occurred in AuthEntryPoint");

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            authException.getMessage(),
            LocalDateTime.now().toString()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
@Data
class ErrorResponse {
    private int statusCode;
    private String status;
    private String message;
    private String timestamp;

    public ErrorResponse(int statusCode, String status, String message, String timestamp) {
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

}
//@Component
//@Slf4j
//public class AuthEntryPoint implements AuthenticationEntryPoint {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	@Override
//	public void commence(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException authException) throws IOException, ServletException {
//		 log.info("*******inside commence AuthEntryPoint");
//	  	 	CustomException cu=new CustomException(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), authException.getMessage(), request.getPathInfo(), HttpStatus.UNAUTHORIZED.value());
//
//		    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			final Map<String, Object> map1 = new LinkedHashMap<>();
//			map1.put(ConstantValues.Timestamp, cu.getTimestamp());
//			map1.put(ConstantValues.statusCode, cu.getStatusCode());
//			map1.put(ConstantValues.Status, cu.getStatus());
//			map1.put(ConstantValues.StatusMessage, cu.getMessage());
//			map1.put(ConstantValues.Description, cu.getDescription());
//
//
//			final ObjectMapper mapper = new ObjectMapper();
//			mapper.writeValue(response.getOutputStream(), map1);
//	}
//
//}
