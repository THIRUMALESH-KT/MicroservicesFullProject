package com.leave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leave.entity.EmployeeLeave;

public interface LeaveRepository extends JpaRepository<EmployeeLeave, Long>{

	List<Object> findByManagerId(Long managerId);

	List<Object> findByManagerIdAndLeaveStatus(Long managerId, String string);

	List<Object> findByEmployeeId(Long valueOf);

}
