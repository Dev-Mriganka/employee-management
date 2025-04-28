package com.platformcommons.employeemanagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

    @Column(unique = true)
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_department",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"))
    private Set<Department> departments = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Helper methods to manage bidirectional relationships
    public void addAddress(Address address) {
        addresses.add(address);
        address.setEmployee(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setEmployee(null);
    }

    public void addDepartment(Department department) {
        departments.add(department);
        department.getEmployees().add(this);
    }

    public void removeDepartment(Department department) {
        departments.remove(department);
        department.getEmployees().remove(this);
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
