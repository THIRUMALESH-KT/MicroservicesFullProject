package com.leave.userRequest;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.leave.helper.ValidFutureLocalDate;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLeaveRequest {

	@NotEmpty(message="LeaveCode Must Not Empty")
	private String LeaveCode;
	@ValidFutureLocalDate(message = "Must Not null and Leave Date Must not be before Current Date")
	//@NotEmpty(message="Enter Leave Date")
	private LocalDate fromDate;
	@ValidFutureLocalDate
	private LocalDate toDate;
	@NotEmpty(message = "Reason Must Not Empty")
	private String reason;
    private Boolean isHalfDayLeave; // Add this field

}
