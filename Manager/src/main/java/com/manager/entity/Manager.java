package com.manager.entity;


import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Manager {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String name;
	    private Long managerId;
	    private String accesCode;
	    @ElementCollection(fetch = FetchType.EAGER)
	    private List<Long> employeesIds;
	    private String designation;
	    private String password;
	    private String email;
	    private String skill;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private Long createdBy;
	    private Long modifiedBy;
}
