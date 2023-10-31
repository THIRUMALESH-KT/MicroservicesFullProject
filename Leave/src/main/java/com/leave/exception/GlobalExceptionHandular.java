package com.leave.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leave.helper.ConstantValues;
import com.netflix.config.validation.ValidationException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;



@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandular {

	 @ExceptionHandler(HttpClientErrorException.class)
	    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
	        // Extract the error response from the exception
	     log.info("inside ********* handleHttpClientErrorException");  
	       Map<String , Object>  obj= ex.getResponseBodyAs(LinkedHashMap.class);
	     return ResponseEntity.badRequest().body(obj);

	    }
	
	@ExceptionHandler({MethodArgumentNotValidException.class,})
	   public Map<String,String> MethodArgumentNotValidException(MethodArgumentNotValidException ex){
		 log.info("*******inside MethodArgumentNotValidException" );
	    	Map<String,String> errorsMap=new LinkedHashMap<>();
	    	errorsMap.put("message", "Validation failed ");
	        ex.getBindingResult().getFieldErrors().forEach(error->{
	            errorsMap.put(error.getField(),error.getDefaultMessage());
	        });
	        errorsMap.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
	        return errorsMap;
	    }
	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<Map<String , Object>> UnAuthorizedException(UnAuthorizedException ex,WebRequest request) {
		 log.info("*******inside UnAuthorizedException" );

  	 	CustomException cu=new CustomException(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.UNAUTHORIZED.value());
    	Map<String, Object> map1=new HashMap<>();
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		map1.put(ConstantValues.StatusMessage, cu.getMessage());
		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.Description, cu.getDescription());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.UNAUTHORIZED);
    		
	}
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<Map<String , Object>> EmployeeNotFoundException(EmployeeNotFoundException ex,WebRequest request) {
		 log.info("*******inside EmployeeNotFoundException" );
		 log.info("**********"+ex.getLocalizedMessage());
		CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
    	Map<String, Object> map1=new LinkedHashMap<>();
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		map1.put(ConstantValues.StatusMessage, cu.getMessage());
		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.Description, cu.getDescription());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(LeaveIdNotFoundException.class)
	public ResponseEntity<Map<String , Object>> LeaveIdNotFoundException(LeaveIdNotFoundException ex,WebRequest request) {
  	 	
		 log.info("*******inside LeaveIdNotFoundException" );
CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getLocalizedMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
    	Map<String, Object> map1=new HashMap<>();
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		map1.put(ConstantValues.StatusMessage, cu.getMessage());
		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.Description, cu.getDescription());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
	}
//	@ExceptionHandler(RuntimeException.class)
//	public ResponseEntity<Map<String, Object>> RuntimeException(RuntimeException ex,WebRequest request) {
//		 log.info("*******inside RuntimeException" );
//		 
//		CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getLocalizedMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
//    	Map<String, Object> map1=new HashMap<>();
////		map1.put(ConstantValues.statusCode, cu.getStatusCode());
////		map1.put(ConstantValues.Status, cu.getStatus());
//		map1.put(ConstantValues.StatusMessage,ex.getLocalizedMessage());
////		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
////		map1.put(ConstantValues.Description, cu.getDescription());
//		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
//	//	return ResponseEntity.badRequest().body(ex.getMessage());
//    		
//	}
//	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	 public ResponseEntity<Map<String,Object>> HttpMessageNotReadableException(HttpMessageNotReadableException ex,WebRequest request){
		 log.info("*******inside HttpMessageNotReadableException" );
	
		CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getLocalizedMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
    	Map<String, Object> map1=new HashMap<>();
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		map1.put(ConstantValues.StatusMessage, cu.getMessage());
		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.Description, cu.getDescription());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
	  }
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ValidationException.class)
	 public ResponseEntity<Map<String,Object>> HttpMessageNotReadableException(ValidationException ex,WebRequest request){
		 log.info("*******inside HttpMessageNotReadableException" );

		CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getLocalizedMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
    	Map<String, Object> map1=new HashMap<>();
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		map1.put(ConstantValues.StatusMessage, cu.getMessage());
		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.Description, cu.getDescription());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
	  }
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	 public ResponseEntity<Map<String,Object>> HttpMessageNotReadableException(IllegalArgumentException ex,WebRequest request){
		 log.info("*******inside IllegalArgumentException" );

		CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getLocalizedMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
    	Map<String, Object> map1=new HashMap<>();
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		map1.put(ConstantValues.StatusMessage, cu.getMessage());
		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.Description, cu.getDescription());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
	  }
//	  @ExceptionHandler(ConstraintViolationException.class)
//	    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
//	        Map<String, Object> errorDetails = new HashMap<>();
//	        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
//	        errorDetails.put("message", "Validation failed");
//
//	        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
//	        List<String> validationErrors = new ArrayList<>();
//	        for (ConstraintViolation<?> violation : violations) {
//	            validationErrors.add(violation.getMessage());
//	        }
//	        errorDetails.put("errors", validationErrors);
//
//	        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//	    }

}
