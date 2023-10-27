package com.leave.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLeaveSummary {
	private EmployeeLeave employeeLeave; // You can define this class to match your entity structure.
    private int leaveDays;
}
