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

import com.google.common.net.HttpHeaders;
import com.leave.entity.EmployeeLeave;
import com.leave.entity.EmployeeLeaveSummary;
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
	private static final String leaveTypeBaseUrl = "http://localhost:8086/leaveType";

	public EmployeeLeave ApplyLeave(UserLeaveRequest request, String tokenHeader, Long paramEmployeeId,
			MultipartFile file) throws Exception {
		log.info("*************inside applyLeave LeaveService");
		 boolean isHalfDayLeave=false;
		if(request.getIsHalfDayLeave()!=null) {
			isHalfDayLeave= request.getIsHalfDayLeave();// You need to define this in your UserLeaveRequest
	    log.info("********* isHalfDayLeave : "+isHalfDayLeave);
		}
	    LocalDate fromDate = request.getFromDate();
	    LocalDate toDate = request.getToDate();

	    if (isHalfDayLeave) {
	        // Handle half-day leave
	    	log.info("******* Employee Taken HalfDay");
	        if (fromDate != null) {
	            // If both fromDate and toDate are specified for half-day leave, adjust them as needed
	            fromDate = fromDate.atTime(12, 0).toLocalDate(); // Set the time to noon
	            log.info("*********fromDate "+fromDate);
	            if(toDate !=null ) {
	            toDate = toDate.atTime(12, 0).toLocalDate(); // Set the time to noon
	            log.info("*********todate : "+toDate);
	            }
	        }
	    }
		EmployeeLeave leave = new EmployeeLeave(null, null, request.getLeaveCode(),request.getFromDate(),
			request.getToDate(),	request.getReason(), null, null, "PENDING", null,false);
		leave.setIsHalfDayLeave(isHalfDayLeave);
		if(leave.getToDate()==null)leave.setToDate(leave.getFromDate());
		if(leave.getFromDate()==null)leave.setFromDate(leave.getToDate());
		
		
		
		
		if (file != null)
			leave.setLeaveFile(file);
		String token = tokenHeader.substring(7);
		String managerMail = null;
		log.info("********before extrace token");
		Long tokenId = Long.valueOf(jwtService.extractEmployeeId(token));
		log.info("********after extract token id: " + tokenId);

		// extracting employee details
		ResponseEntity<employeeUserRequest> employee = new ResponseEntity<employeeUserRequest>(HttpStatus.OK);
		ResponseEntity<employeeUserRequest> tokenEmployee = new ResponseEntity<employeeUserRequest>(HttpStatus.OK);
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		Context context = new Context();

		if (paramEmployeeId != null) {
			employee = restTemplate.exchange(employeeBaseUrl + "/getById/" + paramEmployeeId, HttpMethod.GET, null,
					employeeUserRequest.class);
			if (employee.getBody() == null)
				throw new Exception("Employee id not Found");
			leave.setEmployeeId(employee.getBody().getEmployeeId());
			leave.setAppliedBy(tokenId);
			leave.setManagerId(employee.getBody().getManagerId());
			// extracting manager details

			tokenEmployee = restTemplate.exchange(employeeBaseUrl + "/getById/" + tokenId, HttpMethod.GET, null,
					employeeUserRequest.class);
			if (tokenEmployee.getBody() == null)
				throw new Exception(" Id not found");

			helper.setTo(tokenEmployee.getBody().getEmail());
			managerMail = tokenEmployee.getBody().getEmail();
			context.setVariable("Manager", tokenEmployee.getBody().getName());
			context.setVariable("Employee", employee.getBody().getName());

		}
		if (paramEmployeeId == null) {

			tokenEmployee = restTemplate.exchange(employeeBaseUrl + "/getById/" + tokenId, HttpMethod.GET, null,
					employeeUserRequest.class);
			employee = restTemplate.exchange(employeeBaseUrl + "/getById/" + tokenEmployee.getBody().getManagerId(),
					HttpMethod.GET, null, employeeUserRequest.class);
			leave.setManagerId(tokenEmployee.getBody().getManagerId());
			leave.setEmployeeId(tokenEmployee.getBody().getEmployeeId());
			leave.setAppliedBy(tokenEmployee.getBody().getEmployeeId());
			helper.setTo(employee.getBody().getEmail());
			managerMail = employee.getBody().getEmail();
			context.setVariable("Manager", employee.getBody().getName());
			context.setVariable("Employee", tokenEmployee.getBody().getName());

		}
		// String managerEmail=selfEmploye.getBody().getEmail();

		// extracting hr details
		ResponseEntity<employeeUserRequest> hr = restTemplate.exchange(employeeBaseUrl + "/getHr", HttpMethod.GET,
				tokenEmployee, employeeUserRequest.class);
		if (hr.getBody() == null)
			throw new Exception("Hr Details not Found");

		String hrEmail = hr.getBody().getEmail();
		// Sending Mail

		// Get the Leave Type Description using leave code
		log.info("********fetch description from leavetype using leavecode :" + request.getLeaveCode());
		helper.setSubject(restTemplate.exchange(leaveTypeBaseUrl + "/getDescription/" + request.getLeaveCode(),
				HttpMethod.GET, null, String.class).getBody());
		InternetAddress[] ccRecipients = new InternetAddress[2]; // Assuming two recipients
		ccRecipients[0] = new InternetAddress(hrEmail);
		ccRecipients[1] = new InternetAddress(tokenEmployee.getBody().getEmail());
		// Set the CC recipients in your email message
		helper.setCc(ccRecipients);
		if (file != null)
			helper.addAttachment(file.getOriginalFilename(), new ByteArrayResource(file.getBytes()));
		context.setVariable("leaveFromDate", request.getFromDate());
		context.setVariable("leaveEndDate", request.getToDate());
		context.setVariable("reason", request.getReason());
		if (isHalfDayLeave) {

			context.setVariable("numberOfDays", "HalfDay");
		} else if (fromDate != null && toDate != null) {

		} else if (fromDate != null) {
			context.setVariable("numberOfDays", 01);

		}
		String emailContent = templateEngine.process("email-template.html", context);
		helper.setText(emailContent, true);
		javaMailSender.send(message);

		log.info("******Email Sent Sucefully manager : " + managerMail);
		return repo.save(leave);
	}

	public Object DeleteLeave(Long id) throws Exception {
		EmployeeLeave leave = repo.findById(id)
				.orElseThrow(() -> new Exception("There is no applied leave on given id "));
		repo.deleteById(id);
		return "Leave Deleted sucedfully";
	}

	public Object getAllEmployeesLeaveData(HttpServletRequest request) {
		Long managerId = Long
				.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7)));
		log.info("*********inside getAllEmployeesLeaveData using ManagerId :  " + managerId);

		return repo.findByManagerIdAndLeaveStatus(managerId, "PENDING");
	}

	public EmployeeLeave ApproveLeave(Long id) throws Exception {
		log.info("*********inside approveLeave LeaveService");
		EmployeeLeave leave = repo.findById(id)
				.orElseThrow(() -> new Exception("There is no applied leave on given id"));
		leave.setLeaveStatus("APPROVED");
		repo.save(leave);
		log.info("******retrinve employee details ");
		Long employeeId = leave.getEmployeeId();
		ResponseEntity<employeeUserRequest> employee = restTemplate.exchange(employeeBaseUrl + "/getById/" + employeeId,
				HttpMethod.GET, null, employeeUserRequest.class);
		log.info("**********Sending Approved Email");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(employee.getBody().getEmail());
		helper.setSubject("Leave Rejected ");

		log.debug("**********set variable to context");
		Context context = new Context();
		context.setVariable("employee", employee.getBody().getName());
		context.setVariable("description", "your leave is Rejected . Please continue you Duty.");
		context.setVariable("status", "Leave Rejected");
		helper.setText(templateEngine.process("leaveApproved-template.html", context), true);
		javaMailSender.send(message);
		return leave;

	}

	public EmployeeLeave RejectLeave(Long id) throws Exception {
		log.info("*********inside RejectLeave LeaveService");
		EmployeeLeave leave = repo.findById(id)
				.orElseThrow(() -> new Exception("There is no applied leave on given id"));
		leave.setLeaveStatus("REJECTED");
		repo.save(leave);
		log.info("******retrinve employee details ");
		Long employeeId = leave.getEmployeeId();
		ResponseEntity<employeeUserRequest> employee = restTemplate.exchange(employeeBaseUrl + "/getById/" + employeeId,
				HttpMethod.GET, null, employeeUserRequest.class);
		log.info("**********Sending Approved Email");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(employee.getBody().getEmail());
		helper.setSubject("Leave Approval ");
		log.debug("**********set variable to context");
		Context context = new Context();
		context.setVariable("employee", employee.getBody().getName());
		context.setVariable("Description", "your leave is approved. Take care and come back after your leave.");
		context.setVariable("status", "Leave Approved");
		helper.setText(templateEngine.process("leaveApproved-template.html", context), false);
		javaMailSender.send(message);
		return leave;
	}

	public Object MyLeaves(HttpServletRequest request) {
		log.info("*******inside MyLeaves Leavie Service");
		return repo.findByEmployeeId(
				Long.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7))));
	}

	public Object deleteMyLeave(Long id,HttpServletRequest request) throws Exception {
		log.info("*********inside DeleteMyLeave LeaveService");
		EmployeeLeave employeeLeave = repo.findById(id).orElseThrow(() -> new Exception("Leave Id Not Found"));
		log.info("********after fetching employeeLeave ");
		if(employeeLeave.getEmployeeId()!=Long.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7))))throw new Exception("Applied leave is not urs");
		repo.delete(employeeLeave);
		return employeeLeave;
	}

	public Float takenLeaves(Long employeeId,String status,LocalDate requeriedMonth) {
		log.info("*********inside takenLeaves leaveService");
		LocalDate startDate = requeriedMonth.minusMonths(1).withDayOfMonth(26);
		LocalDate endDate = requeriedMonth.withDayOfMonth(25);
		Float ob = repo.findApprovedLeaveDaysInCurrentMonth(employeeId,status, startDate, endDate);
		log.info("*******" + ob);
		return ob;
	}

	public List<EmployeeLeave> monthlyLeave(Long employeeId, String leaveStatus, LocalDate requiredMonth) {
	    log.info("*********inside monthlyLeave leaveService");

	    LocalDate startDate = requiredMonth.minusMonths(1).withDayOfMonth(26);
	    LocalDate endDate = requiredMonth.withDayOfMonth(25);

	    List<EmployeeLeave> leaveRecords = repo.findCurrentLeaves(employeeId, leaveStatus, startDate, endDate);

	    // Modify the results to match your requirement
	    List<EmployeeLeave> modifiedResults = new ArrayList<>();

	    for (EmployeeLeave leave : leaveRecords) {
	        LocalDate fromDate = leave.getFromDate();
	        LocalDate toDate = leave.getToDate();

	        // Check if the leave period overlaps with your specified month
	        if ((fromDate.isBefore(endDate)||fromDate.isEqual(endDate))&& (toDate == null || toDate.isAfter(startDate) || toDate.isEqual(startDate))) {
	            // Adjust the fromDate if it's before the start of the specified month
	            if (fromDate.isBefore(startDate)||fromDate.isEqual(startDate)) {
	                fromDate = startDate;
	            }

	            // Adjust the toDate if it's after the end of the specified month
	            if (toDate != null && (toDate.isAfter(endDate)|| toDate.isEqual(endDate))) {
	                toDate = endDate;
	            }

	            // Create a copy of the original leave with adjusted dates
	            EmployeeLeave modifiedLeave = new EmployeeLeave(leave);
	            modifiedLeave.setFromDate(fromDate);
	            if (toDate != null) {
	                modifiedLeave.setToDate(toDate);
	            }
	            modifiedResults.add(modifiedLeave);
	        }
	       
	        else {
	            // If the leave does not overlap with the specified month, add it as-is
	            modifiedResults.add(leave);
	        }
	    }

	    log.info("*******" + modifiedResults);
	    return modifiedResults;
	}




}
