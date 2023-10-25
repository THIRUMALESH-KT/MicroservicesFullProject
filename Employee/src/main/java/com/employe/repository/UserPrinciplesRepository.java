package com.employe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employe.entity.OTPRecord;
import com.employe.entity.UserPrinciples;
@Repository
public interface UserPrinciplesRepository extends JpaRepository<UserPrinciples, Long> {

	Optional<UserPrinciples> findByEmployeeId(Long userId);

}
