package com.manager.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.manager.entity.Manager;
import com.manager.repository.ManagerRepository;
import com.manager.userRequest.MangerUserRequest;

@Service
public class ManagerService {

	@Autowired
	private ManagerRepository employeeRepository;
	
	public Object DeleteById(Long id) throws Exception {
		Manager employee=GetById(id);
		employeeRepository.delete(employee);
		return "Employee Sucefully Deleted : "+id;
	}

	public Manager UpdateById(Long id, MangerUserRequest Manager) throws Exception {
		Manager Manager1=GetById(id);
		
		
		return employeeRepository.save(Manager1);
	}

	public Manager GetById(Long id) throws Exception {
		Manager Manager1=employeeRepository.findById(id).orElseThrow(()-> new Exception());
		return Manager1;
	}

	public Object Insert(MangerUserRequest employe) throws Exception {
		Manager Manager1=employeeRepository.findByName(employe.getName());
		if(Manager1!=null) 
			throw new Exception("(Dublicate Employee) Employee already registred with this employee name : "+employe.getName());
		
		
		return employeeRepository.save(Manager1);
	}

	public Object GetAllEmployes() throws Exception {
		List<Manager> list=employeeRepository.findAll();
		if(list==null) throw new Exception();
		return list;
	}

	
}
