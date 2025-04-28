package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Department;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "Department response data")
public record DepartmentResponse(
        @Schema(description = "Unique identifier of the department", example = "1")
        Long id,

        @Schema(description = "Name of the department", example = "Engineering")
        String departmentName,

        @Schema(description = "Description of the department", example = "Software development team")
        String description,

        @Schema(
                description = "Type of department", example = "TECHNICAL",
                allowableValues = {"TECHNICAL", "HR", "FINANCE", "MARKETING", "OPERATIONS", "CUSTOMER_SUPPORT"}
        )
        String departmentType,

        @Schema(description = "Key projects/responsibilities", example = "Product development")
        String responsibilities
) { }