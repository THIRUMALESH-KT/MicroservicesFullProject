package com.leaveType.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandular {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String , Object>> Exception(Exception ex) {
		Map<String , Object> map=new  HashMap<>();
		map.put("message ", "Failed");
		map.put("resutl", ex.getLocalizedMessage());
		map.put("status", HttpStatus.BAD_REQUEST);
		map.put("status code ", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
	}
}
