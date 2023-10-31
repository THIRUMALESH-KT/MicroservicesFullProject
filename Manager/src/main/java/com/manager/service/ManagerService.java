package com.manager.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.manager.entity.Manager;
import com.manager.exception.EmployeeNotFoundException;
import com.manager.exception.UserNotFountException;
import com.manager.repository.ManagerRepository;
import com.manager.userRequest.MangerUserRequest;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManagerService {

	@Autowired
	private ManagerRepository managerRepository;
	
	public ResponseEntity<Object> DeleteById(Long id) throws Exception {
		Manager employee=managerRepository.findByManagerId(id);
		if(employee!=null) {
			
		managerRepository.delete(employee);
		}
		return ResponseEntity.ok(employee);
	}

	public ResponseEntity<Object> UpdateById(Long id, MangerUserRequest request) throws Exception {
		log.info("***********inside updateManagerId ManagaerService");
		Manager employee=managerRepository.findByManagerId(id);
		if(employee!=null)
		{
			log.info(" employee  not null");
			employee.setName(request.getName());
			employee.setMobile(Long.valueOf(request.getMobile()));
			employee.setDesignation(request.getDesignation());
			employee.setEmail(request.getEmail());
			employee.setSkill(request.getSkill());
			employee.setManagerId(request.getManagerId());
			managerRepository.save(employee);
		}
		
		return ResponseEntity.ok(employee);
	}

	public ResponseEntity<Object> GetById(Long id) throws Exception {
		log.info("inside getBYId ManagerService");
		log.info("Manager id : "+id);
		Manager manager1=managerRepository.findByManagerId(id);
//		if(manager1==null)return ResponseEntity.badRequest().body("Manager Id Not Found");
		if(manager1==null)throw new EmployeeNotFoundException("Manager Id Not Found");
		return ResponseEntity.ok(manager1);
	}

	public ResponseEntity<Object> Insert(MangerUserRequest employe) throws Exception {
		Manager manager1=managerRepository.findByManagerId(employe.getEmployeeId());
		if(manager1!=null) 
		return ResponseEntity.badRequest().body("Dublicate Employee  Id");
		manager1=new Manager(null, employe.getName(), employe.getEmployeeId(),null, employe.getDesignation(), employe.getEmail(),Long.valueOf(employe.getMobile()), employe.getSkill(), employe.getStartDate(), null);
		
		return ResponseEntity.ok(managerRepository.save(manager1));
	}

	public Object GetAllEmployes() throws Exception {
		List<Manager> list=managerRepository.findAll();
		if(list==null) throw new Exception();
		return list;
	}
	public ResponseEntity<Object> AddEmployeeId(Long managerId,Long employeeId) throws Exception   {
		log.info("*****inside addEmployeeId managerService");
		log.info("*******employee Id: "+employeeId);
		log.info("******managerId : "+managerId);
		Manager manager=managerRepository.findByManagerId(managerId);
		if(manager==null)return ResponseEntity.badRequest().body("Manager id Not Found");
		manager.setEmployeesIds(new ArrayList<>(manager.getEmployeesIds()));
		manager.getEmployeesIds().add(employeeId);	
		
		return ResponseEntity.ok(managerRepository.save(manager));
	}

	public ResponseEntity<Object> RemoveEmployeeId(Long managerId, Long employeeId) throws Exception {
		log.info("****** inside removeEmployeeId ManagerService");
		Manager manager= managerRepository.findByManagerId(managerId);
		if(manager==null) return ResponseEntity.badRequest().body("Manager Id Not Found");
		manager.setEmployeesIds(new ArrayList<>(manager.getEmployeesIds()));
		manager.getEmployeesIds().remove(employeeId);
		return ResponseEntity.ok(managerRepository.save(manager));

	}
	
	
}
