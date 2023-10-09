package com.gateway.dto;

import lombok.Data;

@Data
public class EmployeDTO {

		private Long id;
	   private String name;
	    private String mobile;
	    private String designation;
	    private String password;
	    private Long managerId;
}
