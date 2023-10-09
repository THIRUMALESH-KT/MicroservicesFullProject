package com.leaveType.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leaveType.entity.LeaveType;

public interface LeaveTypeRepositore extends JpaRepository<LeaveType, Long> {

}
