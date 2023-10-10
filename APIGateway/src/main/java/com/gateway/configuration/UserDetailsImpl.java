package com.gateway.configuration;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import com.gateway.dto.EmployeDetails;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class UserDetailsImpl implements UserDetails {
	private EmployeDetails employeeDetails;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		log.info("inside getAuthorities UserDetailsImpl");
		List<GrantedAuthority> authorities = new ArrayList<>();
	    authorities.add(new SimpleGrantedAuthority(employeeDetails.getAccessCode()));
	    return authorities;
	}
	@Override
	public String getPassword() {
		return employeeDetails.getPassword();
	}

	@Override
	public String getUsername() {
		return employeeDetails.getEmployeeId();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	public UserDetailsImpl(EmployeDetails customer) {
		log.info("inside UserDtails constructory UserDetailsImpl");
		this.employeeDetails=customer;
	}

}
