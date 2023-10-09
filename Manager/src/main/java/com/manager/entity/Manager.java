package com.manager.entity;


import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class Manager {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String name;
	    private Long managerId;
	    private Long[] employeesIds;
	    private String working;
	    private String skill;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private Long createdBy;
	    private Long modifiedBy;
}
