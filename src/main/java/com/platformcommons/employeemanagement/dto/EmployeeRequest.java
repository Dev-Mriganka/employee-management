package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Employee creation request")
public record EmployeeRequest(
        @Schema(description = "Full name of employee", example = "Mriganka Mondal", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Date of birth (YYYY-MM-DD)", example = "2000-10-18", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @Schema(description = "Gender of employee", example = "MALE", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Gender is required")
        Employee.Gender gender,

        @Schema(description = "Unique employee code", example = "EMP001", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Employee code is required")
        @Pattern(regexp = "^[A-Z0-9]{6,10}$", message = "Employee code must be 6-10 uppercase alphanumeric characters")
        String employeeCode,

        @Schema(description = "Email address", example = "mrigankamondal10@gmail.com")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Mobile number", example = "7596943998")
        @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
        String mobileNumber,

        @Schema(description = "Emergency contact number", example = "6291888393")
        @Pattern(regexp = "^[0-9]{10}$", message = "Emergency contact must be 10 digits")
        String emergencyContact,

        @Schema(description = "List of employee addresses")
        @Valid
        Set<AddressRequest> addresses
) {}