package com.platformcommons.employeemanagement.mapper;

import com.platformcommons.employeemanagement.dto.AddressRequest;
import com.platformcommons.employeemanagement.dto.AddressResponse;
import com.platformcommons.employeemanagement.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "employee", ignore = true)
    Address toEntity(AddressRequest dto);

    AddressResponse toResponse(Address entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    void updateFromRequest(AddressRequest request, @MappingTarget Address entity);
}