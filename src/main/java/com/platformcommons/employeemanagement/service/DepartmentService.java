package com.platformcommons.employeemanagement.service;

import com.platformcommons.employeemanagement.dto.DepartmentDetailedResponse;
import com.platformcommons.employeemanagement.dto.DepartmentRequest;
import com.platformcommons.employeemanagement.dto.DepartmentResponse;
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

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return departmentMapper.toResponse(department);
    }

    public DepartmentDetailedResponse getDepartmentWithEmployees(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return departmentMapper.toDetailedResponse(department);
    }

    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByDepartmentName(request.departmentName())) {
            throw new IllegalArgumentException("Department name already exists: " + request.departmentName());
        }

        Department department = departmentMapper.toEntity(request);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest departmentRequest) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (!department.getDepartmentName().equals(departmentRequest.departmentName()) &&
                departmentRepository.existsByDepartmentName(departmentRequest.departmentName())) {
            throw new IllegalArgumentException("Department name already exists: " + departmentRequest.departmentName());
        }

        department.setDepartmentName(departmentRequest.departmentName());
        department.setDescription(departmentRequest.description());
        department.setDepartmentType(departmentRequest.departmentType());
        department.setResponsibilities(departmentRequest.responsibilities());

        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}