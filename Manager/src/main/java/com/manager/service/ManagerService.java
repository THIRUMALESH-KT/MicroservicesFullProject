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
	
	public Object DeleteById(Long id) throws Exception {
		Manager employee=managerRepository.findByManagerId(id);
		if(employee!=null) {
			
		managerRepository.delete(employee);
		}
		return employee;
	}

	public Manager UpdateById(Long id, MangerUserRequest request) throws Exception {
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
		
		return employee;
	}

	public Manager GetById(Long id) throws Exception {
		log.info("inside getBYId ManagerService");
		log.info("Manager id : "+id);
		Manager manager1=managerRepository.findByManagerId(id);
		if(manager1==null)throw new Exception("Manger id not found");
		return manager1;
	}

	public Object Insert(MangerUserRequest employe) throws Exception {
		Manager manager1=managerRepository.findByManagerId(employe.getEmployeeId());
		if(manager1!=null) 
			throw new Exception("(Dublicate Employee) Employee already registred with this employee name : "+employe.getName());
		
		manager1=new Manager(null, employe.getName(), employe.getEmployeeId(),null, employe.getDesignation(), employe.getEmail(),Long.valueOf(employe.getMobile()), employe.getSkill(), employe.getStartDate(), null);
		
		return managerRepository.save(manager1);
	}

	public Object GetAllEmployes() throws Exception {
		List<Manager> list=managerRepository.findAll();
		if(list==null) throw new Exception();
		return list;
	}
	public void AddEmployeeId(Long managerId,Long employeeId) throws Exception   {
		log.info("*****inside addEmployeeId managerService");
		log.info("*******employee Id: "+employeeId);
		log.info("******managerId : "+managerId);
		Manager manager=GetById(managerId);
		manager.setEmployeesIds(new ArrayList<>(manager.getEmployeesIds()));
		manager.getEmployeesIds().add(employeeId);	
		
		managerRepository.save(manager);
	}

	public void RemoveEmployeeId(Long managerId, Long employeeId) throws Exception {
		log.info("****** inside removeEmployeeId ManagerService");
		Manager manager=GetById(managerId);
		manager.setEmployeesIds(new ArrayList<>(manager.getEmployeesIds()));
		manager.getEmployeesIds().remove(employeeId);
		managerRepository.save(manager);

	}
	
	
}
