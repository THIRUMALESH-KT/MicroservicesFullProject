package com.manager.userRequest;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MangerUserRequest {
	
	 private String name;
	    private Long managerId;
	    private String working;
	    private String skill;
	    private LocalDate startDate;
}
