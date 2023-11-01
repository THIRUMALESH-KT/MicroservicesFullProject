package com.leave.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.entity.Tracking;
@Repository
public interface EmployeeLeaveDataTracking extends JpaRepository<Tracking	,Long> {

}
