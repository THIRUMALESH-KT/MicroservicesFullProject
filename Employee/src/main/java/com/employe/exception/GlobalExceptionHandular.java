package com.employe.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.employe.helper.ConstantValues;

import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandular  {
	 @ExceptionHandler(HttpClientErrorException.class)
	    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
	        // Extract the error response from the exception
	     log.info("inside ********* handleHttpClientErrorException");  
	       Map<String , Object>  obj= ex.getResponseBodyAs(LinkedHashMap.class);
	     return ResponseEntity.badRequest().body(obj);

	    }
	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<Map<String , Object>> Exception(UnAuthorizedException ex,WebRequest request) {
  	 	CustomException cu=new CustomException(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.UNAUTHORIZED.value());
    	Map<String, Object> map1=new LinkedHashMap<>();
    	map1.put(ConstantValues.StatusMessage, cu.getMessage());
    	map1.put(ConstantValues.Description, cu.getDescription());
    	map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.UNAUTHORIZED);
    		
	}
   @ExceptionHandler({MethodArgumentNotValidException.class,})
   public Map<String,String> validateException(MethodArgumentNotValidException ex){
	 
    	Map<String,String> errorsMap=new LinkedHashMap<>();
    	errorsMap.put("message", "Validation failed ");
        ex.getBindingResult().getFieldErrors().forEach(error->{
            errorsMap.put(error.getField(),error.getDefaultMessage());
        });
        errorsMap.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return errorsMap;
    }
    @ExceptionHandler(EmployeeNotFoundException.class)
   	public ResponseEntity<Map<String , Object>> EmployeeNotFoundException(EmployeeNotFoundException ex,WebRequest request) {
  	 	CustomException cu=new CustomException(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.UNAUTHORIZED.value());

    	Map<String, Object> map1=new LinkedHashMap<>();
       	map1.put(ConstantValues.StatusMessage, cu.getMessage());
    	map1.put(ConstantValues.Description, cu.getDescription());
    	map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
   		//map1.put(ConstantValues.Description, request.getDescription(false));
   		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
       		
   	}
    @ExceptionHandler(DublicateEmployeeException.class)
   	public ResponseEntity<Map<String , Object>> DublicateEmployeeException(DublicateEmployeeException ex,WebRequest request) {
  	 	CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
    	Map<String, Object> map1=new LinkedHashMap<>();
      	map1.put(ConstantValues.StatusMessage, cu.getMessage());
    	map1.put(ConstantValues.Description, cu.getDescription());
    	map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
   		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
        	}
    @ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String , Object>> Exception(Exception ex,WebRequest request) {
  	 	CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
    	Map<String, Object> map1=new LinkedHashMap<>();
    	map1.put(ConstantValues.StatusMessage, cu.getMessage());
    	map1.put(ConstantValues.Description, cu.getDescription());
    	map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
    		
	}
    
}
