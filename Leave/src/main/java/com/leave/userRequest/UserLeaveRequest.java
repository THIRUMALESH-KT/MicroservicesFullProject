package com.leave.userRequest;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLeaveRequest {

	private String LeaveCode;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String reason;
    private Boolean isHalfDayLeave; // Add this field

}
