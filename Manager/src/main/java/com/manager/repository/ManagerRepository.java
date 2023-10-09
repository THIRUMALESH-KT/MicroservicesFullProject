package com.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manager.entity.Manager;
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

	Manager findByName(String name);

}
