package com.platformcommons.employeemanagement.mapper;

import com.platformcommons.employeemanagement.dto.EmployeeDto;
import com.platformcommons.employeemanagement.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {AddressMapper.class, DepartmentMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    Employee toEntity(EmployeeDto.Request dto);

    @Mapping(target = "departments", source = "departments")
    EmployeeDto.Response toDto(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "employeeCode", ignore = true)
    @Mapping(target = "departments", ignore = true)
    void updateEmployeeFromDto(EmployeeDto.UpdateRequest dto, @MappingTarget Employee entity);
}