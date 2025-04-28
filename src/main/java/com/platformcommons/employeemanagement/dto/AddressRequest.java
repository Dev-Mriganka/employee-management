package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Address creation request")
public record AddressRequest(
        @Schema(description = "Type of address", example = "PERMANENT", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Address type must be specified (PERMANENT, RESIDENTIAL, or CORRESPONDENCE)")
        Address.AddressType addressType,

        @Schema(description = "Street address", example = "91 Lawrence St", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Street address cannot be empty")
        String street,

        @Schema(description = "City", example = "Kolkata", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "City cannot be empty")
        String city,

        @Schema(description = "State/Province", example = "WB", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "State cannot be empty")
        String state,

        @Schema(description = "Country", example = "India", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Country cannot be empty")
        String country,

        @Schema(description = "Postal/ZIP code", example = "712258", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Postal code cannot be empty")
        String postalCode
) {}