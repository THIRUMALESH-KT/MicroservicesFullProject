package com.employe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employe.entity.EmployeeMicroservices;
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeMicroservices, Long> {

	EmployeeMicroservices findByName(String name);

}
