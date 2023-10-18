package com.leaveType.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.leaveType.entity.LeaveType;
@Repository
public interface LeaveTypeRepositore extends JpaRepository<LeaveType, Long> {
    @Query("SELECT lt.description FROM LeaveType lt WHERE lt.leaveCode = :id")
	String findByLeaveCode(String id);

}
