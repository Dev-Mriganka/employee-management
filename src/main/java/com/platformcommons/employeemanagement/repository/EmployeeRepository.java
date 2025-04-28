package com.platformcommons.employeemanagement.repository;

import com.platformcommons.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByNameContainingIgnoreCase(String name);

    Optional<Employee> findByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCode(String employeeCode);

    @Query("SELECT e FROM Employee e JOIN e.departments d WHERE d.id = :departmentId")
    List<Employee> findByDepartmentId(@Param("departmentId") Long departmentId);

    Optional<Employee> findByEmployeeCodeAndDateOfBirth(String employeeCode, LocalDate dateOfBirth);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);
}