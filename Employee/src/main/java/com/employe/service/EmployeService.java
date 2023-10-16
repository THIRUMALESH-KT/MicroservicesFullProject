package com.employe.service;

import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.employe.dto.UserDetails;
import com.employe.entity.EmployeeMicroservices;
import com.employe.repository.EmployeeRepository;
import com.employe.userRequest.UserLeaveRequest;
import com.employe.userRequest.employeeUserRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeService {

	private static final String managerBaseUrl = "http://localhost:8084/manager";

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private EmployeeRepository employeeRepository;

	public Object DeleteById(Long id) throws Exception {
		return null;
	}

	public EmployeeMicroservices UpdateById(Long id, EmployeeMicroservices employee) throws Exception {

		return null;
	}

	public EmployeeMicroservices GetById(Long id) throws Exception {
		EmployeeMicroservices employee=employeeRepository.getByEmployeeId(id);
		if(employee==null)throw new Exception("Employee id not Found");
		return  employee;
	}

	public Object Insert(employeeUserRequest employe) throws Exception {
		log.info("************inside Insert EmployeeService");
		EmployeeMicroservices employeeMicroservices = new EmployeeMicroservices(null, employe.getEmployeeId(),
				employe.getAccesCode(), employe.getName(), employe.getMobile(), employe.getDesignation(),
				employe.getPassword(), employe.getEmail(), employe.getStartDate(), null, employe.getSkill(),
				employe.getManagerId());
		if (!employe.getDesignation().equalsIgnoreCase("MANAGER")&& !employe.getDesignation().equalsIgnoreCase("HR")) {
			log.info("*********before calling manager getById endPoint");
			ResponseEntity<Object> ob = restTemplate.exchange(managerBaseUrl + "/getById/" + employe.getManagerId(),
					HttpMethod.GET, null, Object.class);
			log.info("*********after calling manager getById endPoint");

			if (ob.getBody() != null) {
				employeeRepository.save(employeeMicroservices);

				 restTemplate.exchange(managerBaseUrl+"/addEmployeeId", HttpMethod.PUT,new
				 HttpEntity<employeeUserRequest>(employe), Void.class);
				return "Employee Saved ";
			} else {
				log.error("********giver Manager id not found");
				throw new Exception("Manager Id not Found");
				// return new ResponseEntity<Object>("Manager id not Found
				// ",HttpStatus.BAD_REQUEST);
			}

		} else {
			employeeMicroservices.setManagerId(employe.getEmployeeId());
			restTemplate.exchange(managerBaseUrl+"/insert", HttpMethod.POST,new HttpEntity<employeeUserRequest>(employe), Void.class);
			employeeRepository.save(employeeMicroservices);
			return "Employee Saved ";
		}
	}

	public Object GetAllEmployes() throws Exception {

		return employeeRepository.findAll();
	}

	public EmployeeMicroservices getHr() throws Exception {
		log.info("************inside getHr EmployeeService");
		EmployeeMicroservices hr=employeeRepository.findByDesignation("HR");
		if(hr==null)throw new Exception("Hr not Found");
		return hr;
	}

}
