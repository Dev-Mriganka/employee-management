package com.platformcommons.employeemanagement.service;

import com.platformcommons.employeemanagement.dto.DepartmentDetailedResponse;
import com.platformcommons.employeemanagement.dto.DepartmentRequest;
import com.platformcommons.employeemanagement.dto.DepartmentResponse;
import com.platformcommons.employeemanagement.entity.Department;
import com.platformcommons.employeemanagement.entity.Employee;
import com.platformcommons.employeemanagement.exception.ResourceNotFoundException;
import com.platformcommons.employeemanagement.mapper.DepartmentMapper;
import com.platformcommons.employeemanagement.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;
    private DepartmentResponse departmentResponse;
    private DepartmentDetailedResponse departmentDetailedResponse;
    private DepartmentRequest departmentRequest;
    private String responsibilities = "Develop software, Maintain systems";

    @BeforeEach
    public void setup() {
        // Initialize test data
        department = new Department();
        department.setId(1L);
        department.setDepartmentName("IT Department");
        department.setDescription("Information Technology");
        department.setDepartmentType(Department.DepartmentType.TECHNICAL);
        department.setResponsibilities(responsibilities);

        Set<Employee> employees = new HashSet<>();
        department.setEmployees(employees);

        departmentResponse = new DepartmentResponse(
                1L, "IT Department", "Information Technology", Department.DepartmentType.TECHNICAL,  responsibilities);

        departmentDetailedResponse = new DepartmentDetailedResponse(
                1L, "IT Department", "Information Technology", Department.DepartmentType.TECHNICAL, responsibilities, new HashSet<>()
        );

        departmentRequest = new DepartmentRequest(
                "IT Department", "Information Technology", Department.DepartmentType.TECHNICAL, responsibilities);
    }

    @Test
    public void testGetAllDepartments() {
        List<Department> departments = new ArrayList<>();
        departments.add(department);

        when(departmentRepository.findAll()).thenReturn(departments);
        when(departmentMapper.toResponse(any(Department.class))).thenReturn(departmentResponse);

        List<DepartmentResponse> result = departmentService.getAllDepartments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("IT Department", result.get(0).departmentName());
        assertEquals(Department.DepartmentType.TECHNICAL, result.get(0).departmentType());

        verify(departmentRepository, times(1)).findAll();
        verify(departmentMapper, times(1)).toResponse(any(Department.class));
    }

    @Test
    public void testGetDepartmentById() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(departmentMapper.toResponse(any(Department.class))).thenReturn(departmentResponse);

        DepartmentResponse result = departmentService.getDepartmentById(1L);

        assertNotNull(result);
        assertEquals("IT Department", result.departmentName());
        assertEquals(Department.DepartmentType.TECHNICAL, result.departmentType());

        verify(departmentRepository, times(1)).findById(anyLong());
        verify(departmentMapper, times(1)).toResponse(any(Department.class));
    }

    @Test
    public void testGetDepartmentByIdNotFound() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.getDepartmentById(1L));

        verify(departmentRepository, times(1)).findById(anyLong());
        verify(departmentMapper, never()).toResponse(any(Department.class));
    }

    @Test
    public void testGetDepartmentWithEmployees() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(departmentMapper.toDetailedResponse(any(Department.class))).thenReturn(departmentDetailedResponse);

        DepartmentDetailedResponse result = departmentService.getDepartmentWithEmployees(1L);

        assertNotNull(result);
        assertEquals("IT Department", result.departmentName());
        assertEquals(Department.DepartmentType.TECHNICAL, result.departmentType());

        verify(departmentRepository, times(1)).findById(anyLong());
        verify(departmentMapper, times(1)).toDetailedResponse(any(Department.class));
    }

    @Test
    public void testCreateDepartment() {
        when(departmentRepository.existsByDepartmentName(anyString())).thenReturn(false);
        when(departmentMapper.toEntity(any(DepartmentRequest.class))).thenReturn(department);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponse(any(Department.class))).thenReturn(departmentResponse);

        DepartmentResponse result = departmentService.createDepartment(departmentRequest);

        assertNotNull(result);
        assertEquals("IT Department", result.departmentName());
        assertEquals(Department.DepartmentType.TECHNICAL, result.departmentType());

        verify(departmentRepository, times(1)).existsByDepartmentName(anyString());
        verify(departmentMapper, times(1)).toEntity(any(DepartmentRequest.class));
        verify(departmentRepository, times(1)).save(any(Department.class));
        verify(departmentMapper, times(1)).toResponse(any(Department.class));
    }

    @Test
    public void testCreateDepartmentWithExistingName() {
        when(departmentRepository.existsByDepartmentName(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> departmentService.createDepartment(departmentRequest));

        verify(departmentRepository, times(1)).existsByDepartmentName(anyString());
        verify(departmentMapper, never()).toEntity(any(DepartmentRequest.class));
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    public void testUpdateDepartment() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponse(any(Department.class))).thenReturn(departmentResponse);

        DepartmentResponse result = departmentService.updateDepartment(1L, departmentRequest);

        assertNotNull(result);
        assertEquals("IT Department", result.departmentName());
        assertEquals(Department.DepartmentType.TECHNICAL, result.departmentType());

        verify(departmentRepository, times(1)).findById(anyLong());
        verify(departmentRepository, never()).existsByDepartmentName(anyString()); // Verify it's not called
        verify(departmentRepository, times(1)).save(any(Department.class));
        verify(departmentMapper, times(1)).toResponse(any(Department.class));
    }

    @Test
    public void testUpdateDepartmentWithExistingName() {
        DepartmentRequest newRequest = new DepartmentRequest(
                "New Department", "New Description", Department.DepartmentType.HR, responsibilities
        );

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(departmentRepository.existsByDepartmentName("New Department")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> departmentService.updateDepartment(1L, newRequest));

        verify(departmentRepository, times(1)).findById(anyLong());
        verify(departmentRepository, times(1)).existsByDepartmentName(anyString());
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    public void testDeleteDepartment() {
        when(departmentRepository.existsById(anyLong())).thenReturn(true);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).existsById(anyLong());
        verify(departmentRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteDepartmentNotFound() {
        when(departmentRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> departmentService.deleteDepartment(1L));

        verify(departmentRepository, times(1)).existsById(anyLong());
        verify(departmentRepository, never()).deleteById(anyLong());
    }
}