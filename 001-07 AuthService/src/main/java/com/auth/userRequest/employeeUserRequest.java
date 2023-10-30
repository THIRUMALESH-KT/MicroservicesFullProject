package com.auth.userRequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
public class employeeUserRequest {
	@NotNull(message = "EmployeeId Must Not Be Null")

	private Long   employeeId;
	@NotEmpty(message = "accessCode Must Not Be Null")

    private String accesCode;
		@NotEmpty(message = "name Must Not Be Null")

    private String name;
    	@NotNull(message = "mobile number must not Be null")
	@Pattern(regexp = "^[6789][0-9]{9}$", message = "invalid mobile number")
	@NotEmpty(message = "mobile Must Not Null")
	
    private String mobile;
    	@NotEmpty(message = "designation Must Not Be Null")

    private String designation;
    @NotNull(message = "password  Must Not Be Null")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*~!]).{8,}$", message = "password must size greater than 8, atleast one cpatial and one small and one digit and one special character ")
	
    private String password;
    	@NotEmpty(message = "email Must Not Be Null")

    private String email;
    
    
    	@NotNull(message = "startDate Must Not Be Null")

    private String startDate;
    	@NotEmpty(message = "skill Must Not Be Null")

    private String skill;
    	@NotNull(message = "managerId Must Not Be Null")

    private Long   managerId;
    
    
    
}
