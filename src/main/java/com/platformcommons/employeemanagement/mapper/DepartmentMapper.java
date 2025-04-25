package com.platformcommons.employeemanagement.mapper;

import com.platformcommons.employeemanagement.dto.DepartmentDto;
import com.platformcommons.employeemanagement.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "employees", ignore = true)
    Department toEntity(DepartmentDto.Request dto);

    @Mapping(target = "numberOfEmployees", expression = "java(entity.getEmployees().size())")
    DepartmentDto.Response toDto(Department entity);

    @Mapping(target = "employees", source = "employees")
    DepartmentDto.DetailedResponse toDetailedDto(Department entity);
}
