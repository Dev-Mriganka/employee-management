package com.platformcommons.employeemanagement.service;

import com.platformcommons.employeemanagement.dto.DepartmentDto;
import com.platformcommons.employeemanagement.entity.Department;
import com.platformcommons.employeemanagement.exception.ResourceNotFoundException;
import com.platformcommons.employeemanagement.mapper.DepartmentMapper;
import com.platformcommons.employeemanagement.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public List<DepartmentDto.Response> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public DepartmentDto.Response getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return departmentMapper.toDto(department);
    }

    public DepartmentDto.DetailedResponse getDepartmentWithEmployees(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return departmentMapper.toDetailedDto(department);
    }

    @Transactional
    public DepartmentDto.Response createDepartment(DepartmentDto.Request departmentDto) {
        // Check if department name already exists
        if (departmentRepository.existsByDepartmentName(departmentDto.getDepartmentName())) {
            throw new IllegalArgumentException("Department name already exists: " + departmentDto.getDepartmentName());
        }

        Department department = departmentMapper.toEntity(departmentDto);
        return departmentMapper.toDto(departmentRepository.save(department));
    }

    @Transactional
    public DepartmentDto.Response updateDepartment(Long id, DepartmentDto.Request departmentDto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        // Check if department name already exists for another department
        if (!department.getDepartmentName().equals(departmentDto.getDepartmentName()) &&
                departmentRepository.existsByDepartmentName(departmentDto.getDepartmentName())) {
            throw new IllegalArgumentException("Department name already exists: " + departmentDto.getDepartmentName());
        }

        department.setDepartmentName(departmentDto.getDepartmentName());
        department.setDescription(departmentDto.getDescription());
        department.setDepartmentType(departmentDto.getDepartmentType());
        department.setResponsibilities(departmentDto.getResponsibilities());

        return departmentMapper.toDto(departmentRepository.save(department));
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}