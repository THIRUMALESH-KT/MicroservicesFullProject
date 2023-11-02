package com.auth.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.RepaintManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import com.auth.helper.ConstantValues;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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

    @ExceptionHandler({MethodArgumentNotValidException.class,})
   public ResponseEntity<Map<String , Object>> MethodArgumentNotValidException(MethodArgumentNotValidException ex){
	 log.info("********** inside MethodArgumentNotValidException");
    	Map<String,Object> errorsMap=new HashMap<>();
    	errorsMap.put("result", "failedd");
        ex.getBindingResult().getFieldErrors().forEach(error->{
            errorsMap.put(error.getField(),error.getDefaultMessage());
        });
        errorsMap.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<Map<String,Object>>(errorsMap,HttpStatus.BAD_REQUEST);
    }
	
    @ExceptionHandler(value = {ExpiredJwtException.class})
    @ResponseBody
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        log.info("********** inside ExpiredJwtException");


        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = {SignatureException.class})
    public ResponseEntity<Object> JwtException(SignatureException ex, WebRequest request) {
   	 log.info("********** inside SignatureException");

    	
		return ResponseEntity.internalServerError().body(ex.getMessage()); 
		}
    @ExceptionHandler(value = {MalformedJwtException.class})
    public ResponseEntity<Object> MalformedJwtException(MalformedJwtException ex, WebRequest request) {
   	 log.info("********** inside MalformedJwtException");

    	
		return ResponseEntity.internalServerError().body(ex.getMessage()); 
		}
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String , Object>> Exception(Exception ex){
   	 log.info("********** inside Exception");

    	Map<String , Object> map=new LinkedHashMap<>();
    	map.put("message ","Failed");
    	map.put("result ", ex.getLocalizedMessage());
    	map.put("status" , HttpStatus.BAD_REQUEST);
    	map.put("statusCode", HttpStatus.BAD_REQUEST.value());
    	return new  ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
    }

}
