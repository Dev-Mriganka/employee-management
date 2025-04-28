package com.platformcommons.employeemanagement.service;

import com.platformcommons.employeemanagement.dto.AuthDto;
import com.platformcommons.employeemanagement.entity.Role;
import com.platformcommons.employeemanagement.security.JwtUtils;
import com.platformcommons.employeemanagement.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private AuthDto.LoginRequest loginRequest;
    private AuthDto.EmployeeLoginRequest employeeLoginRequest;
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setup() {
        // Setup admin login request
        loginRequest = new AuthDto.LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");

        // Setup employee login request
        employeeLoginRequest = new AuthDto.EmployeeLoginRequest();
        employeeLoginRequest.setEmployeeCode("EMP001");
        employeeLoginRequest.setDateOfBirth(LocalDate.of(2000, 10, 18));

        // Setup user details
        userDetails = new UserDetailsImpl(
                1L,
                "admin",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority(Role.ERole.ROLE_ADMIN.name()))
        );
    }

    @Test
    public void testAuthenticateAdmin() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("test-jwt-token");

        // Act
        AuthDto.JwtResponse response = authService.authenticateAdmin(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test-jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals(1L, response.getId());
        assertEquals("admin", response.getUsername());
        assertEquals(1, response.getRoles().size());
        assertEquals(Role.ERole.ROLE_ADMIN.name(), response.getRoles().get(0));

        // Verify
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(any(Authentication.class));
    }

    @Test
    public void testAuthenticateEmployee_ValidCredentials() {
        // Arrange
        when(employeeService.validateEmployeeCredentials(anyString(), any(LocalDate.class)))
                .thenReturn(true);
        when(jwtUtils.generateJwtTokenForEmployee(anyString())).thenReturn("test-employee-jwt-token");

        // Act
        AuthDto.JwtResponse response = authService.authenticateEmployee(employeeLoginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test-employee-jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("EMP001", response.getUsername());
        assertEquals(1, response.getRoles().size());
        assertEquals(Role.ERole.ROLE_EMPLOYEE.name(), response.getRoles().get(0));

        // Verify
        verify(employeeService, times(1)).validateEmployeeCredentials(anyString(), any(LocalDate.class));
        verify(jwtUtils, times(1)).generateJwtTokenForEmployee(anyString());
    }

    @Test
    public void testAuthenticateEmployee_InvalidCredentials() {
        // Arrange
        when(employeeService.validateEmployeeCredentials(anyString(), any(LocalDate.class)))
                .thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticateEmployee(employeeLoginRequest);
        });

        assertEquals("Invalid employee credentials", exception.getMessage());

        // Verify
        verify(employeeService, times(1)).validateEmployeeCredentials(anyString(), any(LocalDate.class));
        verify(jwtUtils, never()).generateJwtTokenForEmployee(anyString());
    }
}