package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Department;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(
        description = "Detailed department response including employee information",
        name = "DepartmentDetailedResponse"
)
public record DepartmentDetailedResponse(
        @Schema(
                description = "Unique identifier of the department",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long id,

        @Schema(
                description = "Name of the department",
                example = "Engineering",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String departmentName,

        @Schema(
                description = "Detailed description of the department",
                example = "Software development team",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String description,

        @Schema(
                description = "Type of department",
                example = "TECHNICAL",
                allowableValues = {"TECHNICAL", "HR", "FINANCE", "OPERATIONS"},
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Department.DepartmentType departmentType,

        @Schema(
                description = "Key responsibilities and projects of the department",
                example = "Product development and maintenance",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String responsibilities,

        @ArraySchema(
                arraySchema = @Schema(
                        description = "List of employees assigned to this department",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED
                ),
                schema = @Schema(implementation = EmployeeSimpleResponse.class)
        )
        Set<EmployeeSimpleResponse> employees
) { }

