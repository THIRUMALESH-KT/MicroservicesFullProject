package com.gateway.configuration;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import jakarta.ws.rs.core.SecurityContext;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate restTemplate;

    private static final String authServiceUrl = "http://localhost:8087/auth";

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        log.info("inside Gatewayfilter apply");
        return (exchange, chain) -> {
            log.info("****inside return");
            if (validator.isSecured.test(exchange)) {
                log.info("********inside 1st if");
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                	log.info("*********  No Header Found ");
                    throw new RuntimeException("Missing authorization header");
                }

                // Extract the authorization header
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader != null && authHeader.startsWith("Bearer")) {
                    authHeader = authHeader.substring(7);
                }

                log.info("*********before authentication");
                
                String authServiceUrl = "http://localhost:8087/auth/authenticate"; 
                
              
                String requestPath = exchange.getRequest().getURI().getPath();
                
               
                RequestEntity<Void> requestEntity = RequestEntity
                    .get(URI.create(authServiceUrl))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + authHeader)
                    .build();
                restTemplate.exchange(requestEntity, String.class);
   
                log.info("*********after authentication");
            }

            log.info("********out side filter");
            return chain.filter(exchange);
        };
    }


    public static class Config {
    }
}
