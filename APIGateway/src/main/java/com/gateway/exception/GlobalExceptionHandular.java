package com.gateway.exception;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandular {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> HandleException(Exception ex){
		Map<String , Object> map=new HashMap<>();
		map.put(" Result  : ", " Fail ");
		map.put(" Error message : ", ex.getMessage());
		map.put(" From : ", ex.getClass());
		map.put("status : ",String.valueOf(HttpStatus.BAD_REQUEST.value()));
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
		
		
	}
}
