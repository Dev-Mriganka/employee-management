package com.platformcommons.employeemanagement.service;

import com.platformcommons.employeemanagement.dto.AuthDto;
import com.platformcommons.employeemanagement.entity.Role;
import com.platformcommons.employeemanagement.security.JwtUtils;
import com.platformcommons.employeemanagement.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmployeeService employeeService;

    public AuthDto.JwtResponse authenticateAdmin(AuthDto.LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return AuthDto.JwtResponse.builder()
                    .token(jwt)
                    .type("Bearer")
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .roles(roles)
                    .build();
        } catch (BadCredentialsException ex) {
            // Let this propagate up to the controller
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication processing failed");
        }
    }

    public AuthDto.JwtResponse authenticateEmployee(AuthDto.EmployeeLoginRequest loginRequest) {
        try {
            boolean isValid = employeeService.validateEmployeeCredentials(
                    loginRequest.getEmployeeCode(), loginRequest.getDateOfBirth());

            if (!isValid) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid employee credentials");
            }

            String jwt = jwtUtils.generateJwtTokenForEmployee(loginRequest.getEmployeeCode());

            return AuthDto.JwtResponse.builder()
                    .token(jwt)
                    .type("Bearer")
                    .username(loginRequest.getEmployeeCode())
                    .roles(Collections.singletonList(Role.ERole.ROLE_EMPLOYEE.name()))
                    .build();
        } catch (ResponseStatusException ex) {
            // Let this propagate up to the controller
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Employee authentication failed");
        }
    }
}