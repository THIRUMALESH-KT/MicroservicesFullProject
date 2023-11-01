package com.leave.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.leave.entity.EmployeeLeave;
import com.leave.entity.EmployeeLeaveSummary;

@Repository
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
	
	
//	@Query(value = "SELECT SUM((DATEDIFF(LEAST(to_date, :endDate), GREATEST(from_date, :startDate)))+1) AS leaveDays " +
//	        "FROM employee_leave " +
//	        "WHERE employee_id = :employeeId " +
//	        "AND leave_status = :leaveStatus " +
//	        "AND to_date >= :startDate " +
//	        "AND from_date <= :endDate", nativeQuery = true)
//	List<Long> findApprovedLeaveDaysInCurrentMonth(
//	        Long employeeId,
//	        String leaveStatus,
//	        LocalDate startDate,
//	        LocalDate endDate);



	    @Query(value = "SELECT SUM(" +
	            "CASE WHEN e.isHalfDayLeave = true THEN 0.5 ELSE DATEDIFF(LEAST(e.toDate, :endDate), GREATEST(e.fromDate, :startDate)) + 1 END" +
	            ") AS leaveDays " +
	            "FROM EmployeeLeave e " +
	            "WHERE e.employeeId = :employeeId " +
	            "AND e.leaveStatus = :leaveStatus " +
	            "AND e.toDate >= :startDate " +
	            "AND e.fromDate <= :endDate")
	    Float findApprovedLeaveDaysInCurrentMonth(
	         Long employeeId,
	         String leaveStatus,
	         LocalDate startDate,
	         LocalDate endDate);
	List<Object> findByEmployeeId(Long valueOf);

	List<Object> findByManagerIdAndLeaveStatus(Long managerId, String string);




//
//	@Query(value = "SELECT * " +
//	        "FROM employee_leave " +
//	        "WHERE employee_id = :employeeId " +
//	        "AND leave_status = :leaveStatus " +
//	        "AND to_date >= :startDate " +
//	        "AND from_date <= :endDate", nativeQuery = true)
//	List<EmployeeLeave> findCurrentMonthlyLeaves(Long employeeId, String leaveStatus, LocalDate startDate,
//			LocalDate endDate);
//	
	@Query(value = "SELECT * " +
	        "FROM employee_leave " +
	        "WHERE employee_id = :employeeId " +
	        "AND leave_status = :leaveStatus " +
	        "AND to_date >= :startDate " +
	        "AND from_date <= :endDate", nativeQuery = true)
	List<EmployeeLeave> findCurrentLeaves(Long employeeId, String leaveStatus, LocalDate startDate, LocalDate endDate);
	
	@Query(value = "SELECT * " +
	        "FROM employee_leave " +
	        "WHERE leave_status = :leaveStatus " +
	        "AND to_date >= :startDate " +
	        "AND from_date <= :endDate", nativeQuery = true)
	List<EmployeeLeave> findEmployeesLeaveData(String leaveStatus, LocalDate startDate,LocalDate endDate);
	
	@Query(value = "SELECT * " +
	        "FROM employee_leave " +
	        "WHERE to_date >= :startDate " +
	        "AND from_date <= :endDate", nativeQuery = true)
	List<EmployeeLeave> findAllEmployeesLeaveDataBasedOnLeaveStatusAndDate(LocalDate startDate, LocalDate endDate);

//	@Query(value =
//		    "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
//		    "FROM employee_leave e " +
//		    "WHERE e.employee_id = :employeeId " +
//		    "AND :fromDate BETWEEN from_date AND e.to_date "+
//		    "OR :toDate BETWEEN from_date AND e.to_date ",
//		    nativeQuery = true)
	
    @Query(value =
            "SELECT COUNT(*) FROM employee_leave " +
            "WHERE employee_id = :employeeId " +
            "AND (" +
            "    (:leavefromDate BETWEEN from_date AND to_date) " +
            "    OR (:leavetoDate BETWEEN from_date AND to_date) " +
            "    OR (from_date BETWEEN :leavefromDate AND :leavetoDate) " +
            "    OR (to_date BETWEEN :leavefromDate AND :leavetoDate) " +
            ")",
            nativeQuery = true)
    Long hasLeaveOnDate(Long employeeId, LocalDate leavefromDate, LocalDate leavetoDate);


}
