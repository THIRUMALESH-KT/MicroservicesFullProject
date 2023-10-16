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
		Manager employee=GetById(id);
		managerRepository.delete(employee);
		return "Employee Sucefully Deleted : "+id;
	}

	public Manager UpdateById(Long id, MangerUserRequest Manager) throws Exception {
		Manager Manager1=GetById(id);
		
		
		return managerRepository.save(Manager1);
	}

	public Manager GetById(Long id) throws Exception {
		log.info("inside getBYId ManagerService");
		Manager manager1=managerRepository.findByManagerId(id);
		if(manager1==null)throw new Exception("Manger id not found");
		return manager1;
	}

	public Object Insert(MangerUserRequest employe) throws Exception {
		Manager manager1=GetById(employe.getEmployeeId());
		if(manager1!=null) 
			throw new Exception("(Dublicate Employee) Employee already registred with this employee name : "+employe.getName());
		
		manager1=new Manager(null, employe.getName(), employe.getEmployeeId(), employe.getAccesCode(), null, employe.getDesignation(), employe.getPassword(),employe.getEmail(), employe.getSkill(), employe.getStartDate(), null, null, null);
		
		return managerRepository.save(manager1);
	}

	public Object GetAllEmployes() throws Exception {
		List<Manager> list=managerRepository.findAll();
		if(list==null) throw new Exception();
		return list;
	}
	public void AddEmployeeId(MangerUserRequest employe) throws Exception   {
		Manager manager=GetById(employe.getManagerId());
		manager.setEmployeesIds(new ArrayList<>(manager.getEmployeesIds()));
		manager.getEmployeesIds().add(employe.getEmployeeId());	
		
		managerRepository.save(manager);
	}
	
	
}
