package com.leave.userRequest;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserLeaveRequest {

	private String LeaveCode;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String reason;
    private boolean isHalfDay; // Add this field

	private MultipartFile file;
}
