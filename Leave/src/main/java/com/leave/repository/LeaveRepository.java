package com.leave.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leave.entity.EmployeeLeave;

public interface LeaveRepository extends JpaRepository<EmployeeLeave, Long>{

}
