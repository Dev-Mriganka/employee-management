package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Address;
import com.platformcommons.employeemanagement.entity.Employee;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "Employee response data")
public record EmployeeResponse(
        @Schema(description = "Unique identifier of the employee", example = "1")
        Long id,

        @Schema(description = "Full name of employee", example = "Mriganka Mondal")
        String name,

        @Schema(description = "Date of birth (YYYY-MM-DD)", example = "2000-10-18")
        LocalDate dateOfBirth,

        @Schema(description = "Gender of employee", example = "MALE")
        Employee.Gender gender,

        @Schema(description = "Unique employee code", example = "EMP001")
        String employeeCode,

        @Schema(description = "Email address", example = "mrigankamondal10@gmail.com")
        String email,

        @Schema(description = "Mobile number", example = "7596943998")
        String mobileNumber,

        @Schema(description = "Emergency contact number", example = "6291888393")
        String emergencyContact,

        @Schema(description = "List of employee addresses")
        Set<AddressResponse> addresses,

        @Schema(description = "List of assigned departments")
        Set<DepartmentResponse> departments
) { }