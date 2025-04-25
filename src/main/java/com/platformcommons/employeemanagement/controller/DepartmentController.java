package com.platformcommons.employeemanagement.controller;

import com.platformcommons.employeemanagement.dto.DepartmentDto;
import com.platformcommons.employeemanagement.service.DepartmentService;
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
@RequestMapping("/department")
@RequiredArgsConstructor
@Tag(name = "Department", description = "Department Management API")
@SecurityRequirement(name = "bearerAuth")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "Get all departments", description = "Retrieves all departments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<List<DepartmentDto.Response>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID", description = "Retrieves department by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<DepartmentDto.Response> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping("/{id}/employees")
    @Operation(summary = "Get department with employees", description = "Retrieves department with all assigned employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentDto.DetailedResponse> getDepartmentWithEmployees(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentWithEmployees(id));
    }

    @PostMapping
    @Operation(summary = "Create department", description = "Creates a new department")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentDto.Response> createDepartment(@Valid @RequestBody DepartmentDto.Request departmentDto) {
        return new ResponseEntity<>(departmentService.createDepartment(departmentDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department", description = "Updates an existing department")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentDto.Response> updateDepartment(@PathVariable Long id,
                                                                   @Valid @RequestBody DepartmentDto.Request departmentDto) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department", description = "Deletes a department")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}