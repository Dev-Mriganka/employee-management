package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

public class DepartmentDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Department name is required")
        private String departmentName;

        private String description;

        @NotNull(message = "Department type is required")
        private Department.DepartmentType departmentType;

        private String responsibilities;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String departmentName;
        private String description;
        private Department.DepartmentType departmentType;
        private String responsibilities;
        private int numberOfEmployees;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailedResponse {
        private Long id;
        private String departmentName;
        private String description;
        private Department.DepartmentType departmentType;
        private String responsibilities;
        private Set<EmployeeDto.Response> employees = new HashSet<>();
    }
}