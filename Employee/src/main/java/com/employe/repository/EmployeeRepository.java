package com.employe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employe.entity.EmployeeMicroservices;
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeMicroservices, Long> {

	EmployeeMicroservices findByName(String name);

	EmployeeMicroservices findByEmployeeId(Long id);

	EmployeeMicroservices getByEmployeeId(Long id);

	EmployeeMicroservices findByDesignation(String string);

	List<Object> findByManagerId(Long managerId);

}
