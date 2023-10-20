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

	private UserPrinciples User;
	public CustomUserDetails(UserPrinciples employeeUserRequest) {
		this.User=employeeUserRequest;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority>list=new ArrayList<>();
		list.add(new SimpleGrantedAuthority(("ROLE_"+User.getAccessCode())));
		return list;
	
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return User.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return String.valueOf(User.getEmployeeId());
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
