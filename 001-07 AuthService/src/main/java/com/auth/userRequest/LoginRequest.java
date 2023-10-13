package com.auth.userRequest;

import lombok.Data;

@Data
public class LoginRequest {
	
	private Long id;
	private String password;
}
