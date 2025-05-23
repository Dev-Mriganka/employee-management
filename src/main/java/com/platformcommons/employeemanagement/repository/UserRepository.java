package com.platformcommons.employeemanagement.repository;


import com.platformcommons.employeemanagement.entity.Role;
import com.platformcommons.employeemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    List<User> findByRolesContaining(Role adminRole);
}