package com.leaveType.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.leaveType.helper.ConstantValues;



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
    @ExceptionHandler({MethodArgumentNotValidException.class,})
   public Map<String,String> validateException(MethodArgumentNotValidException ex){
	 
    	Map<String,String> errorsMap=new HashMap<>();
    	errorsMap.put("result", "failed");
        ex.getBindingResult().getFieldErrors().forEach(error->{
            errorsMap.put(error.getField(),error.getDefaultMessage());
        });
        errorsMap.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return errorsMap;
    }
}
