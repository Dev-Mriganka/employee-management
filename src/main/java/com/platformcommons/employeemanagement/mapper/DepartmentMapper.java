package com.platformcommons.employeemanagement.mapper;

import com.platformcommons.employeemanagement.dto.DepartmentDetailedResponse;
import com.platformcommons.employeemanagement.dto.DepartmentRequest;
import com.platformcommons.employeemanagement.dto.DepartmentResponse;
import com.platformcommons.employeemanagement.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Department toEntity(DepartmentRequest request);

    DepartmentResponse toResponse(Department entity);

    DepartmentDetailedResponse toDetailedResponse(Department entity);
}
