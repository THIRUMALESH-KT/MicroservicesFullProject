package com.employe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employe.entity.OTPRecord;

@Repository
public interface OTPRecordRepository extends JpaRepository<OTPRecord, Long> {

	OTPRecord findByUserIdAndOtp(Long userId, String otp);



}
