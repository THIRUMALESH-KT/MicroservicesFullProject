package com.leave.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandular {

	@ExceptionHandler(CustomAccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(CustomAccessDeniedException ex) {
        // You can log the exception or perform additional actions here.
		Map<String, Object> map=new HashMap<>();
		map.put("result ", "failed");
		map.put("message : ", "Authorized persons only can access this page");
		return new ResponseEntity<Map<String, Object>>(map,HttpStatus.FORBIDDEN);
    }
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String , Object>> Exception(Exception ex) {
		Map<String , Object> map=new  HashMap<>();
		map.put("resutl", ex.getLocalizedMessage());
		map.put("status", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
	}
}
