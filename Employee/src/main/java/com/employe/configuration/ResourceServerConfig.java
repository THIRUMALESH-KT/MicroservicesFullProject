//package com.employe.configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//	private String clientId = "pixeltrice";
//
//	@Override
//	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//
//		resources.resourceId(clientId);
//	}
//
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//
//		http.authorizeRequests().antMatchers("/user/save", "/user/login").permitAll() .antMatchers("/user/welcome").hasRole("1001")
//				.anyRequest().authenticated();
//	}
//
//}