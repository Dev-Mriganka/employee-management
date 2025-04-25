package com.platformcommons.employeemanagement.service;

import com.platformcommons.employeemanagement.dto.EmployeeDto;
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

    public List<EmployeeDto.Response> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto.Response getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toDto(employee);
    }

    public EmployeeDto.Response getEmployeeByCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        return employeeMapper.toDto(employee);
    }

    public List<EmployeeDto.Response> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name).stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDto.Response> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeDto.Response createEmployee(EmployeeDto.Request employeeDto) {
        // Check if employee code already exists
        if (employeeRepository.existsByEmployeeCode(employeeDto.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee code already exists: " + employeeDto.getEmployeeCode());
        }

        Employee employee = employeeMapper.toEntity(employeeDto);

        // Handle addresses
        Set<Address> addresses = new HashSet<>();
        employeeDto.getAddresses().forEach(addressDto -> {
            Address address = addressMapper.toEntity(addressDto);
            address.setEmployee(employee);
            addresses.add(address);
        });
        employee.setAddresses(addresses);

        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDto.Response updateEmployee(Long id, EmployeeDto.UpdateRequest updateDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employeeMapper.updateEmployeeFromDto(updateDto, employee);

        // Update addresses
        if (updateDto.getAddresses() != null && !updateDto.getAddresses().isEmpty()) {
            // Remove existing addresses
            employee.getAddresses().clear();

            // Add new addresses
            updateDto.getAddresses().forEach(addressDto -> {
                Address address = addressMapper.toEntity(addressDto);
                address.setEmployee(employee);
                employee.getAddresses().add(address);
            });
        }

        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDto.Response assignDepartment(Long employeeId, Long departmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));

        employee.addDepartment(department);

        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDto.Response removeDepartment(Long employeeId, Long departmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));

        employee.removeDepartment(department);

        return employeeMapper.toDto(employeeRepository.save(employee));
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