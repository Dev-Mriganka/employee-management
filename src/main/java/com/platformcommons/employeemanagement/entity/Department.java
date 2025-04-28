package com.platformcommons.employeemanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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
    @Column(name = "department_type", nullable = false)
    private DepartmentType departmentType;

    private String responsibilities;

    @ManyToMany(mappedBy = "departments", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Employee> employees = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum DepartmentType {
        TECHNICAL, HR, FINANCE, MARKETING, OPERATIONS, CUSTOMER_SUPPORT
    }
}
