//package com.gateway.configuration;
//
//import java.io.IOException;
//import java.net.URI;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.RequestEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.UriTemplate;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Configuration
//public class JwtFilter extends OncePerRequestFilter{
//
//	@Autowired
//	private RestTemplate restTemplate;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//	        throws ServletException, IOException {
//	    String header = request.getHeader("Authorization");
//
//	    if (header == null || !header.startsWith("Bearer ")) {
//	        filterChain.doFilter(request, response);
//	        return;
//	    }
//
//	    // Extract the token from the Authorization header
//	    String token = header.substring(7);
//
//	    // Create the URL for the remote microservice's /authenticate endpoint
//	    String authServiceUrl = "http://localhost:8087/auth/authenticate";
//
//	    // Create the request entity with the token as a header
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.add("Authorization", "Bearer " + token);
//	    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//	    // Send the HTTP request using RestTemplate
//	    ResponseEntity<String> responseEntity = restTemplate.exchange(
//	        authServiceUrl,
//	        HttpMethod.GET,
//	        requestEntity,
//	        String.class
//	    );
//
//	    // Handle the response as needed, for example, sending it back to the original response
//	    response.setStatus(responseEntity.getStatusCodeValue());
//	    response.getWriter().write(responseEntity.getBody());
//
//	    filterChain.doFilter(request, response);
//	}
//
//		
//}
