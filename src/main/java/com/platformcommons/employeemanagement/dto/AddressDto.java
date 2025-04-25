package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AddressDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "Address type is required")
        private Address.AddressType addressType;

        @NotBlank(message = "Street address is required")
        private String streetAddress;

        @NotBlank(message = "City is required")
        private String city;

        @NotBlank(message = "State is required")
        private String state;

        @NotBlank(message = "Country is required")
        private String country;

        @NotBlank(message = "Postal code is required")
        private String postalCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Address.AddressType addressType;
        private String streetAddress;
        private String city;
        private String state;
        private String country;
        private String postalCode;
    }
}