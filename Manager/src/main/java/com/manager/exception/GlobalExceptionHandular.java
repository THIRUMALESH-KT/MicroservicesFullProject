package com.manager.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.manager.helper.ConstantValues;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandular {

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<Map<String , Object>> Exception(UnAuthorizedException ex,WebRequest request) {
  	 	CustomException cu=new CustomException(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.UNAUTHORIZED.value());
    	Map<String, Object> map1=new HashMap<>();
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		map1.put(ConstantValues.StatusMessage, cu.getMessage());
		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.Description, cu.getDescription());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.UNAUTHORIZED);
    		
	}

	@ExceptionHandler(UserNotFountException.class)
	public ResponseEntity<Map<String , Object>> UserNotFountException(UserNotFountException ex,WebRequest request) {
    	Map<String, Object> map1=new HashMap<>();
		map1.put(ConstantValues.statusCode,HttpStatus.BAD_REQUEST.value());
		map1.put(ConstantValues.Status, HttpStatus.BAD_REQUEST);
		map1.put(ConstantValues.StatusMessage, ex.getMessage());
		map1.put(ConstantValues.Timestamp, LocalDateTime.now());
//		map1.put(ConstantValues.Description, );
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
    		
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> Excetprioncal(Exception ex){
		log.info(" *******inside managerexception class exceptioncal");
		Map<String , Object> map1=new HashMap<>();
		map1.put("message ", "Failed");
		map1.put("result ", ex.getMessage());
		map1.put("status", HttpStatus.BAD_REQUEST);
		map1.put("status code ",HttpStatus.BAD_REQUEST.value()	);
		return new ResponseEntity<Map<String,Object>>(map1,HttpStatus.BAD_REQUEST);
	}
}
