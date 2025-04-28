package com.platformcommons.employeemanagement.mapper;

import com.platformcommons.employeemanagement.dto.*;
import com.platformcommons.employeemanagement.entity.Address;
import com.platformcommons.employeemanagement.entity.Department;
import com.platformcommons.employeemanagement.entity.Employee;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {AddressMapper.class, DepartmentMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {
    @Mapping(target = "departments", ignore = true)
    Employee toEntity(EmployeeRequest request);

    @Mapping(target = "addresses", source = "addresses")
    @Mapping(target = "departments", source = "departments")
    EmployeeResponse toResponse(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "departments", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "employeeCode", ignore = true)
    void updateFromRequest(EmployeeUpdateRequest request, @MappingTarget Employee entity);

}