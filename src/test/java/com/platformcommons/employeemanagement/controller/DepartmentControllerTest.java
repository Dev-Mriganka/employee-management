package com.platformcommons.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platformcommons.employeemanagement.dto.DepartmentDetailedResponse;
import com.platformcommons.employeemanagement.dto.DepartmentRequest;
import com.platformcommons.employeemanagement.dto.DepartmentResponse;
import com.platformcommons.employeemanagement.entity.Department;
import com.platformcommons.employeemanagement.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService departmentService;

    private DepartmentRequest departmentRequest;
    private DepartmentResponse departmentResponse;
    private DepartmentDetailedResponse departmentDetailedResponse;
    private String responsibilities = "Develop software, Maintain systems";

    @BeforeEach
    public void setup() {
        departmentRequest = new DepartmentRequest(
                "IT Department", "Information Technology", Department.DepartmentType.TECHNICAL, responsibilities);

        departmentResponse = new DepartmentResponse(
                1L, "IT Department", "Information Technology", Department.DepartmentType.TECHNICAL, responsibilities);

        departmentDetailedResponse = new DepartmentDetailedResponse(
                1L, "IT Department", "Information Technology", Department.DepartmentType.TECHNICAL, responsibilities, new HashSet<>()
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllDepartments() throws Exception {
        List<DepartmentResponse> departments = Arrays.asList(departmentResponse);
        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].departmentName", is("IT Department")))
                .andExpect(jsonPath("$[0].departmentType", is("TECHNICAL")));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetDepartmentById() throws Exception {
        when(departmentService.getDepartmentById(anyLong())).thenReturn(departmentResponse);

        mockMvc.perform(get("/department/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.departmentName", is("IT Department")))
                .andExpect(jsonPath("$.departmentType", is("TECHNICAL")));

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetDepartmentWithEmployees() throws Exception {
        when(departmentService.getDepartmentWithEmployees(anyLong())).thenReturn(departmentDetailedResponse);

        mockMvc.perform(get("/department/1/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.departmentName", is("IT Department")))
                .andExpect(jsonPath("$.employees", hasSize(0)));

        verify(departmentService, times(1)).getDepartmentWithEmployees(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateDepartment() throws Exception {
        when(departmentService.createDepartment(any(DepartmentRequest.class))).thenReturn(departmentResponse);

        mockMvc.perform(post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.departmentName", is("IT Department")));

        verify(departmentService, times(1)).createDepartment(any(DepartmentRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateDepartment() throws Exception {
        when(departmentService.updateDepartment(anyLong(), any(DepartmentRequest.class))).thenReturn(departmentResponse);

        mockMvc.perform(put("/department/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.departmentName", is("IT Department")));

        verify(departmentService, times(1)).updateDepartment(anyLong(), any(DepartmentRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(anyLong());

        mockMvc.perform(delete("/department/1"))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).deleteDepartment(1L);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testEmployeeCanAccessDepartmentById() throws Exception {
        when(departmentService.getDepartmentById(anyLong())).thenReturn(departmentResponse);

        mockMvc.perform(get("/department/1"))
                .andExpect(status().isOk());

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testEmployeeCannotAccessEmployeesList() throws Exception {
        mockMvc.perform(get("/department/1/employees"))
                .andExpect(status().isForbidden());

        verify(departmentService, never()).getDepartmentWithEmployees(anyLong());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/department"))
                .andExpect(status().isUnauthorized());

        verify(departmentService, never()).getAllDepartments();
    }
}