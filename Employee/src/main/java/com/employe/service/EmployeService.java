package com.employe.service;

import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.bouncycastle.asn1.x509.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import com.employe.dto.UserDetails;
import com.employe.entity.EmployeeMicroservices;
import com.employe.entity.OTPRecord;
import com.employe.entity.UserPrinciples;
import com.employe.exception.CustomException;
import com.employe.exception.DublicateEmployeeException;
import com.employe.exception.EmployeeNotFoundException;
import com.employe.helper.ConstantValues;
import com.employe.repository.EmployeeRepository;
import com.employe.repository.OTPRecordRepository;
import com.employe.repository.UserPrinciplesRepository;
import com.employe.userRequest.UserLeaveRequest;
import com.employe.userRequest.employeeUserRequest;
import com.thoughtworks.xstream.converters.time.LocalDateConverter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeService {

	private static final String managerBaseUrl = "http://localhost:8084/manager";

	@Autowired
	private RestTemplate restTemplate;
	
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	@Autowired
	private UserPrinciplesRepository userPrinciplesRepository;
	

	
	@Autowired
	private OTPRecordRepository otpRepo;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private JwtService jwtService;
	
	
	public Object DeleteById(Long id) throws Exception {
		log.info("********inside DeleteById EmployeeService");
		EmployeeMicroservices employee=employeeRepository.findByEmployeeId(id);
		if(employee==null)throw new EmployeeNotFoundException("Employee Id Not Found ");
		employee.setStatus("INACTIVE");
		employee.setEndDate(LocalDate.now());
		employee.setIsDeleted(true);
		employeeRepository.save(employee);
		log.info("********** manager Id "+id);
		ResponseEntity<Object> ob = restTemplate.exchange(managerBaseUrl + "/deleteById/" +id,
				HttpMethod.DELETE, null, Object.class);
		
		log.info("*********after calling manager deleteById endPoint");
		return employee;
	}

	public EmployeeMicroservices UpdateById(Long id, employeeUserRequest request) throws Exception {
		log.info("******inside UpdateById employeeService ");
		EmployeeMicroservices employee=employeeRepository.findByEmployeeId(id);
		if(employee==null)throw new EmployeeNotFoundException("Employee Id Not Found");
		employee.setName(request.getName());
		employee.setMobile(request.getMobile());
		employee.setDesignation(request.getDesignation());
		employee.setEmail(request.getEmail());
		employee.setSkill(request.getSkill());
		log.info("**********before calling manager service for removeEmployeeId ");
		ResponseEntity<Object> object= restTemplate.exchange(managerBaseUrl+"/removeEmployeeId/"+employee.getManagerId()+"/"+employee.getEmployeeId(), HttpMethod.PUT, null, Object.class);
//		if(object.getStatusCode()==HttpStatus.BAD_REQUEST)throw new EmployeeNotFoundException(object.getBody().toString());
		employee.setManagerId(request.getManagerId());
		log.info("********** manager Id "+request.getManagerId());
		ResponseEntity<Object> ob = restTemplate.exchange(managerBaseUrl + "/getById/" + request.getManagerId(),
				HttpMethod.GET, null, Object.class);
//		if(ob.getStatusCode()==HttpStatus.BAD_REQUEST)throw new EmployeeNotFoundException(ob.getBody().toString());
		log.info("*********after calling manager getById endPoint");
		ResponseEntity<Object> manager=restTemplate.exchange(managerBaseUrl+"/update/"+id, HttpMethod.PUT, new HttpEntity<employeeUserRequest>(request), Object.class);
//		if(manager.getStatusCode()==HttpStatus.BAD_REQUEST)throw new EmployeeNotFoundException(manager.getBody().toString());

		log.info("*********after updating manager Service ");
		ResponseEntity<Object>obj= restTemplate.exchange(managerBaseUrl+"/addEmployeeId/"+employee.getManagerId()+"/"+employee.getEmployeeId(), HttpMethod.PUT, null, Object.class);
		if(obj.getStatusCode()==HttpStatus.BAD_REQUEST)throw new EmployeeNotFoundException(obj.getBody().toString());

		return employeeRepository.save(employee);
	}

	public ResponseEntity<Object> GetById(Long id) throws Exception {
		log.info("********inside getById employee Service");
		EmployeeMicroservices employee=employeeRepository.getByEmployeeId(id);
		log.info("******employee: "+employee);
		if(employee==null) {
			log.info("*****inside if");
		throw new EmployeeNotFoundException("Employee Id Not Found");
//	  	 	CustomException cu=new CustomException(HttpStatus.BAD_REQUEST, LocalDateTime.now(), "Employee Id Not Found", null, HttpStatus.BAD_REQUEST.value());
//	    	Map<String, Object> map1=new LinkedHashMap<>();
//	    	map1.put(ConstantValues.StatusMessage, cu.getMessage());
////	    	map1.put(ConstantValues.Description, cu.getDescription());
//	    	map1.put(ConstantValues.Timestamp, cu.getTimestamp());
//			map1.put(ConstantValues.statusCode, cu.getStatusCode());
//			map1.put(ConstantValues.Status, cu.getStatus());			
//			return ResponseEntity.badRequest().body(map1);
		}
		log.info("******out side if");
		return  ResponseEntity.ok(employee);
	}

	public EmployeeMicroservices Insert(employeeUserRequest employe) throws Exception {
		log.info("************inside Insert EmployeeService");
		if(employeeRepository.findByEmployeeId(employe.getEmployeeId())!=null) throw new DublicateEmployeeException(" Ducblicate Employee Id");
			
		
		EmployeeMicroservices employeeMicroservices = new EmployeeMicroservices(null, employe.getEmployeeId(),
				 employe.getName(), employe.getMobile(), employe.getDesignation(),
				 employe.getEmail(), employe.getStartDate(), null, employe.getSkill(),
				employe.getManagerId(),"ACTIVE",false);
		UserPrinciples userPrinciples =new UserPrinciples(employe.getEmployeeId(), employe.getPassword(), employe.getAccesCode());
		if ((!employe.getDesignation().equalsIgnoreCase("MANAGER")) && (!employe.getDesignation().equalsIgnoreCase("HR")) && (!employe.getDesignation().equalsIgnoreCase("ADMIN"))) {
			log.info("*********before calling manager getById endPoint");
			log.info("********* Designation"+employe.getDesignation());
			log.info("********** manager Id "+employe.getManagerId());
			ResponseEntity<Object> ob = restTemplate.exchange(managerBaseUrl + "/getById/" + employe.getManagerId(),
					HttpMethod.GET, null, Object.class);
			if(ob.getStatusCode()==HttpStatus.BAD_REQUEST)throw new EmployeeNotFoundException(ob.getBody().toString());
			log.info("*********after calling manager getById endPoint");

		
			EmployeeMicroservices employee=	employeeRepository.save(employeeMicroservices);
				userPrinciplesRepository.save(userPrinciples);

				ResponseEntity<Object> object= restTemplate.exchange(managerBaseUrl+"/addEmployeeId/"+employe.getManagerId()+"/"+employe.getEmployeeId(), HttpMethod.PUT,
				 null, Object.class);
				if(object.getStatusCode()==HttpStatus.BAD_REQUEST)throw new EmployeeNotFoundException(object.getBody().toString());
				return employee;
		

		} else if(employe.getDesignation().equalsIgnoreCase("MANAGER")) {
			employeeMicroservices.setManagerId(employe.getEmployeeId());
			ResponseEntity<Object> manager= restTemplate.exchange(managerBaseUrl+"/insert", HttpMethod.POST,new HttpEntity<employeeUserRequest>(employe), Object.class);
			if(manager.getStatusCode()==HttpStatus.BAD_REQUEST)throw new  DublicateEmployeeException(manager.getBody().toString());
			EmployeeMicroservices employee= employeeRepository.save(employeeMicroservices);
			userPrinciplesRepository.save(userPrinciples);

			return employee;
		}else if (employe.getDesignation().equalsIgnoreCase("HR")| employe.getDesignation().equalsIgnoreCase("ADMIN")) {
			employeeMicroservices.setManagerId(employe.getEmployeeId());
		EmployeeMicroservices empoloyee=	employeeRepository.save(employeeMicroservices);
			userPrinciplesRepository.save(userPrinciples);
			return empoloyee;
		}
		return employeeMicroservices;
	}

	public Object getAllEmployesUnderMe(HttpServletRequest request) throws Exception {
		Long managerId=Long.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7)));
		return employeeRepository.findByManagerId(managerId);
	}

	public ResponseEntity<?> getHr() throws Exception {
		log.info("************inside getHr EmployeeServicee");
		EmployeeMicroservices hr=employeeRepository.findByDesignation("HR");
		if(hr==null)throw new EmployeeNotFoundException ("HR Id Not Found");
		log.info("************after fetching hr details");
		return ResponseEntity.ok(hr);
	}
	


	public EmployeeMicroservices initiatePasswordReset(Long userId) throws UserPrincipalNotFoundException, MessagingException {
		log.info("*****inside PasswordReset EmployeeService ");
		EmployeeMicroservices employee=employeeRepository.findByEmployeeId(userId);
		if(employee==null)throw new UserPrincipalNotFoundException("invalid id ");
		String mail=employee.getEmail();
		Long mobileNo=employee.getMobile();
		
		// 2. Generate a random OTP (e.g., 6-digit code)
	    String otp = generateRandomOTP();

	    // 3. Store the OTP in a database with a time limit (e.g., 5 minutes)
	    saveOTP(userId, otp);
	    
	    // 4. Send the OTP to the user's email or mobile number
	    
	    sendOTPViaEmailOrSMS(mail, otp);
		return employee;
	}
	
	private void sendOTPViaEmailOrSMS(String mail, String otp) throws MessagingException {
		MimeMessage message =javaMailSender.createMimeMessage();
		MimeMessageHelper helper= new MimeMessageHelper(message);
		helper.setTo(mail);
		helper.setSubject("Password Reset OTP ");
		helper.setText("Hello Your OTP for password regeneration "+otp);
		javaMailSender.send(message);
	}

	private void saveOTP(Long userId, String otp) {

		OTPRecord otpRecord =new OTPRecord(null, userId, otp,  LocalDateTime.now());
		otpRepo.save(otpRecord);
	}

	//OTP generation 
	 // 2. Generate a random OTP (e.g., 6-digit code)

    public static String generateRandomOTP() {
        // Define the range for the OTP (e.g., 100000 to 999999 for a 6-digit OTP)
        int min = 100000;
        int max = 999999;

        // Generate a random number within the specified range
        Random random = new Random();
        int otpValue = random.nextInt(max - min + 1) + min;

        // Format the OTP as a 6-digit string (e.g., "000123")
        String otp = String.format("%06d", otpValue);

        return otp;
    }

	public Object confirmPasswordReset(Long userId, String otp, String newPassword) throws Exception {
		
		log.info("********inside confirmPasswordReset Employee Service");
	    // 2. Verify the OTP
	    if (isValidOTP(userId, otp)) {
	        // 3. Reset the password for the user
	    	log.info("******OTP is Valid ");
	    	log.info("****fetching data using userId : "+userId+" otp : "+ otp);
	    	UserPrinciples user=userPrinciplesRepository.findByEmployeeId(userId).orElseThrow(()->new Exception("User Not Found"));
	    	user.setPassword(passwordEncoder.encode(newPassword) );
	    	log.info("*******after fetching UserDetails");
	        // 4. Delete the used OTP from the database
	    	OTPRecord otpRecord=otpRepo.findByUserIdAndOtp(userId, otp);
	       otpRepo.delete(otpRecord);
	        log.info("******after otp delete otp record");
	        userPrinciplesRepository.save(user);
	        return ResponseEntity.ok("Password reset successful.");
	    } else {
	    	log.info("*******OTP is not Valid");
	        return new ResponseEntity<Object>("Invalid OTP. Password reset failed.",HttpStatus.BAD_REQUEST);
	    }
	}

	private boolean isValidOTP(Long userId, String otp) throws Exception {
		// Load the OTP record from the database by userId and OTP
		log.info("******inside isValidOTP");
		OTPRecord otpRecordOptional = otpRepo.findByUserIdAndOtp(userId, otp);
		log.info("********after fetching otp record");
		if (otpRecordOptional!=null) {
			log.info("**** opt record is not null");
		    OTPRecord otpRecord = otpRecordOptional;

		    // Check if the OTP is still valid (within the time limit)
		    LocalDateTime currentTime = LocalDateTime.now();
		    LocalDateTime creationTime = otpRecord.getCreationTime();
		    Duration duration = Duration.between(creationTime, currentTime);
		    
		    if (duration.toMinutes() <= 5) {
		        // OTP is valid
		        // Proceed with password reset
		    	return true;
		    } else {
		        // OTP has expired
		        // Handle the case where OTP is no longer valid
		    	otpRepo.delete(otpRecordOptional);
		    	throw new Exception("OTP Has Expired");
		    }
		} else {
		    // Invalid OTP
		    // Handle the case where the OTP is not found in the database
			log.info("****otp record is null");
			throw new Exception("Invalid Otp");
		}

	}

	public ResponseEntity<?> getUser(Long id) throws Exception {
		log.info("*******inside getUser employeeService");
		EmployeeMicroservices employee=employeeRepository.findByEmployeeId(id);
		if(employee==null) {
			return ResponseEntity.badRequest().body("Employee id Not Found");
		}if(employee.getIsDeleted()) {
			return ResponseEntity.badRequest().body("Employee is InActive");
		}
		return  ResponseEntity.ok(userPrinciplesRepository.findById(id));
	}

	public EmployeeMicroservices GetById(HttpServletRequest request) {
		log.info("*********inside getById employeeService");
		return employeeRepository.findByEmployeeId(Long.valueOf(jwtService.extractEmployeeId(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7))));
	}

	public Object GetAllEmployes(HttpServletRequest request) {
		return employeeRepository.findAll();
	}

	
//	//Updating Employee
//	
//	public Object update(UserPrinciples principles) {
//		principles.setPassword(passwordEncoder.encode(principles.getPassword()));
//		return userPrinciplesRepository.save(principles);
//	}

}
