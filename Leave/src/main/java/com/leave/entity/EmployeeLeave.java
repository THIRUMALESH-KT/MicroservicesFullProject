package com.leave.entity;

import java.sql.Blob;
import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class EmployeeLeave {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long employeeId;
	private String code;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String reason;
	private Long appliedBy;
	private static String status="Pending";
	
	public static String getStatus() {
		return status;
	}
	public static void setStatus(String status) {
		EmployeeLeave.status = status;
	}
}
