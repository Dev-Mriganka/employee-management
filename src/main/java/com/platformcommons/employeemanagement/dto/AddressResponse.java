package com.platformcommons.employeemanagement.dto;

import com.platformcommons.employeemanagement.entity.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Address response data")
public record AddressResponse(
        @Schema(description = "Unique identifier of the address", example = "1")
        Long id,

        @Schema(description = "Type of address", example = "PERMANENT")
        Address.AddressType addressType,

        @Schema(description = "Street address", example = "91 Lawrence St")
        String street,

        @Schema(description = "City", example = "Kolkata")
        String city,

        @Schema(description = "State/Province", example = "WB")
        String state,

        @Schema(description = "Country", example = "India")
        String country,

        @Schema(description = "Postal/ZIP code", example = "712258")
        String postalCode
) {}