package com.leave.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.leave.entity.EmployeeLeave;
import com.leave.repository.LeaveRepository;
import com.leave.userRequest.UserLeaveRequest;
import com.leave.userRequest.employeeUserRequest;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
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
	private static final String leaveTypeBaseUrl="http://localhost:8086/leaveType";
	public EmployeeLeave ApplyLeave(UserLeaveRequest request, String tokenHeader, Long paramEmployeeId,MultipartFile file) throws Exception {
		log.info("*************inside applyLeave LeaveService");
		EmployeeLeave leave = new EmployeeLeave(null, null, request.getLeaveCode(), request.getFromDate(), request.getToDate(), request.getReason(), null, null,"PENDING");
		//setting file
		if(file!=null)leave.setLeaveFile(file);
		String token = tokenHeader.substring(7);
		log.info("********before extrace token");
		Long selfId = Long.valueOf(jwtService.extractEmployeeId(token));
		log.info("********after extract token id: " + selfId);
		
		//extracting employee details
		ResponseEntity<employeeUserRequest> employee=new ResponseEntity<employeeUserRequest>(HttpStatus.OK);
		ResponseEntity<employeeUserRequest> selfEmploye=new ResponseEntity<employeeUserRequest>(HttpStatus.OK);
		if(paramEmployeeId!=null) {
		 employee = restTemplate.exchange(
				employeeBaseUrl + "/getById/" + paramEmployeeId, HttpMethod.GET, null, employeeUserRequest.class);
		if (employee.getBody() == null)
			throw new Exception("Employee id not Found");
		leave.setEmployeeId(employee.getBody().getEmployeeId());
		leave.setAppliedBy(selfId);
		
		//extracting manager details
		
		 selfEmploye = restTemplate.exchange(employeeBaseUrl+"/getById/"+selfId, HttpMethod.GET, null,
				employeeUserRequest.class);
		 if(selfEmploye.getBody()==null)throw new Exception("Manager Id not found");

	}
		if(paramEmployeeId==null) {
			employee= restTemplate.exchange(employeeBaseUrl+"/getById/"+selfId, HttpMethod.GET, null,
					employeeUserRequest.class);
			selfEmploye = restTemplate.exchange(employeeBaseUrl+"/getById/"+selfId, HttpMethod.GET, null,
					employeeUserRequest.class);
			leave.setEmployeeId(employee.getBody().getEmployeeId());
			leave.setAppliedBy(selfEmploye.getBody().getEmployeeId());
		}
	//	String managerEmail=selfEmploye.getBody().getEmail();
		
		//extracting hr details
		ResponseEntity<employeeUserRequest> hr=restTemplate.exchange(employeeBaseUrl+"/getHr", HttpMethod.GET, selfEmploye, employeeUserRequest.class);
		if(hr.getBody()==null) throw new Exception("Hr Details not Found");
			
		String hrEmail=hr.getBody().getEmail();
		//Sending Mail
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message,true);
		
		helper.setTo(selfEmploye.getBody().getEmail());
		
		//Get the Leave Type Description using leave code
		log.info("********fetch description from leavetype using leavecode :"+request.getLeaveCode());
		helper.setSubject(restTemplate.exchange(leaveTypeBaseUrl+"/getDescription/"+request.getLeaveCode(), HttpMethod.GET, null, String.class).getBody());
		InternetAddress[] ccRecipients = new InternetAddress[2]; // Assuming two recipients
		 ccRecipients[0] = new InternetAddress(hrEmail);
		 ccRecipients[1] = new InternetAddress(employee.getBody().getEmail());
		// Set the CC recipients in your email message
		helper.setCc(ccRecipients);
        helper.setSubject("Leave Email");
        if(file!=null)helper.addAttachment(file.getOriginalFilename(), new ByteArrayResource(file.getBytes()));
        Context context=new Context();
        context.setVariable("Manager", selfEmploye.getBody().getName());
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
        helper.setText(emailContent,true);
        javaMailSender.send(message);
        
        log.info("******Email Sent Sucefully");
        return repo.save(leave);
	}
	

	public Object DeleteLeave(Long id) throws Exception {
		EmployeeLeave leave = repo.findById(id).orElseThrow(() -> new Exception("There is no applied leave on given id "));
		repo.deleteById(id);
		return "Leave Deleted sucedfully";
	}

	public Object getAllEmployeesLeaveData() {
		log.info("*********inside getAllEmployeesLeaveData ");
		
		return repo.findAll();
	}

	public EmployeeLeave ApproveLeave(Long id) throws Exception {
		log.info("*********inside approveLeave LeaveService");
		EmployeeLeave leave=repo.findById(id).orElseThrow(()->new Exception("There is no applied leave on given id"));
		leave.setLeaveStatus("APPROVED");
		repo.save(leave);
		log.info("******retrinve employee details ");
		Long employeeId=leave.getEmployeeId();
		ResponseEntity<employeeUserRequest> employee=restTemplate.exchange(employeeBaseUrl+"/getById"+employeeId, HttpMethod.GET, null, employeeUserRequest.class);
		log.info("**********Sending Approved Email");
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message,true);
		helper.setTo(employee.getBody().getEmail());
		helper.setSubject("Leave Rejected ");
		
		log.debug("**********set variable to context");
		Context context=new Context();
		context.setVariable("employee", employee.getBody().getName());
		context.setVariable("description", "your leave is Rejected . Please continue you Duty.");
		context.setVariable("status", "Leave Rejected");
		helper.setText(templateEngine.process("leaveApproved-template.html", context), true);
		javaMailSender.send(message);
		return leave;
		
	}


	public EmployeeLeave RejectLeave(Long id) throws Exception {
		log.info("*********inside RejectLeave LeaveService");
		EmployeeLeave leave=repo.findById(id).orElseThrow(()->new Exception("There is no applied leave on given id"));
		leave.setLeaveStatus("REJECTED");
		repo.save(leave);
		log.info("******retrinve employee details ");
		Long employeeId=leave.getEmployeeId();
		ResponseEntity<employeeUserRequest> employee=restTemplate.exchange(employeeBaseUrl+"/getById"+employeeId, HttpMethod.GET, null, employeeUserRequest.class);
		log.info("**********Sending Approved Email");
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message,true);
		helper.setTo(employee.getBody().getEmail());
		helper.setSubject("Leave Approval ");
		log.debug("**********set variable to context");
		Context context=new Context();
		context.setVariable("employee", employee.getBody().getName());
		context.setVariable("Description", "your leave is approved. Take care and come back after your leave.");
		context.setVariable("status", "Leave Approved");
		helper.setText(templateEngine.process("leaveApproved-template.html", context), false);
		javaMailSender.send(message);
		return leave;
	}

	

}
