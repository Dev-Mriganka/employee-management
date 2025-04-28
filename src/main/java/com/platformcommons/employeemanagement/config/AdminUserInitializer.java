package com.platformcommons.employeemanagement.config;


import com.platformcommons.employeemanagement.entity.Role;
import com.platformcommons.employeemanagement.entity.User;
import com.platformcommons.employeemanagement.repository.RoleRepository;
import com.platformcommons.employeemanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(UserRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(Role.ERole.ROLE_ADMIN)));

        Role employeeRole = roleRepository.findByName(Role.ERole.ROLE_EMPLOYEE)
                .orElseGet(() -> roleRepository.save(new Role(Role.ERole.ROLE_EMPLOYEE)));

        // Create an admin user if none exists
        if (userRepository.findByRolesContaining(adminRole).isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);

            System.out.println("Initial admin user created with username: admin and password: Admin@123");
        }
    }
}