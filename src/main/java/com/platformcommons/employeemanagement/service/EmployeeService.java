package com.platformcommons.employeemanagement.service;

import com.platformcommons.employeemanagement.dto.AddressRequest;
import com.platformcommons.employeemanagement.dto.EmployeeRequest;
import com.platformcommons.employeemanagement.dto.EmployeeResponse;
import com.platformcommons.employeemanagement.dto.EmployeeUpdateRequest;
import com.platformcommons.employeemanagement.entity.Address;
import com.platformcommons.employeemanagement.entity.Department;
import com.platformcommons.employeemanagement.entity.Employee;
import com.platformcommons.employeemanagement.exception.ResourceNotFoundException;
import com.platformcommons.employeemanagement.mapper.AddressMapper;
import com.platformcommons.employeemanagement.mapper.EmployeeMapper;
import com.platformcommons.employeemanagement.repository.DepartmentRepository;
import com.platformcommons.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;
    private final AddressMapper addressMapper;

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = getEmployeeOrThrow(id);
        return employeeMapper.toResponse(employee);
    }

    public EmployeeResponse getEmployeeByCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        return employeeMapper.toResponse(employee);
    }

    public List<EmployeeResponse> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name).stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<EmployeeResponse> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        // Check if employee code already exists
        if (employeeRepository.existsByEmployeeCode(employeeRequest.employeeCode())) {
            throw new IllegalArgumentException("Employee code already exists: " + employeeRequest.employeeCode());
        }

        Employee employee = employeeMapper.toEntity(employeeRequest);

        // Handle addresses
        if (employeeRequest.addresses() == null || employeeRequest.addresses().isEmpty()) {
            employee.setAddresses(new HashSet<>());
        } else {
            employee.setAddresses(employeeRequest.addresses().stream()
                    .map(addressMapper::toEntity)
                    .peek(employee::addAddress)
                    .collect(Collectors.toSet()));
        }

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest updateRequest) {
        Employee employee = getEmployeeOrThrow(id);

        employeeMapper.updateFromRequest(updateRequest, employee);
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse addAddress(Long employeeId, AddressRequest request) {
        Employee employee = getEmployeeOrThrow(employeeId);
        Address address = addressMapper.toEntity(request);
        employee.addAddress(address); // Uses your bidirectional helper
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse updateAddress(Long employeeId, Long addressId, AddressRequest request) {
        Employee employee = getEmployeeOrThrow(employeeId);
        Address address = employee.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        addressMapper.updateFromRequest(request, address);
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse removeAddress(Long employeeId, Long addressId) {
        Employee employee = getEmployeeOrThrow(employeeId);
        Address address = employee.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        employee.removeAddress(address); // Uses your bidirectional helper
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    private Employee getEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    public EmployeeResponse assignDepartment(Long employeeId, Long departmentId) {
        Employee employee = getEmployeeOrThrow(employeeId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));

        employee.addDepartment(department);

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse removeDepartment(Long employeeId, Long departmentId) {
        Employee employee = getEmployeeOrThrow(employeeId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));

        employee.removeDepartment(department);

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    public boolean validateEmployeeCredentials(String employeeCode, LocalDate dateOfBirth) {
        return employeeRepository.findByEmployeeCodeAndDateOfBirth(employeeCode, dateOfBirth).isPresent();
    }
}