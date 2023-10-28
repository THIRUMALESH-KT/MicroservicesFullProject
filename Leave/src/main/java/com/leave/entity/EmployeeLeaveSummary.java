package com.leave.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class EmployeeLeaveSummary {
    private Long id;
    private Long employeeId;
    private String leaveCode;
    private Long totalDays;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
    private Long appliedBy;
    private String leaveStatus;
    private Long managerId;
    private int leaveDays;

    public EmployeeLeaveSummary(Long id, Long employeeId, String leaveCode, Long totalDays, LocalDate fromDate, LocalDate toDate, String reason, Long appliedBy, String leaveStatus, Long managerId, int leaveDays) {
        this.id = id;
        this.employeeId = employeeId;
        this.leaveCode = leaveCode;
        this.totalDays = totalDays;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.appliedBy = appliedBy;
        this.leaveStatus = leaveStatus;
        this.managerId = managerId;
        this.leaveDays = leaveDays;
    }
}

