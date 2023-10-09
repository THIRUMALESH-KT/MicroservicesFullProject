package com.employe.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTempleteConfiguration {

	  @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
}
