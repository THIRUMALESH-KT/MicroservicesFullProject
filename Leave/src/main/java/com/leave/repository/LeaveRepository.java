package com.leave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leave.entity.EmployeeLeave;

public interface LeaveRepository extends JpaRepository<EmployeeLeave, Long> {

    @Query(value = "SELECT employeeId, SUM("
            + "CASE "
            + "WHEN fromDate < DATE_FORMAT(NOW() - INTERVAL 25 DAY, '%Y-%m-26') "
            + "THEN DATEDIFF(DATE_FORMAT(NOW() - INTERVAL 25 DAY, '%Y-%m-26'), LEAST(toDate, NOW())) "
            + "ELSE 0 END) AS leaveDays "
            + "FROM employee_leave "
            + "WHERE employeeId = :employeeId "
            + "AND leaveStatus = 'APPROVED' "
            + "AND ("
            + "    (fromDate >= DATE_FORMAT(NOW() - INTERVAL 25 DAY, '%Y-%m-26') "
            + "    AND fromDate <= DATE_FORMAT(NOW() - INTERVAL 1 DAY, '%Y-%m-25')) "
            + "    OR (toDate >= DATE_FORMAT(NOW() - INTERVAL 25 DAY, '%Y-%m-26') "
            + "    AND toDate <= DATE_FORMAT(NOW(), '%Y-%m-25')) "
            + "    OR (fromDate < DATE_FORMAT(NOW() - INTERVAL 25 DAY, '%Y-%m-26') "
            + "    AND toDate > DATE_FORMAT(NOW(), '%Y-%m-25')) "
            + ") "
            + "GROUP BY employeeId", nativeQuery = true)
    List<Object[]> findApprovedLeaveDaysInCurrentMonth(int employeeId);

	List<Object> findByEmployeeId(Long valueOf);

	List<Object> findByManagerIdAndLeaveStatus(Long managerId, String string);
}










