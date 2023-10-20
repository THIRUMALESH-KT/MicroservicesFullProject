package com.employe.service;

import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.naming.TimeLimitExceededException;

import org.bouncycastle.asn1.x509.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.employe.dto.UserDetails;
import com.employe.entity.EmployeeMicroservices;
import com.employe.entity.OTPRecord;
import com.employe.entity.UserPrinciples;
import com.employe.repository.EmployeeRepository;
import com.employe.repository.OTPRecordRepository;
import com.employe.repository.UserPrinciplesRepository;
import com.employe.userRequest.UserLeaveRequest;
import com.employe.userRequest.employeeUserRequest;
import com.thoughtworks.xstream.converters.time.LocalDateConverter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
	private UserPrinciplesRepository userPrinciplesRepository;
	

	
	@Autowired
	private OTPRecordRepository otpRepo;
	
	@Autowired
	private JavaMailSender javaMailSender;

	
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
				 employe.getName(), employe.getMobile(), employe.getDesignation(),
				 employe.getEmail(), employe.getStartDate(), null, employe.getSkill(),
				employe.getManagerId());
		UserPrinciples userPrinciples =new UserPrinciples(employe.getEmployeeId(), employe.getPassword(), employe.getAccesCode());
		if ((!employe.getDesignation().equalsIgnoreCase("MANAGER")) && (!employe.getDesignation().equalsIgnoreCase("HR"))) {
			log.info("*********before calling manager getById endPoint");
			log.info("********* Designation"+employe.getDesignation());
			log.info("********** manager Id "+employe.getManagerId());
			ResponseEntity<Object> ob = restTemplate.exchange(managerBaseUrl + "/getById/" + employe.getManagerId(),
					HttpMethod.GET, null, Object.class);
			log.info("*********after calling manager getById endPoint");

			if (ob.getBody() != null) {
				employeeRepository.save(employeeMicroservices);
				userPrinciplesRepository.save(userPrinciples);

				 restTemplate.exchange(managerBaseUrl+"/addEmployeeId", HttpMethod.PUT,new
				 HttpEntity<employeeUserRequest>(employe), Void.class);
				return "Employee Saved ";
			} else {
				log.error("********giver Manager id not found");
				throw new Exception("Manager Id not Found");
				// return new ResponseEntity<Object>("Manager id not Found
				// ",HttpStatus.BAD_REQUEST);
			}

		} else if(employe.getDesignation().equalsIgnoreCase("MANAGER")) {
			employeeMicroservices.setManagerId(employe.getEmployeeId());
			restTemplate.exchange(managerBaseUrl+"/insert", HttpMethod.POST,new HttpEntity<employeeUserRequest>(employe), Void.class);
			employeeRepository.save(employeeMicroservices);
			userPrinciplesRepository.save(userPrinciples);

			return "Employee Saved ";
		}else if(employe.getDesignation().equalsIgnoreCase("HR")) {
			employeeMicroservices.setManagerId(employe.getEmployeeId());
			employeeRepository.save(employeeMicroservices);
			userPrinciplesRepository.save(userPrinciples);
			return "Employee saved sucessfully";
		}
		return "Employee Saved Sucessfully";
	}

	public Object GetAllEmployes() throws Exception {

		return employeeRepository.findAll();
	}

	public EmployeeMicroservices getHr() throws Exception {
		log.info("************inside getHr EmployeeServicee");
		EmployeeMicroservices hr=employeeRepository.findByDesignation("HR");
		if(hr==null)throw new Exception("Hr not Found");
		log.info("************after fetching hr details");
		return hr;
	}
	


	public Object initiatePasswordReset(Long userId) throws UserPrincipalNotFoundException, MessagingException {
		log.info("*****inside PasswordReset EmployeeService ");
		EmployeeMicroservices employee=employeeRepository.findByEmployeeId(userId);
		if(employee==null)throw new UserPrincipalNotFoundException("invalid id ");
		String mail=employee.getEmail();
		String mobileNo=employee.getMobile();
		
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
		helper.setText("Hello Your OTP for password regeneration"+otp);
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

	public Object confirmPasswordReset(Long userId, String otp, String newPassword) throws NotFoundException, TimeLimitExceededException {

	    // 2. Verify the OTP
	    if (isValidOTP(userId, otp)) {
	        // 3. Reset the password for the user
	    	UserPrinciples user=userPrinciplesRepository.findById(userId).orElseThrow(()->new NotFoundException());
	    	user.setPassword(newPassword);

	        // 4. Delete the used OTP from the database
	        otpRepo.deleteByIdAndOtp(userId,otp);

	        return ResponseEntity.ok("Password reset successful.");
	    } else {
	        return new ResponseEntity<Object>("Invalid OTP. Password reset failed.",HttpStatus.BAD_REQUEST);
	    }
	}

	private boolean isValidOTP(Long userId, String otp) throws TimeLimitExceededException {
		// Load the OTP record from the database by userId and OTP
		Optional<OTPRecord> otpRecordOptional = otpRepo.findByUserIdAndOtp(userId, otp);

		if (otpRecordOptional.isPresent()) {
		    OTPRecord otpRecord = otpRecordOptional.get();

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
		    	throw new TimeLimitExceededException();
		    }
		} else {
		    // Invalid OTP
		    // Handle the case where the OTP is not found in the database
			throw new IllegalArgumentException("Invalid Otp");
		}

	}

	public Object getUser(Long id) throws UserPrincipalNotFoundException {
		return userPrinciplesRepository.findById(id).orElseThrow(()->new UserPrincipalNotFoundException("Invalid Login Id"));
	}

}
