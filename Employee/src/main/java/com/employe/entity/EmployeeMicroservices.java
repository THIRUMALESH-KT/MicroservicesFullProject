package com.employe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeMicroservices {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private Long employeeId;
	    private String accesCode;
	    private String name;
	    private String mobile;
	    private String designation;
	    private String password;
	    private String email;
	    private String startDate;
	    private String endDate;
	    private String skill;
	    private Long managerId;
}
