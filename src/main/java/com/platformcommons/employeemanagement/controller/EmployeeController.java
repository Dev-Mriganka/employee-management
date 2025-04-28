package com.platformcommons.employeemanagement.controller;

import com.platformcommons.employeemanagement.dto.AddressRequest;
import com.platformcommons.employeemanagement.dto.EmployeeRequest;
import com.platformcommons.employeemanagement.dto.EmployeeResponse;
import com.platformcommons.employeemanagement.dto.EmployeeUpdateRequest;
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
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves employee by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/code/{employeeCode}")
    @Operation(summary = "Get employee by code", description = "Retrieves employee by employee code")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> getEmployeeByCode(@PathVariable String employeeCode) {
        return ResponseEntity.ok(employeeService.getEmployeeByCode(employeeCode));
    }

    @GetMapping("/search")
    @Operation(summary = "Search employees by name", description = "Searches for employees by name (case insensitive)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> searchEmployeesByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchEmployeesByName(name));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get employees by department", description = "Retrieves all employees assigned to a specific department")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }

    @PostMapping
    @Operation(summary = "Create employee", description = "Creates a new employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        return new ResponseEntity<>(employeeService.createEmployee(employeeRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee", description = "Updates an existing employee")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id,
                                                           @Valid @RequestBody EmployeeUpdateRequest employeeUpdateRequest) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeUpdateRequest));
    }

    @PostMapping("/{employeeId}/address")
    @Operation(summary = "Add address to employee", description = "Adds an address to an employee")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> addAddress(
            @PathVariable Long employeeId,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(
                employeeService.addAddress(employeeId, request)
        );
    }

    @PutMapping("/{employeeId}/address/{addressId}")
    @Operation(summary = "Update address of employee", description = "Updates an address of an employee")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> updateAddress(
            @PathVariable Long employeeId,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(
                employeeService.updateAddress(employeeId, addressId, request)
        );
    }

    @DeleteMapping("/{employeeId}/address/{addressId}")
    @Operation(summary = "Remove address from employee", description = "Removes an address from an employee")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> removeAddress(
            @PathVariable Long employeeId,
            @PathVariable Long addressId) {
        return ResponseEntity.ok(
                employeeService.removeAddress(employeeId, addressId)
        );
    }

    @PutMapping("/{employeeId}/department/{departmentId}")
    @Operation(summary = "Assign department to employee", description = "Assigns a department to an employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> assignDepartment(@PathVariable Long employeeId,
                                                                 @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.assignDepartment(employeeId, departmentId));
    }

    @DeleteMapping("/{employeeId}/department/{departmentId}")
    @Operation(summary = "Remove department from employee", description = "Removes a department from an employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> removeDepartment(@PathVariable Long employeeId,
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