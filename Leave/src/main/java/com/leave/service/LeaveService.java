package com.leave.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.repository.CrudRepository;
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
import com.leave.entity.Tracking;
import com.leave.exception.EmployeeNotFoundException;
import com.leave.exception.LeaveIdNotFoundException;
import com.leave.repository.EmployeeLeaveDataTracking;
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
	@Autowired
	private EmployeeLeaveDataTracking trackingRepo;

	private static final String employeeBaseUrl = "http://localhost:8083/employee";
	private static final String managerBaseUrl = "http://localhost:8084/manager";
	private static final String leaveTypeBaseUrl = "http://localhost:8086/leaveType";

	// Leave Application Code

	public ResponseEntity<?> ApplyLeave(UserLeaveRequest request, String tokenHeader, Long paramEmployeeId,
			MultipartFile file) throws Exception {
		log.info("*************inside applyLeave LeaveService");
		boolean isHalfDayLeave = false;

		// Checking leave Request is halfDate or not

		if (request.getIsHalfDayLeave() != null) {
			isHalfDayLeave = request.getIsHalfDayLeave();// You need to define this in your UserLeaveRequest
			log.info("********* isHalfDayLeave : " + isHalfDayLeave);
		}
		LocalDate fromDate = request.getFromDate();
		LocalDate toDate = request.getToDate();

		if (isHalfDayLeave) {
			// Handle half-day leave
			log.info("******* Employee Taken HalfDay");
			if (fromDate != null) {
				// If both fromDate and toDate are specified for half-day leave, adjust them as
				// needed
				fromDate = fromDate.atTime(12, 0).toLocalDate(); // Set the time to noon
				log.info("*********fromDate " + fromDate);
				if (toDate != null) {
					toDate = toDate.atTime(12, 0).toLocalDate(); // Set the time to noon
					log.info("*********todate : " + toDate);
				}
			}
		}

		// Data Transfor from user leave request to employeeLeave Entity
		EmployeeLeave leave = new EmployeeLeave(null, null, request.getLeaveCode(), request.getFromDate(),
				request.getToDate(), request.getReason(), null, null, "PENDING", null, false);
		leave.setIsHalfDayLeave(isHalfDayLeave);
		if (leave.getToDate() == null)
			leave.setToDate(leave.getFromDate());
		if (leave.getFromDate() == null)
			leave.setFromDate(leave.getToDate());

		// file checking
		if (file != null)
			leave.setLeaveFile(file);

		// storing token into one variable
		String token = tokenHeader.substring(7);
		String managerMail = null;
		log.info("********before extrace token");

		// extract token and storing id into one varivable
		Long tokenId = Long.valueOf(jwtService.extractEmployeeId(token));
		log.info("********after extract token id: " + tokenId);

		// For LongedIn Employee
		ResponseEntity<employeeUserRequest> employee = null;

		// For Other Employee
		ResponseEntity<employeeUserRequest> tokenEmployee = null;
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		Context context = new Context();

		// If any EmployeeId in Url this if condition will be execute
		// It means an Authorized person applying leave for other employee

		if (paramEmployeeId != null) {
			log.info("********* paramEmployee id : " + paramEmployeeId);

			// Checking employee leave request fromDate and toDate with his past leaveDate
			// dates
			// If Employee applying leave Date match with his past leaveData date then throw
			// excption
			// Date  Mathcing conditions
			// request fromDate and toDate between his past leavedata fromDate and toDate or
			// past leaveData fromDate and toDate between the request fromDate and toDate
			if (repo.hasLeaveOnDate(paramEmployeeId, fromDate, toDate) > 0)
				throw new EmployeeNotFoundException("Leave application on this date already exists for the employee.");
			
			// extracting  Employee  details. who want  leave  from employeeservie 

			employee = restTemplate.exchange(employeeBaseUrl + "/getOthers/" + paramEmployeeId, HttpMethod.GET, null,
					employeeUserRequest.class);
			log.info("employee : " + employee.toString());
			leave.setEmployeeId(employee.getBody().getEmployeeId());
			leave.setAppliedBy(tokenId);
			leave.setManagerId(employee.getBody().getManagerId());
			
			
			// extracting LoggedIn Employee  details from employeeservie 
			
			tokenEmployee = restTemplate.exchange(employeeBaseUrl + "/getOthers/" + tokenId, HttpMethod.GET, null,
					employeeUserRequest.class);


			helper.setTo(tokenEmployee.getBody().getEmail());
			managerMail = tokenEmployee.getBody().getEmail();
			context.setVariable("Manager", tokenEmployee.getBody().getName());
			context.setVariable("Employee", employee.getBody().getName());

		}
		
		// If There is no EmployeeId in Url this if condition will be execute
		// It means Logged in Employee applying leave for his self
		
		if (paramEmployeeId == null) {
			log.info("**** self employee leave apply");
			log.info("******** " + tokenId);
			// Checking employee leave request fromDate and toDate with his past leaveDate
			// dates
			// If Employee applying leave Date match with his past leave date then throw
			// excption
			// Date  Mathcing conditions
			// request fromDate and toDate between his past leavedata fromDate and toDate or
			// past leaveData fromDate and toDate between the request fromDate and toDate
			Long count = repo.hasLeaveOnDate(tokenId, fromDate, toDate);
			log.info("Count : " + count);
			if (count > 0)
				throw new EmployeeNotFoundException("Leave application on this date already exists for the employee.");
			
			tokenEmployee = restTemplate.exchange(employeeBaseUrl + "/getOthers/" + tokenId, HttpMethod.GET, null,
					employeeUserRequest.class);
			log.info("********tokenEmployee " + tokenEmployee.toString());

			employee = restTemplate.exchange(employeeBaseUrl + "/getOthers/" + tokenEmployee.getBody().getManagerId(),
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

		String hrEmail = hr.getBody().getEmail();
		// Sending Mail

		// Get the Leave Type Description using leave code
		log.info("********fetch description from leavetype using leavecode :" + request.getLeaveCode());
		ResponseEntity<String> subject = restTemplate.exchange(
				leaveTypeBaseUrl + "/getDescription/" + request.getLeaveCode(), HttpMethod.GET, null, String.class);
		if (subject.getStatusCode() == HttpStatus.BAD_REQUEST)
			throw new LeaveIdNotFoundException(subject.getBody());
		helper.setSubject(subject.getBody());
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
		return ResponseEntity.ok(repo.save(leave));
	}

	public Object DeleteLeave(Long id, HttpServletRequest request) throws Exception {
		log.info("*********inside deleteleave delete service");
		Long loingEmployeeId = Long
				.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7)));
		log.info("****after extract employee id : " + loingEmployeeId);
		EmployeeLeave leave = repo.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Leave id not Found"));
		log.info("********* leave : " + leave);
		if (leave == null)
			throw new EmployeeNotFoundException("There is no applied leave on given id ");
		if (!leave.getLeaveStatus().equalsIgnoreCase("PENDING"))
			throw new EmployeeNotFoundException("You Can Cancle Only Pending Leaves");

		Tracking trackData = new Tracking(null, leave.getEmployeeId(), LocalDateTime.now().toString(), leave.toString(),
				loingEmployeeId, "Cancle");
		trackingRepo.save(trackData);
		repo.deleteById(id);
		return leave;
	}

	public Object getAllEmployeesLeaveData(HttpServletRequest request) {
		Long managerId = Long
				.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7)));
		log.info("*********inside getAllEmployeesLeaveData using ManagerId :  " + managerId);

		return repo.findByManagerIdAndLeaveStatus(managerId, "PENDING");
	}

	public EmployeeLeave ApproveLeave(Long id, HttpServletRequest request) throws Exception {
		Long loingEmployeeId = Long
				.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7)));
		log.info("*********inside approveLeave LeaveService");
		EmployeeLeave leave = repo.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("There is no applied leave on given id"));
		leave.setLeaveStatus("APPROVED");
		repo.save(leave);
		log.info("******retrinve employee details ");
		Long employeeId = leave.getEmployeeId();
		ResponseEntity<employeeUserRequest> employee = restTemplate.exchange(
				employeeBaseUrl + "/getOthers/" + employeeId, HttpMethod.GET, null, employeeUserRequest.class);
		if (employee.getStatusCode() == HttpStatus.BAD_REQUEST)
			throw new EmployeeNotFoundException(employee.getBody().toString());
		log.info("**********Sending Approved Email");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(employee.getBody().getEmail());
		helper.setSubject("Leave APPROVED ");

		log.debug("**********set variable to context");
		Context context = new Context();
		context.setVariable("employee", employee.getBody().getName());
		context.setVariable("description", "your leave is approved . Please continue you Duty.");
		context.setVariable("status", "Leave aprroved");
		helper.setText(templateEngine.process("leaveApproved-template.html", context), true);
		javaMailSender.send(message);
		Tracking trackData = new Tracking(null, leave.getEmployeeId(), LocalDateTime.now().toString(), leave.toString(),
				loingEmployeeId, "LEAVE APPROVED");
		trackingRepo.save(trackData);
		return leave;

	}

	public EmployeeLeave RejectLeave(Long id, HttpServletRequest request) throws Exception {
		log.info("*********inside RejectLeave LeaveService");
		Long loingEmployeeId = Long
				.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7)));

		EmployeeLeave leave = repo.findById(id)
				.orElseThrow(() -> new Exception("There is no applied leave on given id"));
		leave.setLeaveStatus("REJECTED");
		repo.save(leave);
		log.info("******retrinve employee details ");
		Long employeeId = leave.getEmployeeId();
		ResponseEntity<employeeUserRequest> employee = restTemplate.exchange(
				employeeBaseUrl + "/getOthers/" + employeeId, HttpMethod.GET, null, employeeUserRequest.class);

		log.info("**********Sending Approved Email");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(employee.getBody().getEmail());
		helper.setSubject("Leave Approval ");
		log.debug("**********set variable to context");
		Context context = new Context();
		context.setVariable("employee", employee.getBody().getName());
		context.setVariable("Description", "your leave is REJECTED. Take care and come back after your leave.");
		context.setVariable("status", "Leave REJECTED");
		helper.setText(templateEngine.process("leaveApproved-template.html", context), false);
		javaMailSender.send(message);
		Tracking trackData = new Tracking(null, leave.getEmployeeId(), LocalDateTime.now().toString(), leave.toString(),
				loingEmployeeId, "LEAVE REJECTED");
		trackingRepo.save(trackData);
		return leave;
	}

	public Object MyLeaves(HttpServletRequest request) {
		log.info("*******inside MyLeaves Leavie Service");
		return repo.findByEmployeeId(
				Long.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7))));
	}

	public Object deleteMyLeave(Long id, HttpServletRequest request) throws Exception {
		log.info("*********inside DeleteMyLeave LeaveService");

		Long loingEmployeeId = Long
				.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7)));

		EmployeeLeave employeeLeave = repo.findById(id).orElseThrow(() -> new Exception("Leave Id Not Found"));
		if (employeeLeave.getLeaveStatus() != "PENDING")
			throw new EmployeeNotFoundException("Only Pending Leaves Can Be Deleted");

		log.info("********after fetching employeeLeave ");
		if (employeeLeave.getEmployeeId() != Long
				.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7))))
			throw new Exception("Applied leave is not urs");
		Tracking trackData = new Tracking(null, employeeLeave.getEmployeeId(), LocalDateTime.now().toString(),
				employeeLeave.toString(), loingEmployeeId, "Cancle");
		trackingRepo.save(trackData);
		repo.delete(employeeLeave);

		return employeeLeave;
	}

	public Float takenLeaves(Long employeeId, String status, String monthDate) {
		log.info("*********inside takenLeaves leaveService");
		LocalDate requiredMonth;

		try {
			// Define a custom date format pattern "yyyy-MM-dd"
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			// Append "01" to the input date to make it "yyyy-MM-01"
			String fullDate = monthDate + "-01";

			// Parse the complete date
			requiredMonth = LocalDate.parse(fullDate, formatter);
		} catch (DateTimeParseException e) {
			log.error("Invalid date format: " + monthDate);
			throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM format.");
		}

		log.info("required Month : " + requiredMonth);

//	
		LocalDate startDate = requiredMonth.minusMonths(1).withDayOfMonth(26);
		LocalDate endDate = requiredMonth.withDayOfMonth(25);
		Float ob = repo.findApprovedLeaveDaysInCurrentMonth(employeeId, status, startDate, endDate);
		log.info("*******" + ob);
		return ob;
	}

	public List<EmployeeLeave> monthlyLeave(Long employeeId, String leaveStatus, String monthDate) {
		log.info("*********inside monthlyLeave leaveService");
		LocalDate requiredMonth;

		try {
			// Define a custom date format pattern "yyyy-MM-dd"
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			// Append "01" to the input date to make it "yyyy-MM-01"
			String fullDate = monthDate + "-01";

			// Parse the complete date
			requiredMonth = LocalDate.parse(fullDate, formatter);
		} catch (DateTimeParseException e) {
			log.error("Invalid date format: " + monthDate);
			throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM format.");
		}

		log.info("required Month : " + requiredMonth);

		LocalDate startDate = requiredMonth.minusMonths(1).withDayOfMonth(26);
		LocalDate endDate = requiredMonth.withDayOfMonth(25);

		List<EmployeeLeave> leaveRecords = repo.findCurrentLeaves(employeeId, leaveStatus, startDate, endDate);
		return modifiedResults(leaveRecords, startDate, endDate);
	}

	public List<EmployeeLeave> HrLevelMonthyLeave(String leaveStatus, String monthDate) {
		log.info("*********inside monthlyLeave leaveService");
		LocalDate requiredMonth;

		try {
			// Define a custom date format pattern "yyyy-MM-dd"
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			// Append "01" to the input date to make it "yyyy-MM-01"
			String fullDate = monthDate + "-01";

			// Parse the complete date
			requiredMonth = LocalDate.parse(fullDate, formatter);
		} catch (DateTimeParseException e) {
			log.error("Invalid date format: " + monthDate);
			throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM format.");
		}

		log.info("required Month : " + requiredMonth);

		LocalDate startDate = requiredMonth.minusMonths(1).withDayOfMonth(26);
		LocalDate endDate = requiredMonth.withDayOfMonth(25);
		if (leaveStatus.equalsIgnoreCase("ALL")) {
			List<EmployeeLeave> leaveRecords = repo.findAllEmployeesLeaveDataBasedOnLeaveStatusAndDate(startDate,
					endDate);
			return modifiedResults(leaveRecords, startDate, endDate);
		}
		List<EmployeeLeave> leaveRecords = repo.findEmployeesLeaveData(leaveStatus, startDate, endDate);
		return modifiedResults(leaveRecords, startDate, endDate);
	}

	// Modify the results to match your requirement
	public List<EmployeeLeave> modifiedResults(List<EmployeeLeave> leaveRecords, LocalDate startDate,
			LocalDate endDate) {
		List<EmployeeLeave> modifiedResults = new ArrayList<>();

		for (EmployeeLeave leave : leaveRecords) {
			LocalDate fromDate = leave.getFromDate();
			LocalDate toDate = leave.getToDate();

			// Check if the leave period overlaps with your specified month
			if ((fromDate.isBefore(endDate) || fromDate.isEqual(endDate))
					&& (toDate == null || toDate.isAfter(startDate) || toDate.isEqual(startDate))) {
				// Adjust the fromDate if it's before the start of the specified month
				if (fromDate.isBefore(startDate) || fromDate.isEqual(startDate)) {
					fromDate = startDate;
				}

				// Adjust the toDate if it's after the end of the specified month
				if (toDate != null && (toDate.isAfter(endDate) || toDate.isEqual(endDate))) {
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
