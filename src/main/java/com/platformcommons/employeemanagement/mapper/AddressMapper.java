package com.platformcommons.employeemanagement.mapper;

import com.platformcommons.employeemanagement.dto.AddressDto;
import com.platformcommons.employeemanagement.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "employee", ignore = true)
    Address toEntity(AddressDto.Request dto);

    AddressDto.Response toDto(Address entity);
}