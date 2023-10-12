package com.auth.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandular {

	@ExceptionHandler(Exception.class)
	public Object Exception(Exception ex) {
		return ex.getMessage();
	}
}
