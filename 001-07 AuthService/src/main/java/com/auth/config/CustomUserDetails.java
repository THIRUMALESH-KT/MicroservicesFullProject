package com.auth.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.auth.userRequest.employeeUserRequest;

public class CustomUserDetails implements UserDetails {

	private employeeUserRequest employeeUserRequest;
	public CustomUserDetails(employeeUserRequest employeeUserRequest) {
		this.employeeUserRequest=employeeUserRequest;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority>list=new ArrayList<>();
		list.add(new SimpleGrantedAuthority((employeeUserRequest.getAccesCode())));
		return list;
	
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return employeeUserRequest.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return String.valueOf(employeeUserRequest.getEmployeeId());
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

}
