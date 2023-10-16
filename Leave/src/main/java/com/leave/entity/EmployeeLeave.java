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
	private String LeaveCode;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String reason;
	private Long appliedBy;
	@Lob
	private Blob file;
	private  String leaveStatus="Pending";
	
	public   String getStatus() {
		return leaveStatus;
	}
	public  void setStatus(String status) {
		this.leaveStatus = status;
	}
	
	public void setLeaveFile(MultipartFile file) throws SerialException, SQLException, IOException {
		
		this.file=new SerialBlob(file.getBytes());
	}
}
