package com.auth.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auth.config.AuthEntryPoint;
import com.auth.helper.ConstantValues;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandular  {
	

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public  ResponseEntity<Map<Object, Object>>  handleInvalidArgument(MethodArgumentNotValidException ex,WebRequest request) {
        CustomException cu=new CustomException(HttpStatus.BAD_REQUEST,LocalDateTime.now(), ex.getMessage(),request.getDescription(false),404);
		
		Map<Object, Object> map1=new HashMap<Object, Object>();
		
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		
		map1.put(ConstantValues.StatusMessage, cu.getMessage());
		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.StatusMessage, ex.getTarget());
		map1.put(ConstantValues.Description, cu.getDescription());
		return ResponseEntity.ok(map1);
	}
	
	  @ExceptionHandler(value = {ExpiredJwtException.class})
	    public ResponseEntity<Map<String , Object>> ExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
	    	CustomException cu=new CustomException(HttpStatus.FORBIDDEN, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.FORBIDDEN.value());

	    	Map<String, Object> map1=new HashMap<>();
			map1.put(ConstantValues.statusCode, cu.getStatusCode());
			map1.put(ConstantValues.Status, cu.getStatus());
			map1.put(ConstantValues.StatusMessage, cu.getMessage());

			map1.put(ConstantValues.Timestamp, cu.getTimestamp());
			map1.put(ConstantValues.Description, cu.getDescription());
			return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.FORBIDDEN);    }

    @ExceptionHandler(value = {JwtException.class})
    public ResponseEntity<Map<String , Object>> handleJwtException(JwtException ex, WebRequest request) {
    	CustomException cu=new CustomException(HttpStatus.FORBIDDEN, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.FORBIDDEN.value());

    	Map<String, Object> map1=new HashMap<>();
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		map1.put(ConstantValues.StatusMessage, cu.getMessage());

		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.Description, cu.getDescription());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.FORBIDDEN);    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>  handlerSecurityException(Exception ex,WebRequest request){
    	log.info("*****inside handlerSecurityException");
    	if(ex instanceof BadCredentialsException) {
    	CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
    	
    	Map<String, Object> map1=new HashMap<>();
		map1.put(ConstantValues.statusCode, cu.getStatusCode());
		map1.put(ConstantValues.Status, cu.getStatus());
		map1.put(ConstantValues.StatusMessage, cu.getMessage());
		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
		map1.put(ConstantValues.Description, cu.getDescription());
		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
    	}
    	if(ex instanceof InternalAuthenticationServiceException) {
    	 	CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), "Invalid Id", request.getDescription(false), HttpStatus.BAD_REQUEST.value());
        	
        	Map<String, Object> map1=new HashMap<>();
    		map1.put(ConstantValues.statusCode, cu.getStatusCode());
    		map1.put(ConstantValues.Status, cu.getStatus());
    		map1.put(ConstantValues.StatusMessage, cu.getMessage());
    		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
    		map1.put(ConstantValues.Description, cu.getDescription());
    		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.BAD_REQUEST);
        		
    	}
    	if(ex instanceof SignatureException) {
 	CustomException cu=new CustomException(HttpStatus.FORBIDDEN, LocalDateTime.now(), ex.getMessage(), request.getDescription(false), HttpStatus.FORBIDDEN.value());
        	
        	Map<String, Object> map1=new HashMap<>();
    		map1.put(ConstantValues.statusCode, cu.getStatusCode());
    		map1.put(ConstantValues.Status, cu.getStatus());
    		map1.put(ConstantValues.StatusMessage, cu.getMessage());
    		map1.put(ConstantValues.Timestamp, cu.getTimestamp());
    		map1.put(ConstantValues.Description, cu.getDescription());
    		return new ResponseEntity<Map<String, Object>>(map1,HttpStatus.FORBIDDEN);
        		
    	
    	}
    	return null;
    }
}
