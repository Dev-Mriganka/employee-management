package com.platformcommons.employeemanagement.controller;

import com.platformcommons.employeemanagement.dto.EmployeeDto;
import com.platformcommons.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "Employee Management API")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves all employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDto.Response>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves employee by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeDto.Response> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/code/{employeeCode}")
    @Operation(summary = "Get employee by code", description = "Retrieves employee by employee code")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeDto.Response> getEmployeeByCode(@PathVariable String employeeCode) {
        return ResponseEntity.ok(employeeService.getEmployeeByCode(employeeCode));
    }

    @GetMapping("/search")
    @Operation(summary = "Search employees by name", description = "Searches for employees by name (case insensitive)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDto.Response>> searchEmployeesByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchEmployeesByName(name));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get employees by department", description = "Retrieves all employees assigned to a specific department")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<List<EmployeeDto.Response>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }

    @PostMapping
    @Operation(summary = "Create employee", description = "Creates a new employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto.Response> createEmployee(@Valid @RequestBody EmployeeDto.Request employeeDto) {
        return new ResponseEntity<>(employeeService.createEmployee(employeeDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee", description = "Updates an existing employee")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeDto.Response> updateEmployee(@PathVariable Long id,
                                                               @Valid @RequestBody EmployeeDto.UpdateRequest updateDto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, updateDto));
    }

    @PutMapping("/{employeeId}/department/{departmentId}")
    @Operation(summary = "Assign department to employee", description = "Assigns a department to an employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto.Response> assignDepartment(@PathVariable Long employeeId,
                                                                 @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.assignDepartment(employeeId, departmentId));
    }

    @DeleteMapping("/{employeeId}/department/{departmentId}")
    @Operation(summary = "Remove department from employee", description = "Removes a department from an employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto.Response> removeDepartment(@PathVariable Long employeeId,
                                                                 @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.removeDepartment(employeeId, departmentId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee", description = "Deletes an employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}