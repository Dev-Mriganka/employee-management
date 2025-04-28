package com.platformcommons.employeemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record EmployeeUpdateRequest(
        @Schema(description = "Email address", example = "new.email@gmail.com")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Mobile number", example = "7596943998")
        @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
        String mobileNumber,

        @Schema(description = "Emergency contact", example = "6291888393")
        @Pattern(regexp = "^\\d{10}$", message = "Emergency contact must be 10 digits")
        String emergencyContact
) {}
