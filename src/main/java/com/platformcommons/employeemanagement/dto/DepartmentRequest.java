package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Department creation/update request")
public record DepartmentRequest(
        @Schema(
                description = "Name of the department",
                example = "Engineering",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Department name cannot be blank")
        @Size(max = 100, message = "Department name cannot exceed 100 characters")
        String departmentName,

        @Schema(
                description = "Description of the department",
                example = "Software development team",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Department description cannot be blank")
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @Schema(
                description = "Type of department",
                example = "TECHNICAL",
                allowableValues = {"TECHNICAL", "HR", "FINANCE"},
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Department type is required")
        Department.DepartmentType departmentType,

        @Schema(
                description = "Key projects/responsibilities",
                example = "Product development, Code reviews",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        @Size(max = 1000, message = "Responsibilities cannot exceed 1000 characters")
        String responsibilities
) {}