package com.platformcommons.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.platformcommons.employeemanagement.dto.*;
import com.platformcommons.employeemanagement.entity.Address;
import com.platformcommons.employeemanagement.entity.Department;
import com.platformcommons.employeemanagement.entity.Employee;
import com.platformcommons.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private ObjectMapper objectMapper;
    private EmployeeRequest employeeRequest;
    private EmployeeResponse employeeResponse;
    private EmployeeUpdateRequest updateRequest;
    private AddressRequest addressRequest;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For LocalDate serialization

        Set<AddressRequest> addressRequests = new HashSet<>();
        addressRequest = new AddressRequest(
                Address.AddressType.PERMANENT,
                "91 Lawrence Street",
                "Kolkata",
                "West Bengal",
                "India",
                "712258");
        addressRequests.add(addressRequest);

        Set<AddressResponse> addressResponses = new HashSet<>();
        AddressResponse addressResponse = new AddressResponse(
                1L,
                Address.AddressType.PERMANENT,
                "91 Lawrence Street",
                "Kolkata",
                "West Bengal",
                "India",
                "712258"
        );
        addressResponses.add(addressResponse);

        employeeRequest = new EmployeeRequest(
                "Mriganka Mondal",
                LocalDate.of(2000, 10, 18),
                Employee.Gender.MALE,
                "EMP001",
                "mrigankamondal10@gmail.com",
                "7596943998",
                "6291888393",
                addressRequests);

        employeeResponse = new EmployeeResponse(
                1L,
                "Mriganka Mondal",
                LocalDate.of(2000, 10, 18),
                Employee.Gender.MALE,
                "EMP001",
                "mrigankamondal10@gmail.com",
                "7596943998",
                "6291888393",
                addressResponses,
                new HashSet<>()
        );

        updateRequest = new EmployeeUpdateRequest(
                "mrigankamondal10@gmail.com",
                "7596943998",
                "6291888393");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllEmployees() throws Exception {
        List<EmployeeResponse> employees = Arrays.asList(employeeResponse);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Mriganka Mondal")))
                .andExpect(jsonPath("$[0].employeeCode", is("EMP001")));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(anyLong())).thenReturn(employeeResponse);

        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mriganka Mondal")))
                .andExpect(jsonPath("$.employeeCode", is("EMP001")));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetEmployeeByCode() throws Exception {
        when(employeeService.getEmployeeByCode(anyString())).thenReturn(employeeResponse);

        mockMvc.perform(get("/employee/code/EMP001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mriganka Mondal")))
                .andExpect(jsonPath("$.employeeCode", is("EMP001")));

        verify(employeeService, times(1)).getEmployeeByCode("EMP001");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSearchEmployeesByName() throws Exception {
        List<EmployeeResponse> employees = Arrays.asList(employeeResponse);
        when(employeeService.searchEmployeesByName(anyString())).thenReturn(employees);

        mockMvc.perform(get("/employee/search").param("name", "Mriganka"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Mriganka Mondal")));

        verify(employeeService, times(1)).searchEmployeesByName("Mriganka");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetEmployeesByDepartment() throws Exception {
        List<EmployeeResponse> employees = Arrays.asList(employeeResponse);
        when(employeeService.getEmployeesByDepartment(anyLong())).thenReturn(employees);

        mockMvc.perform(get("/employee/department/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Mriganka Mondal")));

        verify(employeeService, times(1)).getEmployeesByDepartment(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateEmployee() throws Exception {
        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(employeeResponse);

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mriganka Mondal")));

        verify(employeeService, times(1)).createEmployee(any(EmployeeRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testUpdateEmployee() throws Exception {
        when(employeeService.updateEmployee(anyLong(), any(EmployeeUpdateRequest.class))).thenReturn(employeeResponse);

        mockMvc.perform(put("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mriganka Mondal")));

        verify(employeeService, times(1)).updateEmployee(anyLong(), any(EmployeeUpdateRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testAddAddress() throws Exception {
        when(employeeService.addAddress(anyLong(), any(AddressRequest.class))).thenReturn(employeeResponse);

        mockMvc.perform(post("/employee/1/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mriganka Mondal")));

        verify(employeeService, times(1)).addAddress(anyLong(), any(AddressRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testUpdateAddress() throws Exception {
        when(employeeService.updateAddress(anyLong(), anyLong(), any(AddressRequest.class)))
                .thenReturn(employeeResponse);

        mockMvc.perform(put("/employee/1/address/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mriganka Mondal")));

        verify(employeeService, times(1))
                .updateAddress(anyLong(), anyLong(), any(AddressRequest.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testRemoveAddress() throws Exception {
        when(employeeService.removeAddress(anyLong(), anyLong())).thenReturn(employeeResponse);

        mockMvc.perform(delete("/employee/1/address/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mriganka Mondal")));

        verify(employeeService, times(1)).removeAddress(anyLong(), anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAssignDepartment() throws Exception {
        when(employeeService.assignDepartment(anyLong(), anyLong())).thenReturn(employeeResponse);

        mockMvc.perform(put("/employee/1/department/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mriganka Mondal")));

        verify(employeeService, times(1)).assignDepartment(anyLong(), anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testRemoveDepartment() throws Exception {
        when(employeeService.removeDepartment(anyLong(), anyLong())).thenReturn(employeeResponse);

        mockMvc.perform(delete("/employee/1/department/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mriganka Mondal")));

        verify(employeeService, times(1)).removeDepartment(anyLong(), anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(anyLong());

        mockMvc.perform(delete("/employee/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(anyLong());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testForbiddenAccess() throws Exception {
        // Test endpoints that require ADMIN role when accessed by EMPLOYEE
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/employee/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUnauthenticatedAccess() throws Exception {
        // Test endpoints without authentication
        mockMvc.perform(get("/employee"))
                .andExpect(status().isUnauthorized());
    }


}