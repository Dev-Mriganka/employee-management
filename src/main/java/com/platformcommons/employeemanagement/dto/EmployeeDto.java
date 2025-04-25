package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class EmployeeDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Name is required")
        private String name;

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;

        @NotNull(message = "Gender is required")
        private Employee.Gender gender;

        @NotBlank(message = "Employee code is required")
        @Pattern(regexp = "^[A-Z0-9]{6,10}$", message = "Employee code must be 6-10 alphanumeric characters")
        private String employeeCode;

        @Email(message = "Invalid email format")
        private String email;

        @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
        private String mobileNumber;

        @Pattern(regexp = "^[0-9]{10}$", message = "Emergency contact must be 10 digits")
        private String emergencyContact;

        @Valid
        private Set<AddressDto.Request> addresses = new HashSet<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private LocalDate dateOfBirth;
        private Employee.Gender gender;
        private String employeeCode;
        private String email;
        private String mobileNumber;
        private String emergencyContact;
        private Set<AddressDto.Response> addresses = new HashSet<>();
        private Set<DepartmentDto.Response> departments = new HashSet<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @Email(message = "Invalid email format")
        private String email;

        @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
        private String mobileNumber;

        @Pattern(regexp = "^[0-9]{10}$", message = "Emergency contact must be 10 digits")
        private String emergencyContact;

        @Valid
        private Set<AddressDto.Request> addresses = new HashSet<>();
    }
}