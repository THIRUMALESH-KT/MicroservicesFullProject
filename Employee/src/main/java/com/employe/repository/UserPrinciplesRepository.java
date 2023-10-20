package com.employe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employe.entity.UserPrinciples;
@Repository
public interface UserPrinciplesRepository extends JpaRepository<UserPrinciples, Long> {

}
