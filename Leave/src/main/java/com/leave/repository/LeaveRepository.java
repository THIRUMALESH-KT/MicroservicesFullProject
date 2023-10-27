package com.leave.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leave.entity.EmployeeLeave;
import com.leave.entity.EmployeeLeaveSummary;

public interface LeaveRepository extends JpaRepository<EmployeeLeave, Long> {

//	@Query(value = "SELECT employee_id, SUM("
//            + "CASE "
//            + "WHEN from_date < DATE_FORMAT(DATE_ADD(LAST_DAY(NOW() - INTERVAL 1 MONTH), INTERVAL 1 DAY), '%Y-%m-26') "
//            + "THEN DATEDIFF(LEAST(to_date, DATE_ADD(LAST_DAY(NOW()), INTERVAL 1 DAY)), DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-26'))"
//            + "ELSE 0 END) AS leaveDays "
//            + "FROM employee_leave "
//            + "WHERE employee_id = :employeeId "
//            + "AND leave_status = 'APPROVED' "
//            + "AND ("
//            + "    (from_date >= DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-26') "
//            + "    AND from_date <= DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-25')) "
//            + "    OR (to_date >= DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-26') "
//            + "    AND to_date <= DATE_FORMAT(LAST_DAY(NOW()), '%Y-%m-25')) "
//            + "    OR (from_date < DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-26') "
//            + "    AND to_date > DATE_FORMAT(LAST_DAY(NOW()), '%Y-%m-25')) "
//            + ") "
//            + "GROUP BY employee_id", nativeQuery = true)
	
	
//	@Query(value = "SELECT employee_id, SUM((DATEDIFF(LEAST(to_date, :endDate), GREATEST(from_date, :startDate)))+1) AS leaveDays "
//			+ "FROM employee_leave e " + // Added space here
//			"WHERE employee_id = :employeeId " + "AND leave_status = :leaveStatus " + "AND to_date >= :startDate "
//			+ "AND from_date <= :endDate " + "GROUP BY employee_id", nativeQuery = true)
//	Object findApprovedLeaveDaysInCurrentMonth(Long employeeId,String leaveStatus, LocalDate startDate, LocalDate endDate);
	
	@Query(value = "SELECT employee_leave, SUM((DATEDIFF(LEAST(e.to_date, :endDate), GREATEST(e.from_date, :startDate))+1)) AS leaveDays " +
	        "FROM employee_leave e " +
	        "WHERE e.employee_id = :employeeId " +
	        "AND e.leave_status = :leaveStatus " +
	        "AND e.to_date >= :startDate " +
	        "AND e.from_date <= :endDate " +
	        "GROUP BY e.employee_id", nativeQuery = true)
	List<EmployeeLeaveSummary> findApprovedLeaveDaysInCurrentMonth(
	        Long employeeId,
	        String leaveStatus,
	        LocalDate startDate,
	        LocalDate endDate);


	List<Object> findByEmployeeId(Long valueOf);

	List<Object> findByManagerIdAndLeaveStatus(Long managerId, String string);

}
