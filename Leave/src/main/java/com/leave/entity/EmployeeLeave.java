package com.leave.entity;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLeave {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long employeeId;
	private String leaveCode;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String reason;
	private Long appliedBy;
	@Lob
	private Blob file;
	private  String leaveStatus;
	private Long managerId;
    private Boolean isHalfDayLeave; 
	public EmployeeLeave(EmployeeLeave original) {
		 	this.id=original.getId();
	        this.employeeId = original.getEmployeeId();
	        this.leaveCode = original.getLeaveCode();
	        this.fromDate = original.getFromDate();
	        this.toDate = original.getToDate();
	        this.reason = original.getReason();
	        this.appliedBy = original.getAppliedBy();
	        this.file = original.getFile();
	        this.leaveStatus = original.getLeaveStatus();
	        this.managerId = original.getManagerId();
	        this.isHalfDayLeave=original.isHalfDayLeave;
	    }
	public void setLeaveFile(MultipartFile file) throws SerialException, SQLException, IOException {
		
		this.file=new SerialBlob(file.getBytes());
	}


	
}
