package com.platformcommons.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepartmentType departmentType;

    @Column(nullable = false)
    private Integer employeeCount = 0;

    private String responsibilities;

    @ManyToMany(mappedBy = "departments", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    public enum DepartmentType {
        TECHNICAL, HR, FINANCE, MARKETING, OPERATIONS, CUSTOMER_SUPPORT
    }
}
