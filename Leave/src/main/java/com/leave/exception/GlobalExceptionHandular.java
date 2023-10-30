package com.leave.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.context.request.WebRequest;

import com.leave.helper.ConstantValues;
import com.netflix.config.validation.ValidationException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;



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
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String , Object>> Exception(RuntimeException ex,WebRequest request) {
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
	@ExceptionHandler({MethodArgumentNotValidException.class,})
	 public Map<String,Object> validateException(MethodArgumentNotValidException ex){
			
	  	Map<String,Object> map=new HashMap<>();
		map.put("result", "failed");

	      ex.getBindingResult().getFieldErrors().forEach(error->{
	    	  map.put(error.getField(),error.getDefaultMessage());
	      });
			map.put("status", String.valueOf(HttpStatus.BAD_REQUEST));

	      return map;
	  }
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	 public ResponseEntity<Map<String,Object>> HttpMessageNotReadableException(HttpMessageNotReadableException ex,WebRequest request){
			
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
