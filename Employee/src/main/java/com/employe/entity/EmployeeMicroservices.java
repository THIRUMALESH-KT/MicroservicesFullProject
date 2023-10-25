 package com.employe.entity;

import java.time.LocalDate;

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
	    private String name;
	    private Long mobile;
	    private String designation;
	    private String email;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private String skill;
	    private Long managerId;
	    private String status;
	    private Boolean isDeleted;
}
