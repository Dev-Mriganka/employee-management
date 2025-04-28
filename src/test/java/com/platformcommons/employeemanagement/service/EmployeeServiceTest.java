package com.platformcommons.employeemanagement.service;

import com.platformcommons.employeemanagement.dto.*;
import com.platformcommons.employeemanagement.entity.Address;
import com.platformcommons.employeemanagement.entity.Department;
import com.platformcommons.employeemanagement.entity.Employee;
import com.platformcommons.employeemanagement.exception.ResourceNotFoundException;
import com.platformcommons.employeemanagement.mapper.AddressMapper;
import com.platformcommons.employeemanagement.mapper.EmployeeMapper;
import com.platformcommons.employeemanagement.repository.DepartmentRepository;
import com.platformcommons.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeResponse employeeResponse;
    private EmployeeRequest employeeRequest;
    private Department department;
    private Address address;
    private AddressRequest addressRequest;
    private EmployeeUpdateRequest updateRequest;

    @BeforeEach
    public void setup() {
        // Initialize test data
        employee = new Employee();
        employee.setId(1L);
        employee.setName("Mriganka Mondal");
        employee.setEmployeeCode("EMP001");
        employee.setDateOfBirth(LocalDate.of(2000, 10, 18));
        employee.setEmail("mrigankamondal10@gmail.com");
        employee.setGender(Employee.Gender.MALE);
        employee.setMobileNumber("7596943998");
        employee.setEmergencyContact("6291888393");

        department = new Department();
        department.setId(1L);
        department.setDepartmentName("IT Department");
        department.setDescription("Information Technology");
        department.setDepartmentType(Department.DepartmentType.TECHNICAL);

        address = new Address();
        address.setId(1L);
        address.setAddressType(Address.AddressType.PERMANENT);
        address.setStreet("91 Lawrence Street");
        address.setCity("Kolkata");
        address.setState("West Bengal");
        address.setCountry("India");
        address.setPostalCode("712258");

        Set<Address> addresses = new HashSet<>();
        addresses.add(address);
        employee.setAddresses(addresses);

        Set<Department> departments = new HashSet<>();
        departments.add(department);
        employee.setDepartments(departments);

        employeeResponse = new EmployeeResponse(
                1L, "Mriganka Mondal", LocalDate.of(2000, 10, 18), Employee.Gender.MALE, "EMP001",  "mrigankamondal10@gmail.com", "7596943998", "6291888393",
                new HashSet<>(), new HashSet<>()
        );

        Set<AddressRequest> addressRequests = new HashSet<>();
        addressRequest = new AddressRequest(Address.AddressType.PERMANENT, "91 Lawrence Street", "Kolkata", "West Bengal", "India", "712258");
        addressRequests.add(addressRequest);

        employeeRequest = new EmployeeRequest(
                "Mriganka Mondal", LocalDate.of(2000, 10, 18), Employee.Gender.MALE, "EMP001",  "mrigankamondal10@gmail.com", "7596943998", "6291888393", addressRequests);

        updateRequest = new EmployeeUpdateRequest("mrigankamondal10@gmail.com", "7596943998", "6291888393");
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mriganka Mondal", result.get(0).name());
        assertEquals("EMP001", result.get(0).employeeCode());

        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testGetEmployeeById() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);
        
        EmployeeResponse result = employeeService.getEmployeeById(1L);
        
        assertNotNull(result);
        assertEquals("Mriganka Mondal", result.name());
        assertEquals("EMP001", result.employeeCode());

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(1L));

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeMapper, never()).toResponse(any(Employee.class));
    }

    @Test
    public void testGetEmployeeByCode() {
        when(employeeRepository.findByEmployeeCode(anyString())).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.getEmployeeByCode("EMP001");

        assertNotNull(result);
        assertEquals("Mriganka Mondal", result.name());
        assertEquals("EMP001", result.employeeCode());

        verify(employeeRepository, times(1)).findByEmployeeCode(anyString());
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testSearchEmployeesByName() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        when(employeeRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(employees);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        List<EmployeeResponse> result = employeeService.searchEmployeesByName("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mriganka Mondal", result.get(0).name());

        verify(employeeRepository, times(1)).findByNameContainingIgnoreCase(anyString());
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testGetEmployeesByDepartment() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        when(employeeRepository.findByDepartmentId(anyLong())).thenReturn(employees);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);
        
        List<EmployeeResponse> result = employeeService.getEmployeesByDepartment(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mriganka Mondal", result.get(0).name());

        verify(employeeRepository, times(1)).findByDepartmentId(anyLong());
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testCreateEmployee() {
        when(employeeRepository.existsByEmployeeCode(anyString())).thenReturn(false);
        when(employeeMapper.toEntity(any(EmployeeRequest.class))).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);
        when(addressMapper.toEntity(any(AddressRequest.class))).thenReturn(address);
        
        EmployeeResponse result = employeeService.createEmployee(employeeRequest);
        
        assertNotNull(result);
        assertEquals("Mriganka Mondal", result.name());
        assertEquals("EMP001", result.employeeCode());

        verify(employeeRepository, times(1)).existsByEmployeeCode(anyString());
        verify(employeeMapper, times(1)).toEntity(any(EmployeeRequest.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testCreateEmployeeWithExistingCode() {
        when(employeeRepository.existsByEmployeeCode(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(employeeRequest));

        verify(employeeRepository, times(1)).existsByEmployeeCode(anyString());
        verify(employeeMapper, never()).toEntity(any(EmployeeRequest.class));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);
        
        EmployeeResponse result = employeeService.updateEmployee(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Mriganka Mondal", result.name());
        assertEquals("EMP001", result.employeeCode());

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeMapper, times(1)).updateFromRequest(any(EmployeeUpdateRequest.class), any(Employee.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testAddAddress() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(addressMapper.toEntity(any(AddressRequest.class))).thenReturn(address);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.addAddress(1L, addressRequest);

        assertNotNull(result);

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(addressMapper, times(1)).toEntity(any(AddressRequest.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testUpdateAddress() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.updateAddress(1L, 1L, addressRequest);

        assertNotNull(result);

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(addressMapper, times(1)).updateFromRequest(addressRequest, address);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testRemoveAddress() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.removeAddress(1L, 1L);
        assertNotNull(result);

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }
    

    @Test
    public void testAssignDepartment() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.assignDepartment(1L, 1L);
        
        assertNotNull(result);

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(departmentRepository, times(1)).findById(anyLong());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testRemoveDepartment() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.removeDepartment(1L, 1L);

        assertNotNull(result);

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(departmentRepository, times(1)).findById(anyLong());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toResponse(any(Employee.class));
    }

    @Test
    public void testDeleteEmployee() {
        when(employeeRepository.existsById(anyLong())).thenReturn(true);

        employeeService.deleteEmployee(1L);
        
        verify(employeeRepository, times(1)).existsById(anyLong());
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteEmployeeNotFound() {
        when(employeeRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(1L));

        verify(employeeRepository, times(1)).existsById(anyLong());
        verify(employeeRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testValidateEmployeeCredentials() {
        LocalDate dob = LocalDate.of(2000, 10, 18);
        when(employeeRepository.findByEmployeeCodeAndDateOfBirth("EMP001", dob))
                .thenReturn(Optional.of(employee));
        
        boolean result = employeeService.validateEmployeeCredentials("EMP001", dob);

        assertTrue(result);
        verify(employeeRepository, times(1)).findByEmployeeCodeAndDateOfBirth(anyString(), any(LocalDate.class));
    }

    @Test
    public void testValidateEmployeeCredentialsInvalid() {
        LocalDate dob = LocalDate.of(2000, 10, 18);
        when(employeeRepository.findByEmployeeCodeAndDateOfBirth("EMP001", dob))
                .thenReturn(Optional.empty());
        
        boolean result = employeeService.validateEmployeeCredentials("EMP001", dob);
        
        assertFalse(result);
        verify(employeeRepository, times(1)).findByEmployeeCodeAndDateOfBirth(anyString(), any(LocalDate.class));
    }
}