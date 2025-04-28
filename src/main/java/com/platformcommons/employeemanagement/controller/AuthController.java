package com.platformcommons.employeemanagement.controller;

import com.platformcommons.employeemanagement.dto.AuthDto;
import com.platformcommons.employeemanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/admin/login")
    @Operation(summary = "Admin login", description = "Authenticates admin with username and password")
    public ResponseEntity<AuthDto.JwtResponse> authenticateAdmin(@Valid @RequestBody AuthDto.LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateAdmin(loginRequest));
    }

    @PostMapping("/employee/login")
    @Operation(summary = "Employee login", description = "Authenticates employee with employee code and date of birth")
    public ResponseEntity<AuthDto.JwtResponse> authenticateEmployee(@Valid @RequestBody AuthDto.EmployeeLoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateEmployee(loginRequest));
    }

}