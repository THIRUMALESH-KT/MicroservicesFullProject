package com.auth.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.auth.exception.CustomAccessDeniedException;
import com.auth.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver exceptionResolver;
	
	public JwtAuthenticationFilter(HandlerExceptionResolver response) {
		this.exceptionResolver=response;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("**********inside doFilterInternal JwtAuthenitcationFilter AuthService" );
		try {
		String header=request.getHeader("Authorization");
		if(header==null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		String Token=header.substring(7);

		String UserName=String.valueOf(jwtService.extractEmployeeId(Token));
		
			UserDetails userDetails=userDetailsService.loadUserByUsername(UserName);
			
			UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
			if(!authtoken.isAuthenticated()) {
				log.info("*********User authentication failed" );
				//throw new Exception(" Authentication Failedd ");
			}
			System.out.println(authtoken);
			authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authtoken);
		
		log.info("tokn : "+authtoken);
		filterChain.doFilter(request, response);
		}catch(Exception e) {
			log.info("*******inside catch");
			log.info(e.getMessage());
		
			exceptionResolver.resolveException(request, response, null, e);
			log.info(exceptionResolver.toString());
		}
	}

}
