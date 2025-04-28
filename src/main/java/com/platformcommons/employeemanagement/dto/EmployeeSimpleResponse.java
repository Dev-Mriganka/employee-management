package com.platformcommons.employeemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Simplified employee information")
public record EmployeeSimpleResponse(
        @Schema(description = "Employee ID", example = "1")
        Long id,

        @Schema(description = "Full name of employee", example = "Mriganka Mondal")
        String name,

        @Schema(description = "Unique employee code", example = "EMP001")
        String employeeCode
) {}