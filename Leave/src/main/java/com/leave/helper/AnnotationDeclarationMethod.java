package com.leave.helper;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.leave.exception.UnAuthorizedException;
import com.leave.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;



@Component
@Aspect
@Slf4j
public class AnnotationDeclarationMethod {
	
	@Autowired
	private JwtService jwtService;
	
	private static String role;
	
	@Autowired
	private HttpServletRequest request;
	
	public String gettoken(HttpServletRequest request) {
		System.out.println("-----------"+this.request);
		System.out.println(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
		
		String role=  jwtService.GetRole(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
		System.out.println(role);
		this.role=role;
		return role;
	}
	
	@Around("@annotation(com.leave.helper.CustomAnnotation)")
	public Object validateAspect(ProceedingJoinPoint pjp) throws Throwable {
	    log.info("********inside validateAspect AnnotationDeclarationMethod");

	    // Get the method signature and the method
	    MethodSignature signature = (MethodSignature) pjp.getSignature();
	    Method method = signature.getMethod();

	    // Get the CustomAnnotation from the method
	    CustomAnnotation customAnnotation = method.getAnnotation(CustomAnnotation.class);

	    // Check if the annotation is present
	    if (customAnnotation != null) {
	        String[] customList = customAnnotation.allowedRoles();
	        log.info(customList.toString());
	        log.info("New List: " + Arrays.toString(customList));

	        // Check if the list in the annotation contains the expected value
	        String expectedValue = this.gettoken(request);
	        if (Arrays.asList(customList).contains(expectedValue)) {
	            return pjp.proceed();
	        } else {
	            throw new UnAuthorizedException("You don't have authority");
	        }
	    } else {
	        // Handle the case when the annotation is not present
	        // You can choose to proceed or handle it differently based on your requirements.
	        return pjp.proceed();
	    }
	}



}
