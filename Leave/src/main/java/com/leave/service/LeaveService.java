package com.leave.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.leave.entity.EmployeeLeave;
import com.leave.repository.LeaveRepository;
import com.leave.userRequest.UserLeaveRequest;
import com.leave.userRequest.employeeUserRequest;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaveService {

	@Autowired
	private LeaveRepository repo;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private TemplateEngine templateEngine;
	
	private static final String employeeBaseUrl = "http://localhost:8083/employee";
	private static final String managerBaseUrl = "http://localhost:8084/manager";

	public String ApplyLeave(UserLeaveRequest request, String tokenHeader, Long paramEmployeeId) throws Exception {
		log.info("*************inside applyLeave LeaveService");
		EmployeeLeave leave = new EmployeeLeave(null, null, request.getLeaveCode(), request.getFromDate(), request.getToDate(), request.getReason(), null, null);
		if(!request.getFile().isEmpty())leave.setLeaveFile(request.getFile());
		String token = tokenHeader.substring(7);
		log.info("********before extrace token");
		Long managerId = Long.valueOf(jwtService.extractEmployeeId(token));
		log.info("********after extract token id: " + managerId);
		//extracting employee details
		ResponseEntity<employeeUserRequest> employee = restTemplate.exchange(
				employeeBaseUrl + "getById/" + paramEmployeeId, HttpMethod.GET, null, employeeUserRequest.class);
		if (employee.getBody().getEmployeeId() == null)
			throw new Exception("Employee id not Found");
		leave.setEmployeeId(employee.getBody().getEmployeeId());
		leave.setAppliedBy(managerId);
		String employeeEmail = employee.getBody().getEmail();
		//extracting manager details
		ResponseEntity<employeeUserRequest> manager = restTemplate.exchange(managerBaseUrl, HttpMethod.GET, null,
				employeeUserRequest.class);
		String managerEmail=manager.getBody().getEmail();
		//extracting hr details
		ResponseEntity<employeeUserRequest> hr=restTemplate.exchange(employeeBaseUrl+"/getHr/", HttpMethod.GET, manager, employeeUserRequest.class);
		String hrEmail=hr.getBody().getEmail();
		//Sending Mail
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message,true);
		helper.setTo(managerEmail);
		InternetAddress[] ccRecipients = new InternetAddress[2]; // Assuming two recipients
		 ccRecipients[0] = new InternetAddress(hrEmail);
		 ccRecipients[1] = new InternetAddress(employeeEmail);
		// Set the CC recipients in your email message
		helper.setCc(ccRecipients);
        helper.setSubject("Leave Email");

        Context context=new Context();
        context.setVariable("Manager", manager.getBody().getName());
        context.setVariable("Employee", employee.getBody().getName());
        context.setVariable("leaveFromDate", request.getFromDate());
        context.setVariable("leaveEndDate", request.getToDate()	);
        context.setVariable("reason", request.getReason());
        LocalDate fromDate=request.getFromDate();
        LocalDate toDate=request.getToDate();
        boolean isHalfDay = request.isHalfDay(); // Check the half-day flag
        if (isHalfDay) {
           
        	context.setVariable("numberOfDays", "HalfDay");
        }
        else if (fromDate != null && toDate != null) {
            long numberOfDays = ChronoUnit.DAYS.between(fromDate, toDate);
            context.setVariable("numberOfDays", numberOfDays);

        } else if (fromDate != null) {
            context.setVariable("numberOfDays", 01);
 
        }
        String emailContent = templateEngine.process("email-template.html", context);

        helper.setText(emailContent);
        javaMailSender.send(message);
        log.info("******Email Sent Sucefully");
        return "Leave Applied Sucefully";
	}

}
