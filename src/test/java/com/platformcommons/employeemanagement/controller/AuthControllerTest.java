package com.platformcommons.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platformcommons.employeemanagement.dto.AuthDto;
import com.platformcommons.employeemanagement.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private AuthDto.LoginRequest adminLoginRequest;
    private AuthDto.EmployeeLoginRequest employeeLoginRequest;
    private AuthDto.JwtResponse jwtResponse;

    @BeforeEach
    public void setup() {
        // Setup admin login request
        adminLoginRequest = new AuthDto.LoginRequest();
        adminLoginRequest.setUsername("admin");
        adminLoginRequest.setPassword("password");

        // Setup employee login request
        employeeLoginRequest = new AuthDto.EmployeeLoginRequest();
        employeeLoginRequest.setEmployeeCode("EMP001");
        employeeLoginRequest.setDateOfBirth(LocalDate.of(2000, 10, 18));

        // Setup JWT response
        jwtResponse = AuthDto.JwtResponse.builder()
                .token("test-jwt-token")
                .type("Bearer")
                .id(1L)
                .username("admin")
                .roles(Collections.singletonList("ROLE_ADMIN"))
                .build();
    }

    @Test
    public void testAdminLogin_Success() throws Exception {
        when(authService.authenticateAdmin(any(AuthDto.LoginRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", is("test-jwt-token")))
                .andExpect(jsonPath("$.type", is("Bearer")))
                .andExpect(jsonPath("$.username", is("admin")));

        verify(authService, times(1)).authenticateAdmin(any(AuthDto.LoginRequest.class));
    }

    @Test
    public void testAdminLogin_BadRequest() throws Exception {
        adminLoginRequest.setUsername(null); // Invalid request

        mockMvc.perform(post("/auth/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).authenticateAdmin(any(AuthDto.LoginRequest.class));
    }

    @Test
    public void testEmployeeLogin_Success() throws Exception {
        AuthDto.JwtResponse employeeResponse = AuthDto.JwtResponse.builder()
                .token("test-employee-token")
                .type("Bearer")
                .username("EMP001")
                .roles(Collections.singletonList("ROLE_EMPLOYEE"))
                .build();

        when(authService.authenticateEmployee(any(AuthDto.EmployeeLoginRequest.class))).thenReturn(employeeResponse);

        mockMvc.perform(post("/auth/employee/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", is("test-employee-token")))
                .andExpect(jsonPath("$.type", is("Bearer")))
                .andExpect(jsonPath("$.username", is("EMP001")));

        verify(authService, times(1)).authenticateEmployee(any(AuthDto.EmployeeLoginRequest.class));
    }

    @Test
    public void testEmployeeLogin_BadRequest() throws Exception {
        employeeLoginRequest.setEmployeeCode(null); // Invalid request

        mockMvc.perform(post("/auth/employee/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeLoginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).authenticateEmployee(any(AuthDto.EmployeeLoginRequest.class));
    }

    @Test
    public void testEmployeeLogin_InvalidCredentials() throws Exception {
        when(authService.authenticateEmployee(any(AuthDto.EmployeeLoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid employee credentials"));

        mockMvc.perform(post("/auth/employee/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeLoginRequest)))
                .andExpect(status().isInternalServerError());

        verify(authService, times(1)).authenticateEmployee(any(AuthDto.EmployeeLoginRequest.class));
    }
}