package com.auth.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrinciples {
	private Long employeeId;
	private String password;
	private String accessCode;
}
