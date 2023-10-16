package com.leaveType.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaveType.entity.LeaveType;

public interface LeaveTypeRepositore extends JpaRepository<LeaveType, Long> {
    @Query("SELECT lt.description FROM LeaveType lt WHERE lt.id = :id")
	String findByLeaveCode(Long id);

}
