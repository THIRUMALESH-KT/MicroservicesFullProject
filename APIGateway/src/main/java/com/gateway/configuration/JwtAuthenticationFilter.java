package com.gateway.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
@Configuration
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtService jwtService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
log.info("inside doFilterInternal JwtAuthenticationFilter");
		String header=request.getHeader("Authorization");
		if(header==null || !header.startsWith("Bearer ")) {
			log.info("header is null");
			filterChain.doFilter(request, response);
			return;
		}
		String Token=header.substring(7);

		String UserName=jwtService.extractUserName(Token);
		if(jwtService.isTokenValid(Token)) {
			UserDetails userDetails=userDetailsService.loadUserByUsername(UserName);
			UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(UserName, userDetails.getPassword(), userDetails.getAuthorities());
			System.out.println(authtoken);
			authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authtoken);
		}
		
		filterChain.doFilter(request, response);
	}

}
