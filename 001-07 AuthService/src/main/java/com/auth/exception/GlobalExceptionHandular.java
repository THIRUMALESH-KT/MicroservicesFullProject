package com.auth.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auth.config.AuthEntryPoint;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@NoArgsConstructor
@Slf4j
public class GlobalExceptionHandular extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(CustomAccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(CustomAccessDeniedException ex) {
		log.info(" ********inside handleAccessDeniedException");
        // You can log the exception or perform additional actions here.
		Map<String, Object> map=new HashMap<>();
		map.put("result ", "failed");
		map.put("message : ", "Authorized persons only can access this page");
		return new ResponseEntity<Map<String, Object>>(map,HttpStatus.FORBIDDEN);
    }
	@ExceptionHandler(Exception.class)
	public Object Exception(Exception ex) {
		log.info("****** inside Exception");
		return ex.getMessage();
	}

	
	
}
